package com.yjfei.paas.mesos;

import java.util.concurrent.locks.ReentrantLock;

import com.yjfei.paas.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaasLock {

	private final ReentrantLock lock;

	public PaasLock() {
		this.lock = new ReentrantLock();
	}

	public long lock(String name) {
		final long start = System.currentTimeMillis();
		log.info("{} - Locking", name);
		lock.lock();
		log.info("{} - Acquired ({})", name, TimeUtil.duration(start));
		return System.currentTimeMillis();
	}

	public void unlock(String name, long start) {
		log.info("{} - Unlocking ({})", name, TimeUtil.duration(start));
		lock.unlock();
	}


}