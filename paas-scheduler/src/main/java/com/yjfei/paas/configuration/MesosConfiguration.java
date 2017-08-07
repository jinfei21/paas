package com.yjfei.paas.configuration;

import com.google.common.base.Optional;

import lombok.Data;

@Data
public class MesosConfiguration {

	private String master;

	private String frameworkName;

	private String frameworkId;

	private double frameworkFailoverTimeout = 0.0;

	private int defaultCpus = 1;

	private int defaultMemory = 64;

	private int defaultDisk = 0;

	private boolean checkpoint = true;

	private String frameworkRole = "default";

	private String rackIdAttributeKey = "rackid";

	private String defaultRackId = "DEFAULT";

	private Optional<String> credentialPrincipal = Optional.absent();
	
	private Optional<String> credentialSecret = Optional.absent();
}
