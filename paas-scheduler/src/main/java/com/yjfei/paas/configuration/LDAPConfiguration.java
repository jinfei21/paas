package com.yjfei.paas.configuration;

import java.util.List;

import lombok.Data;

@Data
public class LDAPConfiguration {

	private String server;
	
	private List<String> paths;
}
