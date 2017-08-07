package com.yjfei.paas.vo;

import com.yjfei.paas.common.AppType;
import com.yjfei.paas.entity.AppEntity;
import com.yjfei.paas.util.TimeUtil;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class AppVO {

    private long id;
    private String appId;
    private String appName;
    private String type;
    private String desc;
    private String owner;
    private String depart;
    private String createTime;
    private String modifyTime;
    private double cpu;
    private int memory;
    private int ports;
    private int disk;

    public AppVO() {

    }

    public AppVO(AppEntity app) {
        BeanUtils.copyProperties(app, this);

        this.createTime = TimeUtil.convertDateToString(app.getCreateTime());
        this.modifyTime = TimeUtil.convertDateToString(app.getModifyTime());

        this.type = AppType.getNameByCode(app.getType());
    }

    public AppEntity buildAppEntity() {
        AppEntity appEntity = new AppEntity();
        BeanUtils.copyProperties(this, appEntity);
        if (this.createTime != null) {
            appEntity.setCreateTime(TimeUtil.convertStringToDate(this.createTime));
        }
        appEntity.setType(AppType.getCodeByName(this.type));
        return appEntity;
    }

}