package com.yjfei.paas.manage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.data.Rack;
import com.yjfei.paas.data.Slave;
import com.yjfei.paas.service.SlaveService;

@Component
public class SlaveManager {

	@Autowired
	private SlaveService slaveService;

	public List<Slave> getDecomissionSlaves() {
		return slaveService.listSlave(MachineState.SLAVE_DECOMISSION);
	}

	public List<Slave> getSlaveByRack(List<Rack> racks) {
		List<Slave> slaveList = Lists.newArrayList();

		for (Rack rack : racks) {
			slaveList.addAll(this.slaveService.listSlave(rack));
		}
		
		return slaveList;
	}
	
	public boolean isDecomission(String slaveId) {
		return slaveService.isDecomission(slaveId);
	}

}
