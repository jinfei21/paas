package com.yjfei.paas.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.common.CleanupType;
import com.yjfei.paas.dao.CleanupTaskRepository;
import com.yjfei.paas.entity.CleanupTaskEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
public class CleanupTaskRepositoryTest {
	
	@Autowired
	private CleanupTaskRepository cleanupTaskRepository;

	
	@Test
	public void testAddCleanupTask()  throws Exception {
		CleanupTaskEntity cleanup = new CleanupTaskEntity();

		cleanup.setAppId("1111101");
		cleanup.setCreateTime(new Date());
		cleanup.setModifyTime(new Date());
		cleanup.setType(CleanupType.DECOMISSION.getCode());
		cleanup.setDeployId(111);
		
		cleanupTaskRepository.save(cleanup);
		System.out.println("fsaf");
	}
	
	@Test
	public void testUpdateCleanupTask() throws Exception {
		
		CleanupTaskEntity cleanup = new CleanupTaskEntity();
		cleanup.setId(2);
		cleanup.setAppId("1111101");
		cleanup.setCreateTime(new Date());
		cleanup.setModifyTime(new Date());
		cleanup.setType(CleanupType.DECOMISSION.getCode());
		cleanup.setDeployId(111);
		
		cleanupTaskRepository.saveAndFlush(cleanup);
	}

	@Test
	public void testFindByType() throws Exception {
		List<CleanupTaskEntity> cleanupList = cleanupTaskRepository.findByType(CleanupType.DECOMISSION.getCode());
		
		System.out.println("fdsafas");
				
	}

}
