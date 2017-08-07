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
@Table(name = "app")
public class AppEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
    @Basic
    @Column(name = "app_id")
	private String appId;
	
    @Basic
    @Column(name = "app_name")
	private String appName;
	
    @Basic
    @Column(name = "type")
	private int type;
	
    @Basic
    @Column(name = "cpu")
	private double cpu;
	
    @Basic
    @Column(name = "memory")
	private int memory;
	
    @Basic
    @Column(name = "ports")
	private int ports;
	
    @Basic
    @Column(name = "disk")
	private int disk;
	
    @Basic
    @Column(name = "owner")
	private String owner;
	
    @Basic
    @Column(name = "depart")
	private String depart;
    
    @Basic
    @Column(name = "app_desc")
	private String desc;
    
    @Basic
    @Column(name = "affinity")
	private String affinity;
	
    @Basic
    @Column(name = "create_time")
	private Date createTime;
    
    @Basic
    @Column(name = "modify_time")
    private Date modifyTime; 
    

}
