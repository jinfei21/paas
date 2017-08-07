package com.yjfei.paas.common;

public enum MachineState {

	UP(1,"machine up",true),
	SLAVE_DECOMISSION(2,"slave decomission",false),
	RACK_DECOMISSION(3,"rack decomission",false),
	RACK_NOT_MATCH(2,"rack not match",false),;
	
	private int code;
	private  boolean isAppropriate;
	private String text;
	
	MachineState(int code,String text,boolean isAppropriate){
		this.code = code;
		this.text = text;
		this.isAppropriate = isAppropriate;
	}
	
	public int getCode(){
		return this.code;
	}
	
	public boolean isAppropriate(){
		return this.isAppropriate;
	}
	
	public static String getNameByCode(int code){

		for(MachineState type:values()){
			if(type.code == code){
				return type.text;
			}
		}
		return  "unknow";
	}
	
	public static int getCodeByName(String name){

		for(MachineState type:values()){
			if(type.text.equalsIgnoreCase(name)){
				return type.code;
			}
		}
		return 0;
	}
	
	public static MachineState getStateByCode(int code){

		for(MachineState type:values()){
			if(type.code == code){
				return type;
			}
		}
		return UP;
	}

}
