package com.yjfei.paas.mesos;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.mesos.Protos;
import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.OfferID;
import org.apache.mesos.Protos.SlaveID;
import org.apache.mesos.Protos.TaskStatus;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yjfei.paas.poller.CoolDownPoller;
import com.yjfei.paas.poller.HealthPoller;
import com.yjfei.paas.poller.SLBPoller;
import com.yjfei.paas.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaasMesosSchedulerDelegator implements Scheduler {

	@Autowired
	private PaasMesosScheduler scheduler;
	
	@Autowired
	private HealthPoller healthChecker;
	
	@Autowired
	private SLBPoller slbChecker;
	
	@Autowired
	private CoolDownPoller coolDownChecker;

	private Lock stateLock;

	private PaasLock lock;

	private enum SchedulerState {
		STARTUP, RUNNING, STOPPED;
	}

	private volatile SchedulerState state;

	private List<Protos.TaskStatus> queuedUpdates;

	private long lastOfferTimestamp;

	private final AtomicReference<MasterInfo> masterInfoHolder = new AtomicReference<>();

	@PostConstruct
	public void init() {

		this.queuedUpdates = Lists.newArrayList();
		this.lock = new PaasLock();
		this.stateLock = new ReentrantLock();
		this.state = SchedulerState.STARTUP;
		this.lastOfferTimestamp = 0;
	}

	public long getLastOfferTimestamp() {
		return lastOfferTimestamp;
	}

	public Optional<MasterInfo> getMaster() {
		return Optional.fromNullable(masterInfoHolder.get());
	}

	public void notifyStopping() {
		log.info("Scheduler is moving to stopped, current state: {}", state);

		slbChecker.stop();
		healthChecker.stop();
		coolDownChecker.stop();
		state = SchedulerState.STOPPED;

		log.info("Scheduler now in state: {}", state);
	}

	private void handleUncaughtSchedulerException(Throwable t) {
		log.error("Scheduler threw an uncaught exception - exiting", t);

	}

	private void startup(SchedulerDriver driver, MasterInfo masterInfo) throws Exception {
		Preconditions.checkState(state == SchedulerState.STARTUP, "Asked to startup - but in invalid state: %s",state.name());

		masterInfoHolder.set(masterInfo);
		
		
		//TODO 添加checker启动项
		healthChecker.start();
		slbChecker.start();
		coolDownChecker.start();
		
		stateLock.lock(); // ensure we aren't adding queued updates. calls to
							// status updates are now blocked.

		try {
			state = SchedulerState.RUNNING; // calls to resource offers will now
											// block, since we are already
											// scheduler locked.

			for (Protos.TaskStatus status : queuedUpdates) {
				scheduler.statusUpdate(driver, status);
			}

		} finally {
			stateLock.unlock();
		}
	}

	@Override
	public void registered(SchedulerDriver driver, FrameworkID frameworkId, MasterInfo masterInfo) {
		final long start = lock.lock("registering");

		try {
			scheduler.registered(driver, frameworkId, masterInfo);

			startup(driver, masterInfo);
		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			lock.unlock("registering", start);
		}
	}

	@Override
	public void reregistered(SchedulerDriver driver, MasterInfo masterInfo) {
		final long start = lock.lock("reregistered");

		try {
			scheduler.reregistered(driver, masterInfo);

			startup(driver, masterInfo);
		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			lock.unlock("reregistered", start);
		}
	}

	public boolean isRunning() {
		return state == SchedulerState.RUNNING;
	}

	@Override
	public void resourceOffers(SchedulerDriver driver, List<Offer> offers) {
		lastOfferTimestamp = System.currentTimeMillis();

		if (!isRunning()) {
			log.info(String.format("Scheduler is in state %s, declining %s offer(s)", state.name(), offers.size()));

			for (Protos.Offer offer : offers) {
				driver.declineOffer(offer.getId());
			}

			return;
		}

		final long start = lock.lock("resourceOffers");

		try {
			scheduler.resourceOffers(driver, offers);
		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			lock.unlock("resourceOffers", start);

			log.debug("Handled {} resource offers in {}", offers.size(), TimeUtil.duration(start));
		}
	}

	@Override
	public void offerRescinded(SchedulerDriver driver, OfferID offerId) {
		if (!isRunning()) {
			log.info("Ignoring offer rescind message {} because scheduler isn't running ({})", offerId, state);
			return;
		}

		final long start = lock.lock("offerRescinded");

		try {
			scheduler.offerRescinded(driver, offerId);
		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			lock.unlock("offerRescinded", start);
		}
	}

	@Override
	public void statusUpdate(SchedulerDriver driver, TaskStatus status) {
		final long start = System.currentTimeMillis();

		stateLock.lock();

		try {
			if (!isRunning()) {
				log.info("Scheduler is in state {}, queueing an update {} - {} queued updates so far", state.name(),
						status, queuedUpdates.size());

				queuedUpdates.add(status);

				return;
			}
		} finally {
			stateLock.unlock();
		}

		try {
			scheduler.statusUpdate(driver, status);
		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			log.debug("Handled status update for {} in {}", status.getTaskId().getValue(), TimeUtil.duration(start));
		}
	}

	@Override
	public void frameworkMessage(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId, byte[] data) {
		if (!isRunning()) {
			log.info("Ignoring framework message because scheduler isn't running ({})", state);
			return;
		}

		final long start = lock.lock("frameworkMessage");

		try {
			scheduler.frameworkMessage(driver, executorId, slaveId, data);
		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			lock.unlock("frameworkMessage", start);
		}
	}

	@Override
	public void disconnected(SchedulerDriver driver) {
		if (!isRunning()) {
			log.info("Ignoring disconnect because scheduler isn't running ({})", state);
			return;
		}

		final long start = lock.lock("disconnected");

		try {
			scheduler.disconnected(driver);
		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			lock.unlock("disconnected", start);
		}
	}

	@Override
	public void slaveLost(SchedulerDriver driver, SlaveID slaveId) {
		if (!isRunning()) {
			log.info("Ignoring slave lost {} because scheduler isn't running ({})", slaveId, state);
			return;
		}

		final long start = lock.lock("slaveLost");

		try {
			scheduler.slaveLost(driver, slaveId);
		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			lock.unlock("slaveLost", start);
		}
	}

	@Override
	public void executorLost(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId, int status) {
		if (!isRunning()) {
			log.info("Ignoring executor lost {} because scheduler isn't running ({})", executorId, state);
			return;
		}

		final long start = lock.lock("executorLost");

		try {
			scheduler.executorLost(driver, executorId, slaveId, status);
		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			lock.unlock("executorLost", start);
		}
	}

	@Override
	public void error(SchedulerDriver driver, String message) {
		if (!isRunning()) {
			log.info("Ignoring error {} because scheduler isn't running ({})", message, state);
			return;
		}

		final long start = lock.lock("error");

		try {
			scheduler.error(driver, message);

			log.error("Aborting due to error: {}", message);

		} catch (Throwable t) {
			handleUncaughtSchedulerException(t);
		} finally {
			lock.unlock("error", start);
		}
	}

}
