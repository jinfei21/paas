package com.yjfei.paas.common;

public enum DeployState {

	ACTIVE(0, "active", true), 
	SCHEDULE(1,"schedule",true),
	DELETED(2, "delete", false), 
	PAUSED(3, "paused", false), 
	COOLDOWN(4, "cooldown",true), 
	FINISHED(5, "finished", false);

	private int code;
	private String text;
	private final boolean isRunnable;

	DeployState(int code, String text, boolean isRunnable) {
		this.code = code;
		this.text = text;
		this.isRunnable = isRunnable;
	}

	public boolean isRunnable() {
		return isRunnable;
	}
	
	public static String getNameByCode(int code){

		for(DeployState type:values()){
			if(type.code == code){
				return type.text;
			}
		}
		return  "unknow";
	}
	
	public static int getCodeByName(String name){

		for(DeployState type:values()){
			if(type.text.equalsIgnoreCase(name)){
				return type.code;
			}
		}
		return 0;
	}
	
	public static DeployState getStateByCode(int code){

		for(DeployState type:values()){
			if(type.code == code){
				return type;
			}
		}
		return DELETED;
	}
	
	public int getCode(){
		return this.code;
	}

}
