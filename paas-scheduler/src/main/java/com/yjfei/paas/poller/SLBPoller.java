package com.yjfei.paas.poller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Scope("singleton")
public class SLBPoller extends BasePoller{

	public SLBPoller(){
		super("SLBCheckPoller");
	}

	@Override
	public void process() {
		

	}
	
}
