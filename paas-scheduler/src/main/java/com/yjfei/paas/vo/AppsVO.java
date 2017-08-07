package com.yjfei.paas.vo;

import com.yjfei.paas.common.AppType;
import com.yjfei.paas.entity.AppEntity;
import com.yjfei.paas.entity.AppsEntity;
import com.yjfei.paas.util.TimeUtil;

import lombok.Data;

@Data
public class AppsVO {

	private long id;
	private String appId;
	private String appName;
	private String owner;
	private String depart;
	private String createTime;
	private String modifyTime;
	private String email;

	public AppsVO() {

	}

	public AppsVO(AppsEntity app) {
		this.id = app.getId();
		this.appId = app.getAppId();
		this.appName = app.getName();
		this.owner = app.getOwner();
		this.depart = app.getDepart();
		this.createTime = TimeUtil.convertDateToString(app.getCreateTime());
		this.modifyTime = TimeUtil.convertDateToString(app.getModifyTime());
		this.email = app.getEmail();
	}
	
	public AppsEntity buildAppEntity(){
		AppsEntity app = new AppsEntity();
		app.setId(id);
		app.setAppId(appId);
		app.setDepart(depart);
		app.setOwner(owner);
		app.setName(appName);
		app.setEmail(email);
		return app;
	}

}