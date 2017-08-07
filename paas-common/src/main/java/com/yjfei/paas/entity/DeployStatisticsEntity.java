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
@Table(name = "deploy_statistics")
public class DeployStatisticsEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    
    @Basic
    @Column(name = "deploy_id")
	private long deployId;
    
    @Basic
    @Column(name = "fail_count")
	private int failCount;
    
    @Basic
    @Column(name = "fail_seqcount")
	private int failSeqCount;
    
    @Basic
    @Column(name = "success_count")
	private int successCount;
    
    @Basic
    @Column(name = "success_seqcount")
	private int successSeqCount;
    
    @Basic
    @Column(name = "last_task_state")
	private int lastFinishState;
    
    @Basic
    @Column(name = "last_finish_time")
    private Date lastFinishAt; 
}
