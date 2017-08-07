package com.yjfei.paas.data;

import com.yjfei.paas.common.MachineState;

import lombok.Data;

@Data
public class Rack {

	private long id;
	private String rackid;
    private MachineState status;
}
