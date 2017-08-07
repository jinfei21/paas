package com.yjfei.paas.mesos;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.mesos.Protos.Attribute;
import org.apache.mesos.Protos.Offer;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yjfei.paas.common.CleanupType;
import com.yjfei.paas.common.DeployState;
import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.common.PaasTaskState;
import com.yjfei.paas.configuration.PaasConfiguration;
import com.yjfei.paas.data.CleanupTask;
import com.yjfei.paas.data.Deploy;
import com.yjfei.paas.data.DeployStatistics;
import com.yjfei.paas.data.DeployTaskRequest;
import com.yjfei.paas.data.PaasTaskId;
import com.yjfei.paas.data.Rack;
import com.yjfei.paas.data.Resources;
import com.yjfei.paas.data.ScheduleTask;
import com.yjfei.paas.data.Slave;
import com.yjfei.paas.manage.DeployManager;
import com.yjfei.paas.manage.RackManager;
import com.yjfei.paas.manage.SlaveManager;
import com.yjfei.paas.manage.TaskManager;
import com.yjfei.paas.poller.CooldownChecker;
import com.yjfei.paas.util.StringUtil;
import com.yjfei.paas.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SchedulerService {

	@Autowired
	private TaskManager taskManager;

	@Autowired
	private SlaveManager slaveManager;

	@Autowired
	private RackManager rackManager;

	@Autowired
	private DeployManager deployManager;

	@Autowired
	private PaasConfiguration configuration;

	@Autowired
	private CooldownChecker cooldown;

	private String rackIdAttributeKey;

	private String defaultRackId;

	@PostConstruct
	public void init() {
		this.rackIdAttributeKey = configuration.getMesosConfiguration().getRackIdAttributeKey();
		this.defaultRackId = configuration.getMesosConfiguration().getDefaultRackId();
	}

	public void checkForDecomissions() {

		final long start = System.currentTimeMillis();

		final List<ScheduleTask> activeTaskList = taskManager.getAllActiveTask();

		final List<Slave> slaves = slaveManager.getDecomissionSlaves();
		final List<Rack> racks = rackManager.getDecomissionRacks();

		if (!racks.isEmpty()) {
			List<Slave> rackSlaves = slaveManager.getSlaveByRack(racks);
			slaves.addAll(rackSlaves);
		}

		final Set<ScheduleTask> rescheduleTaskSet = Sets.newHashSet();
		for (Slave slave : slaves) {
			for (ScheduleTask activeTask : taskManager.getTasksOnSlave(activeTaskList, slave)) {
				cleanupTaskDueToDecomission(rescheduleTaskSet, activeTask, slave.getSlaveId());
			}
		}

		// TODO 重新调度task
		for (ScheduleTask task : rescheduleTaskSet) {
			taskManager.updateScheduleTaskStatus(task.getId(), PaasTaskState.TASK_LAUNCHED);
		}

		if (slaves.isEmpty() && racks.isEmpty() && rescheduleTaskSet.isEmpty()) {
			log.trace("Decomission check found nothing");
		} else {
			log.info("Found {} decomissioning slaves, {} decomissioning racks, rescheduling {} task  for cleanup in {}",slaves.size(), racks.size(), rescheduleTaskSet.size(), TimeUtil.duration(start));
		}
	}

	private void cleanupTaskDueToDecomission(Set<ScheduleTask> rescheduleTaskSet, ScheduleTask task,
			String decomissioningObject) {
		if (rescheduleTaskSet.contains(task))
			return;
		rescheduleTaskSet.add(task);
		taskManager.createCleanupTask(new CleanupTask(task.getAppId(), task.getDeployId(), CleanupType.TASK_CANCEL,
				String.valueOf(task.getId())));
		log.trace("Scheduling a cleanup task for {} due to decomissioning {}", task.getId(), decomissioningObject);
	}

	public void drainPendingDeploy() {

		final long start = System.currentTimeMillis();

		List<Deploy> pendingDeploys = deployManager.getAllPendingDeploy();
		if (pendingDeploys.isEmpty()) {
			log.trace("Pending deploy was empty");
			return;
		}

		log.info("Pending deploy size: {} ", pendingDeploys.size());
		int totalNewScheduledTasks = 0;
		for (Deploy pendingDeploy : pendingDeploys) {
			
			
			List<ScheduleTask> matchTaskList = taskManager.getAllActiveScheduleTaskByDeployId(pendingDeploy.getId());

			int numScheduledTasks = scheduleTasks(pendingDeploy, matchTaskList);

			if (numScheduledTasks == 0) {
				log.info("appId:{},deployId:{} schedule no task,  because it is scheduled and has an active task",pendingDeploy.getAppId(), pendingDeploy.getId());
				continue;
			} else {
				log.trace("appId:{},deployId:{} schedule {} task。", pendingDeploy.getAppId(), pendingDeploy.getId(),numScheduledTasks);
			}
			totalNewScheduledTasks += numScheduledTasks;
			deployManager.updateStatusByDeployId(pendingDeploy.getId(), DeployState.SCHEDULE);
		}

		log.info("Scheduled {} new tasks in {}", totalNewScheduledTasks, TimeUtil.duration(start));

	}

	private int scheduleTasks(Deploy pendingDeploy, List<ScheduleTask> matchList) {

		final int numMissingInstances = getNumMissingInstances(pendingDeploy, matchList);

		if (numMissingInstances > 0) {
			final List<ScheduleTask> scheduleTasks = getScheduledTask(numMissingInstances, pendingDeploy, matchList);

			if (!scheduleTasks.isEmpty()) {
				log.trace("Scheduling tasks: {}", scheduleTasks);

				taskManager.createScheduleTask(scheduleTasks);
			} else {
				log.info("No new scheduled tasks found for {}, setting state to {}", pendingDeploy.getId(),DeployState.FINISHED);
				deployManager.finish(pendingDeploy.getId());
			}
		} else if (numMissingInstances < 0) {
			log.debug("Missing instances is negative: {}, deployid {}, matching tasks: {}", numMissingInstances,pendingDeploy.getId(), matchList);

			for (int i = 0; i < Math.abs(numMissingInstances); i++) {
				final ScheduleTask toCleanup = matchList.get(i);

				log.info("Cleaning up task {} due to new deploy {} - scaling down to {} instances", toCleanup.getId(),pendingDeploy.getId(), pendingDeploy.getInstances());

				taskManager.createCleanupTask(new CleanupTask(pendingDeploy.getAppId(), pendingDeploy.getId(),CleanupType.SCALE_DOWN, String.valueOf(toCleanup.getId())));
			}
		}

		return numMissingInstances;
	}

	private int getNumMissingInstances(Deploy pendingDeploy, List<ScheduleTask> activeList) {
		return pendingDeploy.getInstances() - activeList.size();
	}

	private List<ScheduleTask> getScheduledTask(int numMissingInstances, Deploy pendingDeploy,List<ScheduleTask> matchList) {
		DeployStatistics deployStatistics = getDeployStatistics(pendingDeploy.getId());
		final Optional<Long> nextRunAt = getNextRunAt(pendingDeploy, deployStatistics);
		if (!nextRunAt.isPresent()) {
			return Collections.emptyList();
		}

		final Set<Integer> inuseInstanceNumbers = Sets.newHashSetWithExpectedSize(matchList.size());
		for (ScheduleTask task : matchList) {
			inuseInstanceNumbers.add(task.getInstanceNo());
		}

		final List<ScheduleTask> newTasks = Lists.newArrayListWithCapacity(numMissingInstances);

		int nextInstanceNumber = 1;

		for (int i = 0; i < numMissingInstances; i++) {
			while (inuseInstanceNumbers.contains(nextInstanceNumber)) {
				nextInstanceNumber++;
			}

			newTasks.add(new ScheduleTask(0, PaasTaskState.TASK_LAUNCHED, pendingDeploy.getAppId(),pendingDeploy.getId(), nextInstanceNumber, new Date(nextRunAt.get()), null, -1, null));

			nextInstanceNumber++;
		}

		return newTasks;
	}

	private Optional<Long> getNextRunAt(Deploy pendingDeploy, DeployStatistics deployStatistics) {
		final long now = System.currentTimeMillis();

		long nextRunAt = now;

		if (!StringUtils.isEmpty(pendingDeploy.getQuartz())) {
			try {
				Date scheduleFrom = new Date(now);

				CronExpression cronExpression = new CronExpression(pendingDeploy.getQuartz());

				final Date nextRunAtDate = cronExpression.getNextValidTimeAfter(scheduleFrom);

				if (nextRunAtDate == null) {
					return Optional.absent();
				}

				log.trace("Calculating nextRunAtDate for {} (schedule: {}): {} (from: {})", pendingDeploy.getId(),
						pendingDeploy.getQuartz(), nextRunAtDate, scheduleFrom);

				nextRunAt = Math.max(nextRunAtDate.getTime(), now); // don't
																	// create a
																	// schedule
																	// that is
																	// overdue
																	// as this
																	// is used
																	// to
																	// indicate
																	// that
																	// singularity
																	// is not
																	// fulfilling
																	// requests.

				log.trace("Scheduling next run of {} (schedule: {}) at {} (from: {})", pendingDeploy.getId(),pendingDeploy.getQuartz(), nextRunAtDate, scheduleFrom);
			} catch (ParseException pe) {
				throw Throwables.propagate(pe);
			}
		}

		return Optional.of(nextRunAt);
	}

	public List<DeployTaskRequest> getDueTaskRequest() {
		List<DeployTaskRequest> dueTasks = Lists.newArrayList();
		List<ScheduleTask> taskList = taskManager.getAllLaunchTask();

		if(CollectionUtils.isEmpty(taskList)){
			log.trace("deploy task is empty.");
			return dueTasks;
		}
	
		//获取所有的cooldown状态
		List<Deploy> cooldownList = deployManager.getAllCoolDownDeploy();
		Set<Long> cooldownDeploySet = Sets.newHashSet();
		cooldownList.forEach( deploy -> {
			cooldownDeploySet.add(deploy.getId());			
		});

		Set<String> appIds = Sets.newHashSet();
		Set<Long> deployIds = Sets.newHashSet();
		taskList.forEach( task -> {
			appIds.add(task.getAppId());
			deployIds.add(task.getDeployId());
		});
		
		Map<String, Resources> resourceMap = deployManager.getAllResourceByAppId(appIds);
		
		Map<Long, Deploy> deployMap = deployManager.getAllDeployByDeployId(deployIds);
		final long now = System.currentTimeMillis();
		//获取调度的appId
		for (ScheduleTask task : taskList) {
			if (task.getScheduleTime().getTime() <= now && !cooldownDeploySet.contains(task.getDeployId())) {
				Resources resource = resourceMap.get(task.getAppId());

				Deploy deploy = deployMap.get(task.getDeployId());
				if (deploy == null || resource == null ) {
					log.warn("appId{} taskId{} deploy or resource is empty.",task.getAppId(),task.getId());
					continue;
				}
				dueTasks.add(new DeployTaskRequest(task, deploy, null, null, resource));
			}
		}

		return dueTasks;
	}

	public String getRackId(Offer offer) {
		for (Attribute attribute : offer.getAttributesList()) {
			if (attribute.getName().equals(rackIdAttributeKey)) {
				return StringUtil.getSafeString(attribute.getText().getValue());
			}
		}

		return defaultRackId;
	}

	private String getHost(String hostname) {
		return StringUtil.getSafeString(hostname.split("\\.")[0]);
	}

	public String getSlaveHost(Offer offer) {
		return getHost(offer.getHostname());
	}

	public MachineState checkOffer(List<DeployTaskRequest> dueTasks, DeployTaskRequest currentTask, Offer offer) {

		final String rackId = getRackId(offer);

		final boolean rackCheck = rackManager.isDecomission(rackId);

		if (rackCheck) {
			return MachineState.RACK_DECOMISSION;
		}
		final String slaveId = offer.getSlaveId().getValue();

		final boolean slaveCheck = slaveManager.isDecomission(slaveId);

		if (slaveCheck) {
			return MachineState.SLAVE_DECOMISSION;
		}

		if (!currentTask.getResource().getRackAffinity().isEmpty()) {
			if (!currentTask.getResource().getRackAffinity().contains(rackId)) {
				log.trace("Task {} requires a rack in {} (current rack {})", currentTask.getScheduleTask().getId(),
						currentTask.getResource().getRackAffinity(), rackId);
				return MachineState.RACK_NOT_MATCH;
			}
		}

		// TODO 分散在各个不同机器

		return MachineState.UP;
	}

	public void updateTaskState(long id, PaasTaskState status) {
		taskManager.updateScheduleTaskStatus(id, status);
	}
	
	private DeployStatistics getDeployStatistics(long deployId) {
		Optional<DeployStatistics> deployStatistics = deployManager.getDeployStatistics(deployId);

		if (deployStatistics.isPresent()) {
			return deployStatistics.get();
		}

		return new DeployStatistics(deployId);
	}

	public void handleCompletedTask(Optional<ScheduleTask> maybeActiveTask, PaasTaskId taskId, PaasTaskState state) {

		final long failTime = System.currentTimeMillis();

		DeployStatistics deployStatistics = getDeployStatistics(taskId.getDeployId());

		Optional<DeployState> deployStatus = deployManager.getDeployStatus(taskId.getDeployId());

		if (deployStatus.isPresent() && !deployStatus.get().isRunnable()) {
			log.debug("Task {} completed, but it didn't match active deploy state - ignoring", taskId.getTaskId());
			return;
		}

		if (state.isSuccess()) {			
			if(deployStatus.get() == DeployState.COOLDOWN){
				deployManager.active(taskId.getDeployId());
			}
			
			deployStatistics.setFailSeqCount(0);
			deployStatistics.setSuccessSeqCount(deployStatistics.getSuccessSeqCount() + 1);
			deployStatistics.setSuccessCount(deployStatistics.getSuccessCount() + 1);
		}else if (state.isFailed()) {
			
			if(shouldEnterCooldown(taskId, deployStatistics)){
				deployManager.coolDown(taskId.getDeployId());
			}
			
			deployStatistics.setSuccessSeqCount(0);
			deployStatistics.setFailSeqCount(deployStatistics.getFailSeqCount() + 1);
			deployStatistics.setFailCount(deployStatistics.getFailCount() + 1);

		} 
		
		deployStatistics.setLastFinishAt(new Date(failTime));
		deployStatistics.setLastTaskStateAt(state);

		log.trace("Saving new deploy statistics {}", deployStatistics);
		deployManager.saveDeployStatistics(deployStatistics);
				
		
		Optional<Deploy> pendingDeploy = deployManager.getDeploy(taskId.getDeployId());
		
		
		if(pendingDeploy.isPresent()){
			if(pendingDeploy.get().isKeepAlive()){
				List<ScheduleTask> matchList = taskManager.getAllActiveScheduleTaskByDeployId(taskId.getDeployId());
				scheduleTasks(pendingDeploy.get(), matchList);
			}
		}else{
			log.trace("appId {} deploy {} is not present.",taskId.getAppId(),taskId.getDeployId());
		}
		
	}

	private boolean shouldEnterCooldown(PaasTaskId taskId, DeployStatistics deployStatistics) {

		if (configuration.getCooldownAfterFailures() < 1) {
			return false;
		}

		final int numSequentialFailures = deployStatistics.getFailSeqCount() + 1;
		final boolean failedTooManyTimes = numSequentialFailures >= configuration.getCooldownAfterFailures();

		if (failedTooManyTimes) {
			log.trace("Deploy {} failed {} times, which is over the cooldown threshold of {}", taskId.getDeployId(),numSequentialFailures, configuration.getCooldownAfterFailures());
		}

		if (deployStatistics.getLastFinishAt() == null) {
			log.trace("Can't factor finish time into cooldown state for deploy {} because there wasn't a previous task finish time",taskId.getDeployId());
			return failedTooManyTimes;
		}

		if (cooldown.hasCooldownExpired(deployStatistics)) {
			return false;
		}

		return failedTooManyTimes;
	}

}
