package com.yjfei.paas.manage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.data.Rack;
import com.yjfei.paas.service.RackService;

@Component
public class RackManager {

	@Autowired
	private RackService rackService;


	public List<Rack> getDecomissionRacks() {
		return rackService.listRack(MachineState.RACK_DECOMISSION);
	}

	public boolean isDecomission(String rackId) {
		return rackService.isDecomission(rackId);
	}
}
