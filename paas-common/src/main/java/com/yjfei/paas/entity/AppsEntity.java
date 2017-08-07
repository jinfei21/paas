package com.yjfei.paas.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Cacheable(false)
@Entity
@Table(name = "apps")
public class AppsEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
    @Basic
    @Column(name = "appid")
	private String appId;
	
    @Basic
    @Column(name = "name")
	private String name;
	
    @Basic
    @Column(name = "owner")
	private String owner;
	
    @Basic
    @Column(name = "depart")
	private String depart;
    
    @Basic
    @Column(name = "status")
	private int status;

    @Basic
    @Column(name = "create_time")
	private Date createTime;
    
    @Basic
    @Column(name = "modify_time")
    private Date modifyTime;

    @Basic
    @Column(name = "email")
    private String email;

    @Override
    public String toString() {
        return "AppsEntity{" +
                "id=" + id +
                ", appId='" + appId + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", depart='" + depart + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", email='" + email + '\'' +
                '}';
    }
}