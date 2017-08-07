package com.yjfei.paas.common;

public enum AppType {

	WORKER(0, "worker",true), 
	SERVICE(1, "service",true), 
	JOB(2, "job",true),
	ONEOFF(3,"oneoff",true);


	private int code;
	private String text;
	private boolean longRunning;

	AppType(int code, String text,boolean longRunning) {
		this.code = code;
		this.text = text;
		this.longRunning = longRunning;
	}

	public boolean isLongRunning(){
		return this.longRunning;
	}
	
	public static String getNameByCode(int code){

		for(AppType type:values()){
			if(type.code == code){
				return type.text;
			}
		}
		return  "unknow";
	}
	
	public static int getCodeByName(String name){

		for(AppType type:values()){
			if(type.text.equalsIgnoreCase(name)){
				return type.code;
			}
		}
		return 0;
	}
	
	public static AppType getAppTypeByCode(int code){
		for(AppType type:values()){
			if(type.code == code){
				return type;
			}
		}
		return SERVICE;
	}
}
