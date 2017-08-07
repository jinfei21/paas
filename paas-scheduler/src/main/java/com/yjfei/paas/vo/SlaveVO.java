package com.yjfei.paas.vo;

import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.entity.RackEntity;
import com.yjfei.paas.entity.SlaveEntity;
import com.yjfei.paas.util.TimeUtil;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by zhongyi on 2017/8/2.
 */
@Data
public class SlaveVO {

    private long id;
    private String slaveId;
    private String rackId;
    private String status;
    private String host;
    private String createTime;
    private String modifyTime;

    public SlaveVO() {
    }

    public SlaveVO(SlaveEntity slaveEntity) {
        BeanUtils.copyProperties(slaveEntity, this);

        this.createTime = TimeUtil.convertDateToString(slaveEntity.getCreateTime());
        this.modifyTime = TimeUtil.convertDateToString(slaveEntity.getModifyTime());

        this.status = MachineState.getNameByCode(slaveEntity.getStatus());
    }

    public SlaveEntity buildSlaveEntity() {
        SlaveEntity slaveEntity = new SlaveEntity();
        BeanUtils.copyProperties(this, slaveEntity);
        if (this.createTime != null) {
            slaveEntity.setCreateTime(TimeUtil.convertStringToDate(this.createTime));
        }
        slaveEntity.setStatus(MachineState.getCodeByName(this.status));
        return slaveEntity;
    }
}
