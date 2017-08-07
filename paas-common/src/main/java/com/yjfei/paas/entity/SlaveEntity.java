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
@Table(name = "slave")
public class SlaveEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    
    @Basic
    @Column(name = "slaveid")
	private String slaveId;
    
    @Basic
    @Column(name = "rackid")
	private String rackId;
    
    @Basic
    @Column(name = "host")
	private String host;
    
    @Basic
    @Column(name = "status")    
    private int status;
    
    @Basic
    @Column(name = "create_time")
	private Date createTime;
    
    @Basic
    @Column(name = "modify_time")
    private Date modifyTime; 
}
