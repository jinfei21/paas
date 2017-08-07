package com.yjfei.paas.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.common.PaasTaskState;
import com.yjfei.paas.data.ScheduleTask;
import com.yjfei.paas.service.ScheduleTaskService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
@Transactional
public class ScheduleTaskServiceTest {

	
	@Autowired
	private ScheduleTaskService  scheduleTaskService;
	
	
	public void testListScheduleTask(){
		
		List<ScheduleTask> taskList = this.scheduleTaskService.listScheduleTask(PaasTaskState.TASK_STARTING);
		
		
		System.out.println("end");
	}
	
	@Test
	public void testUpdateDeployTaskStatus() {

		scheduleTaskService.updateScheduleTaskStatus(1, PaasTaskState.TASK_LOST_WHILE_DOWN);

		System.out.println("end");
	}
}
