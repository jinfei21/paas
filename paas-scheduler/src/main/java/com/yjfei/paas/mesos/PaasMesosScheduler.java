package com.yjfei.paas.mesos;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.mesos.Protos;
import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.OfferID;
import org.apache.mesos.Protos.SlaveID;
import org.apache.mesos.Protos.TaskState;
import org.apache.mesos.Protos.TaskStatus;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.common.PaasTaskState;
import com.yjfei.paas.configuration.PaasConfiguration;
import com.yjfei.paas.data.DeployTaskRequest;
import com.yjfei.paas.data.OfferHolder;
import com.yjfei.paas.data.PaasTaskId;
import com.yjfei.paas.data.ScheduleTask;
import com.yjfei.paas.manage.TaskManager;
import com.yjfei.paas.poller.HealthPoller;
import com.yjfei.paas.poller.SLBPoller;
import com.yjfei.paas.util.MesosUtil;
import com.yjfei.paas.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaasMesosScheduler implements Scheduler {

	@Autowired
	private PaasConfiguration configuration;

	@Autowired
	private SchedulerService scheduler;	

	@Autowired
	private TaskManager taskManager;
	
	@Autowired
	private PaasMesosTaskBuilder mesosTaskBuilder;
	
	private AtomicReference<SchedulerDriver> schedulerDriverHolder = new AtomicReference<>(null);

	@Autowired
	private HealthPoller healthChecker;
	
	@Autowired
	private SLBPoller slbChecker;


	@Override
	public void registered(SchedulerDriver driver, FrameworkID frameworkId, MasterInfo masterInfo) {
		log.info("Registered driver {}, with frameworkId {} and master {}", driver, frameworkId, masterInfo);
		schedulerDriverHolder.set(driver);
	}

	@Override
	public void reregistered(SchedulerDriver driver, MasterInfo masterInfo) {
		log.info("Reregistered driver {}, with master {}", driver, masterInfo);
		schedulerDriverHolder.set(driver);
	}

	@Override
	public void resourceOffers(SchedulerDriver driver, List<Offer> offers) {
		log.info("Received {} offer(s)", offers.size());

		final long start = System.currentTimeMillis();

		final Set<Protos.OfferID> acceptedOffers = Sets.newHashSetWithExpectedSize(offers.size());

		int numDueTasks = 0;

		try {
			
			scheduler.checkForDecomissions();
			
			scheduler.drainPendingDeploy();

			final List<DeployTaskRequest> dueTasks = scheduler.getDueTaskRequest();

			for (DeployTaskRequest dueTask : dueTasks) {
				log.trace("AppId {} Task {} is due", dueTask.getScheduleTask().getAppId(), dueTask.getScheduleTask().getId());
			}

			numDueTasks = dueTasks.size();

			final List<OfferHolder> offerHolders = Lists.newArrayListWithCapacity(offers.size());

			for (Protos.Offer offer : offers) {
				offerHolders.add(new OfferHolder(offer, numDueTasks));
			}

			boolean addedTaskInLastLoop = true;

			while (!dueTasks.isEmpty() && addedTaskInLastLoop) {
				addedTaskInLastLoop = false;
				Collections.shuffle(offerHolders);

				for (OfferHolder offerHolder : offerHolders) {
					Optional<DeployTaskRequest> accepted = match(dueTasks, offerHolder);
					if (accepted.isPresent()) {
						offerHolder.addMatchedTask(accepted.get());
						addedTaskInLastLoop = true;
						dueTasks.remove(accepted.get());
					}

					if (dueTasks.isEmpty()) {
						break;
					}
				}
			}

			for (OfferHolder offerHolder : offerHolders) {
				if (!offerHolder.getAcceptedTasks().isEmpty()) {
					offerHolder.launchTasks(driver);
					acceptedOffers.add(offerHolder.getOffer().getId());
				} else {
					driver.declineOffer(offerHolder.getOffer().getId());
				}
			}

		} catch (Throwable t) {
			log.error("Received fatal error while accepting offers - will decline all available offers", t);

			for (Protos.Offer offer : offers) {
				if (acceptedOffers.contains(offer.getId())) {
					continue;
				}

				driver.declineOffer(offer.getId());
			}

			throw t;
		}

		log.info("Finished handling {} offer(s) ({}), {} accepted, {} declined, {} outstanding tasks", offers.size(),
				TimeUtil.duration(start), acceptedOffers.size(), offers.size() - acceptedOffers.size(),
				numDueTasks - acceptedOffers.size());

	}

	private Optional<DeployTaskRequest> match(List<DeployTaskRequest> dueTasks, OfferHolder offerHolder) {

		for (DeployTaskRequest task : dueTasks) {
			log.trace("Attempting to match task {} resources {} with remaining offer resources {}",
					task.getScheduleTask().getId(), task.getResource(), offerHolder.getCurrentResources());
			
			final boolean matchesResources = MesosUtil.doesOfferMatchResources(task.getResource(), offerHolder.getCurrentResources(),Lists.newArrayList());
			
			final MachineState checkState = scheduler.checkOffer(dueTasks,task,offerHolder.getOffer());
			
			if(matchesResources&&checkState.isAppropriate()){
				DeployTaskRequest paasTask = mesosTaskBuilder.buildTask(offerHolder.getOffer(), offerHolder.getCurrentResources(), task);
		        log.info("Launching task {} slot on slave {} ({})", task.getScheduleTask().getId(), offerHolder.getOffer().getSlaveId().getValue(), offerHolder.getOffer().getHostname());
				
		        paasTask.getScheduleTask().setStatus(PaasTaskState.TASK_STAGING);
		        if(paasTask.getFirstPort().isPresent()){
		        	paasTask.getScheduleTask().setPort(paasTask.getFirstPort().get());
		        }
		        taskManager.updateScheduleTask(paasTask.getScheduleTask());
		        return Optional.of(paasTask);
			}else{			
				log.trace("Ignoring offer {} for task {}; matched resources: {}, machine state: {}", offerHolder.getOffer().getId(), task.getScheduleTask().getId(), matchesResources, checkState);				   
			}
		}

		return Optional.absent();
	}

	@Override
	public void offerRescinded(SchedulerDriver driver, OfferID offerId) {
		log.info("Offer {} rescinded", offerId);
	}

	@Override
	public void statusUpdate(SchedulerDriver driver, TaskStatus status) {
		final String taskId = status.getTaskId().getValue();

		log.debug("Task {} is now {} ({})", taskId, status.getState(), status.getMessage());
			
		PaasTaskId taskIdObj = PaasTaskId.fromString(taskId);
		PaasTaskState taskState = PaasTaskState.fromTaskState(status.getState());
				
	    Optional<ScheduleTask> maybeActiveTask =  taskManager.getScheduleTaskById(taskIdObj.getTaskId());
	    	    
	    if (maybeActiveTask.isPresent() && status.getState() == TaskState.TASK_RUNNING) {
	    	ScheduleTask scheduleTask = maybeActiveTask.get();
	    	scheduleTask.setStatus(taskState);
	    	taskManager.updateScheduleTask(scheduleTask);
	    	healthChecker.enqueue(scheduleTask);
	     }
	    
	    if(taskState.isDone()){
	    	ScheduleTask scheduleTask = maybeActiveTask.get();
	    	scheduleTask.setStatus(taskState);
	    	taskManager.updateScheduleTask(scheduleTask);
	    	scheduler.handleCompletedTask(maybeActiveTask, taskIdObj, taskState);
	    }
	}

	@Override
	public void frameworkMessage(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId, byte[] data) {
		log.info("Framework message from executor {} on slave {} with {} bytes of data", executorId, slaveId,data.length);
	}

	@Override
	public void disconnected(SchedulerDriver driver) {
		schedulerDriverHolder.set(null);
	}

	@Override
	public void slaveLost(SchedulerDriver driver, SlaveID slaveId) {
		log.warn("Lost a slave {}", slaveId);
	}

	@Override
	public void executorLost(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId, int status) {
		log.warn("Lost an executor {} on slave {} with status {}", executorId, slaveId, status);
	}

	@Override
	public void error(SchedulerDriver driver, String message) {
		log.warn("Error from mesos: {}", message);
	}

	public boolean isConnected() {
		return schedulerDriverHolder.get() != null;
	}

}
