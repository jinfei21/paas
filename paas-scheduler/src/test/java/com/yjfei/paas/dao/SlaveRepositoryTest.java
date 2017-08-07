package com.yjfei.paas.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.dao.SlaveRepository;
import com.yjfei.paas.entity.SlaveEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
public class SlaveRepositoryTest {

	@Autowired
	private SlaveRepository slaveRepository;

	@Test
	public void testAddSlave() throws Exception {

		SlaveEntity slave = new SlaveEntity();
		slave.setRackId("rackid");
		slave.setSlaveId("slaveid");
		slave.setCreateTime(new Date());
		slave.setHost("fasf");
		slave.setModifyTime(new Date());
		slave.setStatus(MachineState.UP.getCode());
		slaveRepository.save(slave);
	}

	@Test
	public void testUpdateSlave() throws Exception {

		SlaveEntity slave = new SlaveEntity();

		slave.setId(3);
		slave.setHost("jinfei");
		slave.setModifyTime(new Date());
		slaveRepository.saveAndFlush(slave);
		
		System.out.println("Fdsfas");
	}

	@Test
	public void testFindByStatus() throws Exception {
		List<SlaveEntity> slaveList = slaveRepository.findByStatus(MachineState.UP.getCode());
		
		System.out.println("end");
	}

}
