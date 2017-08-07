package com.yjfei.paas.vo;

import com.yjfei.paas.entity.DeployTaskEntity;
import com.yjfei.paas.entity.SlaveEntity;
import com.yjfei.paas.util.TimeUtil;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TaskVO {

    private long id;
    private String appId;
    private long deployId;
    private String host;
    private long port;
    private String slaveId;
    private int instanceNo;
    private int status;
    private String scheduleTime;
    private String modifyTime;

    public TaskVO(DeployTaskEntity deployTask) {
        BeanUtils.copyProperties(deployTask, this);
        try {
            this.scheduleTime = TimeUtil.convertDateToString(deployTask.getScheduleTime());
            this.modifyTime = TimeUtil.convertDateToString(deployTask.getModifyTime());
        } catch (Exception e) {
            this.scheduleTime = null;
            this.modifyTime = null;
        }
    }

}
