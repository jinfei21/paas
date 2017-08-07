package com.yjfei.paas.data;

import lombok.Data;

@Data
public class Deploy {

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
	private String quartz;
	

}
