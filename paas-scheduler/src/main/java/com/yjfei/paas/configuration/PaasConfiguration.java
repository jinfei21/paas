package com.yjfei.paas.configuration;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PaasConfiguration {
	
	  private String loadBalancerUri;
	  
	  @NotNull
	  private int cooldownAfterFailures = 5;

	  @NotNull
	  private long cooldownExpiresAfterMinutes = 30;
	  	
	  @JsonProperty("ui")
	  @Valid
	  private UIConfiguration uiConfiguration = new UIConfiguration();
	  	  
	  @JsonProperty("mesos")
	  @Valid
	  private MesosConfiguration mesosConfiguration;

	  @JsonProperty("network")
	  @Valid
	  private NetworkConfiguration networkConfiguration = new NetworkConfiguration();
	  
	  @Min(4)
	  @Max(32)
	  private int deployIdLength = 8;

	  @JsonProperty("zookeeper")
	  @Valid
	  private ZooKeeperConfiguration zooKeeperConfiguration;

	  @JsonProperty("ldap")
	  @Valid
	  private LDAPConfiguration ldapConfiguration;
	  
}
