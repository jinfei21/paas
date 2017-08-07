package com.yjfei.paas.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Cacheable(false)
@Entity
@Table(name = "deploy")
public class DeployEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
    @Basic
    @Column(name = "app_id")
	private String appId;
	
    @Basic
    @Column(name = "package_id")
	private String packageId;
	
    @Basic
    @Column(name = "package_uri")
	private String packageUri;
	
    @Basic
    @Column(name = "instance_count")
	private int instances;
	
    @Basic
    @Column(name = "tag")
	private String tag;
	
    @Basic
    @Column(name = "health_path")
	private String healthPath;
	
    @Basic
    @Column(name = "slb_path")
	private String slbPath;
	
    @Basic
    @Column(name = "cmd")
	private String cmd;
    
    @Basic
    @Column(name = "quartz")
	private String quartz;
	
    @Basic
    @Column(name = "keep_alive")
	private boolean keepAlive;
    
    @Basic
    @Column(name = "create_time")
	private Date createTime;
	
    @Basic
    @Column(name = "modify_time")
	private Date modifyTime;
}
