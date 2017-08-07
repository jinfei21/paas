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
import com.yjfei.paas.data.Slave;
import com.yjfei.paas.service.SlaveService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
@Transactional
@Rollback
public class SlaveServiceTest {

	@Autowired
	private SlaveService slaveService;
	
	@Test
	public void testListSlave(){
		
		List<Slave> slaveList = slaveService.listSlave(MachineState.SLAVE_DECOMISSION);
		
		System.out.println("end");
	}
}
