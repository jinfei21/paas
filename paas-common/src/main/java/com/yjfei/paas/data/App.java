package com.yjfei.paas.data;

import java.util.Date;

import com.yjfei.paas.common.AppType;

import lombok.Data;

@Data
public class App {

	private long id;

	private String appId;

	private String appName;

	private AppType type;

	private double cpu;

	private int memory;

	private int ports;

	private int disk;

	private String affinity;

	private Date createTime;

	private Date modifyTime;
}
