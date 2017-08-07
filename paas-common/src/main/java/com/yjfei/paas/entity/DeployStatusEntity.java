package com.yjfei.paas.entity;

import java.sql.Date;

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
@Table(name = "deploy_status")
public class DeployStatusEntity {

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
    @Column(name = "status")   
    private int status;
    
    @Basic
    @Column(name = "schedule_time")   
    private Date scheduleTime;
    
    @Basic
    @Column(name = "modify_time")   
    private Date modifyTime;
    
    @Basic
    @Column(name = "sand_box")   
    private String sandbox;
}
