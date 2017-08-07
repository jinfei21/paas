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
@Table(name = "deploy_task")
public class DeployTaskEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    
    @Basic
    @Column(name = "app_id")
	private String appId;
    
    @Basic
    @Column(name = "deploy_id")
	private long deployId;
    
    @Basic
    @Column(name = "host")    
    private String host;
    
    @Basic
    @Column(name = "port")   
    private long port;
    
    @Basic
    @Column(name = "slave_id")   
    private String slaveId;
    
    @Basic
    @Column(name = "instance_no")   
    private int instanceNo;
    
    @Basic
    @Column(name = "status")   
    private int status;
    
    @Basic
    @Column(name = "schedule_time")   
    private Date scheduleTime;
    
    @Basic
    @Column(name = "modify_time")   
    private Date modifyTime;
    
    
}
