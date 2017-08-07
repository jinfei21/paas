package com.yjfei.paas.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.data.Rack;
import com.yjfei.paas.service.RackService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
@Transactional
@Rollback
public class RackServiceTest {

	@Autowired
	private RackService rackService;
	
	@Test
	public void testListSlave(){
		
		List<Rack> rackList = rackService.listRack(MachineState.SLAVE_DECOMISSION);
		
		System.out.println("end");
	}
}
