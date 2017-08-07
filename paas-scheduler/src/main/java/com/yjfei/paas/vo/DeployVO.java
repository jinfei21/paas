package com.yjfei.paas.vo;

import com.yjfei.paas.entity.DeployEntity;
import com.yjfei.paas.util.TimeUtil;

import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class DeployVO {

    private long id;
    private String appId;
    private String packageId;
    private String packageUri;
    private int instances;
    private String tag;
    private String healthPath;
    private String slbPath;
    private String cmd;
    private boolean keepAlive;

    private String createTime;
    private String modifyTime;
    private String status;


    public DeployVO() {

    }

    public DeployVO (DeployEntity deploy){
		BeanUtils.copyProperties(deploy, this);
		try{
            this.createTime = TimeUtil.convertDateToString(deploy.getCreateTime());
            this.modifyTime = TimeUtil.convertDateToString(deploy.getModifyTime());
        }catch(Exception e){
		    this.createTime = null;
		    this.modifyTime = null;
        }

	}
//    public DeployVO(DeployEntity deploy) {
//        this.id = deploy.getId();
//        this.appId = deploy.getAppId();
//        this.packageId = deploy.getPackageId();
//        this.packageUri
//	TimeUtil.convertDateToString(
//    }

    public DeployEntity buildDeployEntity() {
        DeployEntity deploy = new DeployEntity();
        BeanUtils.copyProperties(this, deploy);
        return deploy;
    }

    @Override
    public String toString() {
        return "DeployVO{" +
                "id=" + id +
                ", appId='" + appId + '\'' +
                ", packageId='" + packageId + '\'' +
                ", packageUri='" + packageUri + '\'' +
                ", instances=" + instances +
                ", tag='" + tag + '\'' +
                ", healthPath='" + healthPath + '\'' +
                ", slbPath='" + slbPath + '\'' +
                ", cmd='" + cmd + '\'' +
                ", keepAlive=" + keepAlive +
                ", createTime='" + createTime + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
