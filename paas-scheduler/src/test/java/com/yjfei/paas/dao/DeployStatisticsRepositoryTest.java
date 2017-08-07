package com.yjfei.paas.dao;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.dao.DeployStatisticsRepository;
import com.yjfei.paas.entity.DeployStatisticsEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
public class DeployStatisticsRepositoryTest {

	
	
	@Autowired
	private DeployStatisticsRepository deployStatisticsRepository;
	
	
	@Test 
	public void testFindDeployStatisticsByDeployId(){
		
		DeployStatisticsEntity deployStatistics = deployStatisticsRepository.findByDeployId(1);
		
		
		System.out.println("end");
		
	}
	
	@Test
	public void testAddDeployStatistics()  throws Exception {
		DeployStatisticsEntity deployStatistics = new DeployStatisticsEntity();

		deployStatistics.setId(1);
		deployStatistics.setLastFinishState(1);
		deployStatistics.setLastFinishAt(new Date());
		deployStatistics.setDeployId(1);
		deployStatistics.setFailCount(1);
		deployStatistics.setSuccessCount(1);
		deployStatistics.setFailSeqCount(1);
		deployStatisticsRepository.save(deployStatistics);
		System.out.println("end");
	}
	
	@Test
	public void testUpdateDeployStatistics() throws Exception {
		
		DeployStatisticsEntity deployStatistics = new DeployStatisticsEntity();
		deployStatistics.setId(1);
		deployStatistics.setLastFinishState(1);
		deployStatistics.setLastFinishAt(new Date());
		deployStatistics.setDeployId(1);
		deployStatistics.setFailCount(1);
		deployStatistics.setSuccessCount(1);
		deployStatistics.setFailSeqCount(1);
		deployStatisticsRepository.saveAndFlush(deployStatistics);
		System.out.println("end");

	}

}
