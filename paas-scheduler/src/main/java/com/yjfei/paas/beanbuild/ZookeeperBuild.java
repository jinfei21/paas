package com.yjfei.paas.beanbuild;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yjfei.paas.configuration.PaasConfiguration;
import com.yjfei.paas.configuration.ZooKeeperConfiguration;

@Configuration
public class ZookeeperBuild {

	@Autowired
	private PaasConfiguration configuration;
	
    @Bean
    public CuratorFramework buildCurator(){
		ZooKeeperConfiguration zookeeperConfig = configuration.getZooKeeperConfiguration();

		CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
	        .defaultData(null)
	        .sessionTimeoutMs(zookeeperConfig.getSessionTimeoutMillis())
	        .connectionTimeoutMs(zookeeperConfig.getConnectTimeoutMillis())
	        .connectString(zookeeperConfig.getQuorum())
	        .retryPolicy(new ExponentialBackoffRetry(zookeeperConfig.getRetryBaseSleepTimeMilliseconds(), zookeeperConfig.getRetryMaxTries()))
	        .namespace(zookeeperConfig.getZkNamespace()).build();
		return curatorFramework;
    }
}
