package com.yjfei.paas.poller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Scope("singleton")
public class ReconcilePoller extends BasePoller {
	
	
	public ReconcilePoller() {
		super("ReconcilePoller");
	}

	@Override
	public void process() {
		

	}
	
}
