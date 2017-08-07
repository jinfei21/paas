package com.yjfei.paas.mesos;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos;
import org.apache.mesos.Protos.Credential;
import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.SlaveID;
import org.apache.mesos.Protos.TaskID;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.yjfei.paas.configuration.PaasConfiguration;
import com.yjfei.paas.util.IPUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaasMesosDriver {

	private Protos.FrameworkInfo frameworkInfo;

	@Autowired
	private PaasMesosSchedulerDelegator scheduler;

	private SchedulerDriver driver;

	@Autowired
	private PaasConfiguration configuration;

	@PostConstruct
	public void init() throws IOException {

		final FrameworkInfo.Builder frameworkInfoBuilder = Protos.FrameworkInfo.newBuilder()
				.setCheckpoint(configuration.getMesosConfiguration().isCheckpoint())
				.setFailoverTimeout(configuration.getMesosConfiguration().getFrameworkFailoverTimeout())
				.setName(configuration.getMesosConfiguration().getFrameworkName())
				.setId(FrameworkID.newBuilder().setValue(configuration.getMesosConfiguration().getFrameworkId()))
				.setUser("");// let mesos assign

		frameworkInfoBuilder.setHostname(IPUtil.getLocalHostName());

		// only set the web UI URL if it's fully qualified
		frameworkInfoBuilder.setWebuiUrl(configuration.getUiConfiguration().getBaseUrl());

		if (configuration.getMesosConfiguration().getFrameworkRole() != null) {
			frameworkInfoBuilder.setRole(configuration.getMesosConfiguration().getFrameworkRole());
		}

		this.frameworkInfo = frameworkInfoBuilder.build();

		if (configuration.getMesosConfiguration().getCredentialPrincipal().isPresent()
				&& configuration.getMesosConfiguration().getCredentialSecret().isPresent()) {
			Credential credential = Credential.newBuilder()
					.setPrincipal(configuration.getMesosConfiguration().getCredentialPrincipal().get())
					.setSecret(configuration.getMesosConfiguration().getCredentialSecret().get()).build();
			this.driver = new MesosSchedulerDriver(scheduler, frameworkInfo,
					configuration.getMesosConfiguration().getMaster(), true, credential);
		} else {
			this.driver = new MesosSchedulerDriver(scheduler, frameworkInfo,
					configuration.getMesosConfiguration().getMaster(), true);
		}
	}

	@VisibleForTesting
	public Scheduler getScheduler() {
		return scheduler;
	}

	public Optional<MasterInfo> getMaster() {
		return scheduler.getMaster();
	}

	public long getLastOfferTimestamp() {
		return scheduler.getLastOfferTimestamp();
	}

	public Protos.Status start() {
		log.info("Calling driver.start() ...");

		Protos.Status status = driver.start();

		log.info("Started with status: {}", status);

		return status;
	}

	public Protos.Status kill(String taskId) {
		Protos.Status status = driver.killTask(TaskID.newBuilder().setValue(taskId.toString()).build());

		log.info("Killed task {} with driver status: {}", taskId, status);

		return status;
	}

	public Protos.Status sendFrameworkMessage(String taskId, ExecutorID executorID, SlaveID slaveID, byte[] bytes) {
		Protos.Status status = driver.sendFrameworkMessage(executorID, slaveID, bytes);
		log.info("Sent framework message for task {} with driver status: {}", taskId, status);
		return status;
	}

	public Protos.Status abort() {
		log.info("Notifying scheduler about impending driver abort");

		scheduler.notifyStopping();

		log.info("Calling driver.abort() ...");

		Protos.Status status = driver.abort();

		log.info("Aborted with status: {}", status);

		return status;
	}

}
