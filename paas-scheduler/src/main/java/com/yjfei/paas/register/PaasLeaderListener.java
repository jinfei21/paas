package com.yjfei.paas.register;

import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.mesos.Protos;
import org.apache.mesos.Protos.MasterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.yjfei.paas.manage.PaasDriverManage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaasLeaderListener implements LeaderLatchListener {

	@Autowired
	private PaasDriverManage driverManager;

	private volatile boolean master;

	public PaasLeaderListener() {
		this.master = false;
	}

	@Override
	public void isLeader() {
		log.info("We are now the leader! Current status {}", driverManager.getCurrentStatus());

		master = true;

		if (driverManager.getCurrentStatus() != Protos.Status.DRIVER_RUNNING) {
			try {
				driverManager.startMesos();
			} catch (Throwable t) {
				log.error("While starting driver", t);

			}
		}
	}

	public boolean isMaster() {
		return master;
	}

	public Optional<MasterInfo> getMaster() {
		return driverManager.getMaster();
	}

	public long getLastOfferTimestamp() {
		return driverManager.getLastOfferTimestamp();
	}

	public Protos.Status getCurrentStatus() {
		return driverManager.getCurrentStatus();
	}

	@Override
	public void notLeader() {
		log.info("We are not the leader! Current status {}", driverManager.getCurrentStatus());

		master = false;

		if (driverManager.getCurrentStatus() == Protos.Status.DRIVER_RUNNING) {
			try {
				driverManager.stop();
			} catch (Throwable t) {
				log.error("While stopping driver", t);
			}
		}
	}


}
