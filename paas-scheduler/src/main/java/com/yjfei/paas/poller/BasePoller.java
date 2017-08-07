package com.yjfei.paas.poller;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicPropertyFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BasePoller {

	private Thread worker;
	private volatile boolean running = false;
	private DynamicBooleanProperty enable;
	private DynamicLongProperty interval;

	public BasePoller(String name) {
		this.enable = DynamicPropertyFactory.getInstance().getBooleanProperty("paas." + name.toLowerCase() + ".enable",
				true);
		this.interval = DynamicPropertyFactory.getInstance().getLongProperty("paas." + name.toLowerCase() + ".interval",
				1000);
		this.worker = new Thread(new Runnable() {

			@Override
			public void run() {
				while (running) {
					try {
						if (!enable.get()) continue;

						process();

					} catch (Throwable t) {
						log.error(name + "process fail.", t);
					} finally {
						try {
							Thread.sleep(interval.get());
						} catch (InterruptedException e) {
							log.error(name + " sleep error!", e);
						}
					}

				}
			}
		}, name);
	}

	public void start() {
		if (!running) {
			this.running = true;
			this.worker.start();
		}
	}

	public void stop() {
		this.running = false;
	}

	public abstract void process();
}
