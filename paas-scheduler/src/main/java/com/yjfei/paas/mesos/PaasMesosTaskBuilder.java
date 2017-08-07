package com.yjfei.paas.mesos;

import java.util.Collections;
import java.util.List;

import org.apache.mesos.Protos;
import org.apache.mesos.Protos.CommandInfo;
import org.apache.mesos.Protos.CommandInfo.URI;
import org.apache.mesos.Protos.Environment;
import org.apache.mesos.Protos.Environment.Variable;
import org.apache.mesos.Protos.Resource;
import org.apache.mesos.Protos.TaskID;
import org.apache.mesos.Protos.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.yjfei.paas.data.DeployTaskRequest;
import com.yjfei.paas.data.PaasTaskId;
import com.yjfei.paas.data.Resources;
import com.yjfei.paas.util.MesosUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaasMesosTaskBuilder {

	@Autowired
	private SchedulerService scheduler;

	public DeployTaskRequest buildTask(Protos.Offer offer, List<Resource> availableResources,
			DeployTaskRequest taskRequest) {

		final String rackId = scheduler.getRackId(offer);
		final String host = scheduler.getSlaveHost(offer);

		PaasTaskId taskId = new PaasTaskId(taskRequest.getScheduleTask().getAppId(),
				taskRequest.getScheduleTask().getDeployId(), taskRequest.getScheduleTask().getScheduleTime().getTime(),
				taskRequest.getScheduleTask().getInstanceNo(), host, rackId,taskRequest.getScheduleTask().getId());

		final TaskInfo.Builder bldr = TaskInfo.newBuilder().setTaskId(TaskID.newBuilder().setValue(taskId.toString()));

		Resources desiredTaskResources = taskRequest.getResource();

		Optional<long[]> ports = Optional.absent();
		Optional<Resource> portsResource = Optional.absent();

		if (desiredTaskResources.getNumPorts() > 0) {
			portsResource = Optional.of(MesosUtil.getPortsResource(desiredTaskResources.getNumPorts(),
					availableResources, Collections.<Long>emptyList()));
			ports = Optional.of(MesosUtil.getPorts(portsResource.get(), desiredTaskResources.getNumPorts()));
		}
		
	    prepareCommand(bldr, taskId, taskRequest, ports);
	    

	    if (portsResource.isPresent()) {
	      bldr.addResources(portsResource.get());
	    }

	    bldr.addResources(MesosUtil.getCpuResource(desiredTaskResources.getCpus(),Optional.<String>absent()));
	    bldr.addResources(MesosUtil.getMemoryResource(desiredTaskResources.getMemoryMb(),Optional.<String>absent()));
	    bldr.addResources(MesosUtil.getDiskResource(desiredTaskResources.getDiskMb(), Optional.<String>absent()));
	    bldr.setSlaveId(offer.getSlaveId());

	    bldr.setName(taskRequest.getDeploy().getAppId());

	    TaskInfo task = bldr.build();
	    
	    taskRequest.setMesosTask(task);
	    taskRequest.setOffer(offer);
	    taskRequest.getScheduleTask().setHost(host);
	    taskRequest.getScheduleTask().setSlaveId(offer.getSlaveId().getValue());
	    return taskRequest;

	}

	private void prepareCommand(final TaskInfo.Builder bldr, final PaasTaskId taskId, final DeployTaskRequest task,
			final Optional<long[]> ports) {
		CommandInfo.Builder commandBldr = CommandInfo.newBuilder();

		commandBldr.setValue(task.getDeploy().getCmd());

		commandBldr.addUris(URI.newBuilder().setValue(task.getDeploy().getPackageUri()).build());
		prepareEnvironment(task, commandBldr, ports);

		bldr.setCommand(commandBldr);
	}

	private void prepareEnvironment(final DeployTaskRequest task, CommandInfo.Builder commandBuilder,
			final Optional<long[]> ports) {
		Environment.Builder envBldr = Environment.newBuilder();

		envBldr.addVariables(Variable.newBuilder().setName("INSTANCE_NO")
				.setValue(Integer.toString(task.getScheduleTask().getInstanceNo())).build());

		envBldr.addVariables(Variable.newBuilder().setName("TASK_DEPLOY_ID")
				.setValue(Long.toString(task.getScheduleTask().getDeployId())).build());

		// for (Entry<String, String> envEntry :
		// task.getDeploy().getEnv().or(Collections.<String, String>emptyMap())
		// .entrySet()) {
		// envBldr.addVariables(
		// Variable.newBuilder().setName(envEntry.getKey()).setValue(envEntry.getValue()).build());
		// }

		if (ports.isPresent()) {
			for (int portNum = 0; portNum < ports.get().length; portNum++) {
				if (portNum == 0) {
					envBldr.addVariables(Variable.newBuilder().setName("PORT")
							.setValue(Long.toString(ports.get()[portNum])).build());
					task.getScheduleTask().setPort(ports.get()[portNum]);
				}

				envBldr.addVariables(Variable.newBuilder().setName(String.format("PORT%s", portNum))
						.setValue(Long.toString(ports.get()[portNum])).build());
			}
		}

		commandBuilder.setEnvironment(envBldr.build());
	}
}
