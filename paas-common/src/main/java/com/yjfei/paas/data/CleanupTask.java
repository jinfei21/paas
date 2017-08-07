package com.yjfei.paas.data;

import com.yjfei.paas.common.CleanupType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CleanupTask {

	private long id;
	
	private CleanupType type;

	private long deployId;
	
	private String appId;
	
	private String cmd;
	
	public CleanupTask(String appId,long deployId,CleanupType type,String cmd){
		this.appId = appId;
		this.deployId = deployId;
		this.type = type;
		this.cmd = cmd;
	}
}
