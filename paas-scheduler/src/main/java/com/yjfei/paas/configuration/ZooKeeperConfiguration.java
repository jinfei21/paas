package com.yjfei.paas.configuration;

import lombok.Data;

@Data
public class ZooKeeperConfiguration {
  
  private String quorum;
  
  private int sessionTimeoutMillis = 600_000;
  
  private int connectTimeoutMillis = 60_000;
  
  private int retryBaseSleepTimeMilliseconds = 1_000;
  
  private int retryMaxTries = 3;
  
  private String zkNamespace;
 
}
