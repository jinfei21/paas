package com.yjfei.paas.data;

import com.yjfei.paas.common.MachineState;

import lombok.Data;

@Data
public class Slave {

	private long id;
	private String slaveId;
	private String rackid;
	private String host;
    private MachineState status;    
}
