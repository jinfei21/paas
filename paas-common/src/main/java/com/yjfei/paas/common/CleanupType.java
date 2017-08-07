package com.yjfei.paas.common;

public enum CleanupType {

	DECOMISSION(0, "decomission"), 
	SCALE_DOWN(1, "scaledown"), 
	TASK_CANCEL(2, "task cancel"),
	TASK_RESCHEDULE(3, "task reschedule"),
	DELETE(4, "delete");


	private int code;
	private String text;

	CleanupType(int code, String text) {
		this.code = code;
		this.text = text;
	}

	public int getCode(){
		return this.code;
	}
	
	public static String getNameByCode(int code){

		for(CleanupType type:values()){
			if(type.code == code){
				return type.text;
			}
		}
		return  "unknow";
	}
	
	public static int getCodeByName(String name){

		for(CleanupType type:values()){
			if(type.text.equalsIgnoreCase(name)){
				return type.code;
			}
		}
		return 0;
	}
}
