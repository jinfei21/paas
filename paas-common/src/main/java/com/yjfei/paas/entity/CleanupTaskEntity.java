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
@Table(name = "cleanup_task")
public class CleanupTaskEntity {

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
    @Column(name = "type")    
    private int type;
    
    @Basic
    @Column(name = "cmd")
    private String cmd;
    
    @Basic
    @Column(name = "create_time")
	private Date createTime;
    
    @Basic
    @Column(name = "modify_time")
    private Date modifyTime; 
    
}
