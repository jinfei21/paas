package com.yjfei.paas.data;

import com.yjfei.paas.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaasTaskId {
	
	private String appId;
	private long deployId;
	private long startedAt;
	private int instanceNo;
	private String host;
	private String rackId;
	private long taskId;

	@Override
	public String toString() {
		return String.format("%s-%s-%s-%s-%s-%s-%s", appId, deployId, startedAt, instanceNo, host, rackId,taskId);
	}

	public static PaasTaskId fromString(String string) throws RuntimeException {
		String[] splits = null;

		try {
			splits = StringUtil.reverseSplit(string, 7, "-");
		} catch (IllegalStateException ise) {
			throw new RuntimeException(String.format("TaskId %s was invalid (%s)", string, ise.getMessage()));
		}

		try {
			final String appId = splits[0];
			final long deployId = Long.parseLong(splits[1]);
			final long startedAt = Long.parseLong(splits[2]);
			final int instanceNo = Integer.parseInt(splits[3]);
			final String host = splits[4];
			final String rackId = splits[5];
			final long taskId = Long.parseLong(splits[6]);

			return new PaasTaskId(appId, deployId, startedAt, instanceNo, host, rackId,taskId);
		} catch (NumberFormatException nfe) {
			throw new RuntimeException(
					String.format("TaskId %s had an invalid number parameter (%s)", string, nfe.getMessage()));
		}
	}
}
