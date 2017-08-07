package com.yjfei.paas.data;

import java.util.Date;

import com.yjfei.paas.common.PaasTaskState;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeployStatistics {

	private long id;   
	private long deployId;    
	private int failCount;  
	private int failSeqCount;    
	private int successSeqCount;  
	private int successCount;    
	private PaasTaskState lastTaskStateAt;    
    private Date lastFinishAt; 
    
    public DeployStatistics(long deployId){
    	this.deployId = deployId;
    }
}
