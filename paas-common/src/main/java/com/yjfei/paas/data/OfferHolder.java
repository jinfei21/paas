package com.yjfei.paas.data;

import java.util.List;

import org.apache.mesos.Protos;
import org.apache.mesos.Protos.Resource;
import org.apache.mesos.Protos.Status;
import org.apache.mesos.Protos.TaskInfo;
import org.apache.mesos.SchedulerDriver;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.yjfei.paas.util.MesosUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OfferHolder {

	private final Protos.Offer offer;
	private final List<DeployTaskRequest> acceptedTasks;
	private List<Resource> currentResources;

	public OfferHolder(Protos.Offer offer, int taskSizeHint) {
		this.offer = offer;
		this.acceptedTasks = Lists.newArrayListWithCapacity(taskSizeHint);
		this.currentResources = offer.getResourcesList();
	}

	public void addMatchedTask(DeployTaskRequest task) {
		acceptedTasks.add(task);
		currentResources = MesosUtil.subtractResources(currentResources, task.getMesosTask().getResourcesList());
	}

	public void launchTasks(SchedulerDriver driver) {
		final List<TaskInfo> toLaunch = Lists.newArrayListWithCapacity(acceptedTasks.size());
		final List<String> taskIds = Lists.newArrayListWithCapacity(acceptedTasks.size());

		for (DeployTaskRequest task : acceptedTasks) {
			taskIds.add(task.getMesosTask().getTaskId().toString());
			toLaunch.add(task.getMesosTask());
			log.trace("Launching {} mesos task: {}", task.getMesosTask().getTaskId().toString(), task.getMesosTask());
		}

		Status initialStatus = driver.launchTasks(ImmutableList.of(offer.getId()), toLaunch);

		log.info("{} tasks ({}) launched with status {}", taskIds.size(), taskIds, initialStatus);
	}

	public List<DeployTaskRequest> getAcceptedTasks() {
		return acceptedTasks;
	}

	public List<Resource> getCurrentResources() {
		return currentResources;
	}

	public Protos.Offer getOffer() {
		return offer;
	}
}
