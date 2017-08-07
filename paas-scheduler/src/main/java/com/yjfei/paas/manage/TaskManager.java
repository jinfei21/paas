package com.yjfei.paas.manage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.yjfei.paas.common.PaasTaskState;
import com.yjfei.paas.data.CleanupTask;
import com.yjfei.paas.data.ScheduleTask;
import com.yjfei.paas.data.Slave;
import com.yjfei.paas.service.CleanupTaskService;
import com.yjfei.paas.service.ScheduleTaskService;

@Component
public class TaskManager {

	@Autowired
	private ScheduleTaskService scheduleTaskService;

	@Autowired
	private CleanupTaskService cleanupTaskService;

	public List<ScheduleTask> getAllActiveTask() {
		return scheduleTaskService.listScheduleTask(PaasTaskState.TASK_LAUNCHED,PaasTaskState.TASK_STAGING,PaasTaskState.TASK_RUNNING,PaasTaskState.TASK_STARTING);		
	}
	
	public List<ScheduleTask> getAllLaunchTask(){
		return scheduleTaskService.listScheduleTask(PaasTaskState.TASK_LAUNCHED);
	}

	public List<ScheduleTask> getTasksOnSlave(List<ScheduleTask> activeTaskList, Slave slave) {
		List<ScheduleTask> taskList = Lists.newArrayList();

		for (ScheduleTask activeTask : activeTaskList) {
			if (activeTask.getHost().equalsIgnoreCase(slave.getHost())
			 && activeTask.getSlaveId().equalsIgnoreCase(slave.getSlaveId())) {

				taskList.add(activeTask);
			}
		}

		return taskList;
	}

	public void createCleanupTask(CleanupTask cleanupTask) {
		cleanupTaskService.addCleanupTask(cleanupTask);
	}

	public void createScheduleTask(ScheduleTask scheduleTask) {
		scheduleTaskService.createScheduleTask(scheduleTask);
	}
	
	public void createScheduleTask(List<ScheduleTask> scheduleTasks){
		scheduleTaskService.createScheduleTasks(scheduleTasks);
	}

	public void updateScheduleTaskStatus(long id, PaasTaskState status) {
		scheduleTaskService.updateScheduleTaskStatus(id, status);
	}
	
	public List<ScheduleTask> getScheduleTaskByDeployId(long deployId){
		return scheduleTaskService.listScheduleTaskByDeployId(deployId);
	}
	
	public List<ScheduleTask> getScheduleTaskkByDeployId(long deployId,PaasTaskState ...status){
		return scheduleTaskService.listScheduleTaskByDeployId(deployId, status);
	}
	
	public List<ScheduleTask> getAllActiveScheduleTaskByDeployId(long deployId){
		return scheduleTaskService.listScheduleTaskByDeployId(deployId, PaasTaskState.TASK_LAUNCHED,PaasTaskState.TASK_STAGING,PaasTaskState.TASK_RUNNING,PaasTaskState.TASK_STARTING);		
	}
	
	public Optional<ScheduleTask> getScheduleTaskById(long id){
		return scheduleTaskService.findScheduleTaskById(id);
	}
	
	public void updateScheduleTask(ScheduleTask scheduleTask){
		scheduleTaskService.updateScheduleTask(scheduleTask);
	}
}
