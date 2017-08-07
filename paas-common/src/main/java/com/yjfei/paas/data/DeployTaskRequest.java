package com.yjfei.paas.data;

import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.Resource;
import org.apache.mesos.Protos.TaskInfo;
import org.apache.mesos.Protos.Value.Range;

import com.google.common.base.Optional;
import com.yjfei.paas.util.MesosUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeployTaskRequest implements Comparable<DeployTaskRequest> {

	private ScheduleTask scheduleTask;

	private Deploy deploy;

	private Offer offer;

	private TaskInfo mesosTask;

	private Resources resource;

	@Override
	public int compareTo(DeployTaskRequest o) {

		return (int) (scheduleTask.getId() - o.getScheduleTask().getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else {
			DeployTaskRequest other = (DeployTaskRequest) obj;
			if (this.scheduleTask.getId() == other.getScheduleTask().getId()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) this.scheduleTask.getId();
	}

	public Optional<Long> getFirstPort() {
		for (Resource resource : mesosTask.getResourcesList()) {
			if (resource.getName().equals(MesosUtil.PORTS)) {
				for (Range range : resource.getRanges().getRangeList()) {
					return Optional.of(range.getBegin());
				}
			}
		}

		return Optional.absent();
	}
}
