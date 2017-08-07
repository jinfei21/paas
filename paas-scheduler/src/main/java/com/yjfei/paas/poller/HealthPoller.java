package com.yjfei.paas.poller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yjfei.paas.data.ScheduleTask;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Scope("singleton")
public class HealthPoller extends BasePoller {


	public HealthPoller() {
		super("HealthCheckPoller");
	}

	@Override
	public void process() {


	}

	public void enqueue(ScheduleTask task){
		
		
		
	}
}
