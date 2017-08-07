package com.yjfei.paas.data;

import java.util.Date;

import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.TaskInfo;

import com.yjfei.paas.common.PaasTaskState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTask implements Comparable<ScheduleTask> {

	private long id;
	
	private PaasTaskState status;
	
	private String appId;
	
	private long deployId;

    private int instanceNo;
    
    private Date scheduleTime;
    
    private String host;
    
    private long port;
    
    private String slaveId;

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
        	return true;
        }else{
        	ScheduleTask other = (ScheduleTask) obj;
        	if(this.id == other.id){
        		return true;
        	}
        }
    	return false;
    }
    
    @Override
    public  int hashCode(){
    	return (int) id;
    }

	@Override
	public int compareTo(ScheduleTask o) {
		return (int) (this.id - o.id);
	}
    
}
