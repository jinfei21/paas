package com.yjfei.paas.vo;

import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.entity.RackEntity;
import com.yjfei.paas.util.TimeUtil;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by zhongyi on 2017/8/2.
 */
@Data
public class RackVO {

    private long id;
    private String rackId;
    private String status;
    private String createTime;
    private String modifyTime;

    public RackVO() {

    }

    public RackVO(RackEntity rackEntity) {
        BeanUtils.copyProperties(rackEntity, this);

        this.createTime = TimeUtil.convertDateToString(rackEntity.getCreateTime());
        this.modifyTime = TimeUtil.convertDateToString(rackEntity.getModifyTime());

        this.status = MachineState.getNameByCode(rackEntity.getStatus());
    }

    public RackEntity buildRackEntity() {
        RackEntity rackEntity = new RackEntity();
        BeanUtils.copyProperties(this, rackEntity);
        if (this.createTime != null) {
            rackEntity.setCreateTime(TimeUtil.convertStringToDate(this.createTime));
        }
        rackEntity.setStatus(MachineState.getCodeByName(this.status));
        return rackEntity;
    }
}
