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
import com.yjfei.paas.dao.RackRepository;
import com.yjfei.paas.entity.RackEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
public class RackRepositoryTest {

	@Autowired
	private RackRepository rackRepository;

	@Test
	public void testAddRack() throws Exception {

		RackEntity rack = new RackEntity();
		rack.setRackId("rackid");
		rack.setCreateTime(new Date());
		rack.setModifyTime(new Date());
		rack.setStatus(MachineState.UP.getCode());
		rackRepository.save(rack);
	}

	@Test
	public void testUpdateRack() throws Exception {

		RackEntity rack = new RackEntity();

		rack.setId(1);
		rack.setCreateTime(new Date());
		rack.setModifyTime(new Date());
		rack.setStatus(MachineState.UP.getCode());
		rackRepository.save(rack);
	}

	@Test
	public void testFindByStatus() throws Exception {
		List<RackEntity> rackList = rackRepository.findByStatus(MachineState.UP.getCode());
		
		System.out.println("end");
	}

}
