package com.yjfei.paas.manage;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.mesos.Protos;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.yjfei.paas.mesos.PaasMesosDriver;

@Component
public class PaasDriverManage {

	@Autowired
	private PaasMesosDriver driver;
	
	private Lock driverLock;

	private Protos.Status currentStatus;

	
	@PostConstruct
	public void start() {
		this.driverLock = new ReentrantLock();
		this.currentStatus = Protos.Status.DRIVER_NOT_STARTED;
	}

	@PreDestroy
	public void stop() {
		stopMesos();
	}

	public Protos.Status getCurrentStatus() {
		return currentStatus;
	}

	public Optional<MasterInfo> getMaster() {
		driverLock.lock();

		try {
			return driver.getMaster();
		} finally {
			driverLock.unlock();
		}
	}

	public  long getLastOfferTimestamp() {
		driverLock.lock();

		try {
			return driver.getLastOfferTimestamp();
		} finally {
			driverLock.unlock();
		}
	}

	public Protos.Status startMesos() {
		driverLock.lock();

		try {
			Preconditions.checkState(isStartable());

			currentStatus = driver.start();
		} finally {
			driverLock.unlock();
		}

		return currentStatus;
	}

	private boolean canKillTask() {
		return currentStatus == Status.DRIVER_RUNNING;
	}

	private boolean isStartable() {
		return currentStatus == Status.DRIVER_NOT_STARTED;
	}

	private boolean isStoppable() {
		return currentStatus == Status.DRIVER_RUNNING;
	}

	public Protos.Status stopMesos() {
		driverLock.lock();

		try {
			if (isStoppable()) {
				currentStatus = driver.abort();
			}
		} finally {
			driverLock.unlock();
		}

		return currentStatus;
	}

}
