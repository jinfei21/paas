package com.yjfei.paas.dao;

import java.util.Date;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.common.PaasTaskState;
import com.yjfei.paas.dao.DeployTaskRepository;
import com.yjfei.paas.entity.DeployTaskEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
public class DeployTaskRepositoryTest {

	@Autowired
	private DeployTaskRepository deployTaskRepository;

	@Test
	public void testAddDeployTask() throws Exception {
		DeployTaskEntity deployTask = new DeployTaskEntity();

		deployTask.setAppId("111111");
		deployTask.setDeployId(1);
		deployTask.setInstanceNo(3);
		deployTask.setScheduleTime(new Date());
		deployTask.setHost("host");
		
		deployTask.setStatus(PaasTaskState.TASK_RUNNING.getCode());

		deployTaskRepository.save(deployTask);
		System.out.println("end");
	}

	@Test
	public void testUpdateDeployTask() throws Exception {

		DeployTaskEntity deployTask = new DeployTaskEntity();

		deployTask.setId(2L);
		deployTask.setAppId("111111");
		deployTask.setDeployId(1);
		deployTask.setInstanceNo(3);
		deployTask.setScheduleTime(new Date());
		deployTask.setHost("fsafsa");
		deployTask.setStatus(PaasTaskState.TASK_RUNNING.getCode());

		deployTaskRepository.saveAndFlush(deployTask);
	}

	
	public void testFindById() throws Exception {
		DeployTaskEntity deployTask = deployTaskRepository.findOne(1L);
		Assert.assertNotNull(deployTask);
	}

	
	public void testFindDeployTaskByStatus() {
		List<DeployTaskEntity> deployTaskList = deployTaskRepository.findByStatus(PaasTaskState.TASK_RUNNING.getCode());

		System.out.println("end");
	}

	
	public void testFindByStatusIn() {
		List<Integer> status = Lists.newArrayList();
		status.add(PaasTaskState.TASK_RUNNING.getCode());
		status.add(4);
		List<DeployTaskEntity> deployTaskList = deployTaskRepository.findByStatusIn(status);

		System.out.println("end");
	}

	
	public void testFindByStatusNotIn() {
		List<Integer> status = Lists.newArrayList();
		status.add(PaasTaskState.TASK_RUNNING.getCode());
		status.add(4);
		List<DeployTaskEntity> deployTaskList = deployTaskRepository.findByStatusNotIn(status);

		System.out.println("end");
	}

	@Test
	public void testUpdateDeployTaskStatus() {

		deployTaskRepository.updateStatusById(1, 600, new Date());

		System.out.println("end");
	}
	
	@Test
	public void testFindByDeployIdAndStatusIn(){
		
		List<Integer> statusList = Lists.newArrayList();
		statusList.add(2);
		List<DeployTaskEntity> deployTaskList = deployTaskRepository.findByDeployIdAndStatusIn(1, statusList);
		
		System.out.println("end");
	}

}