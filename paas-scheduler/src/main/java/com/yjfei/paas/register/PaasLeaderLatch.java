package com.yjfei.paas.register;

import java.util.Set;

import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;

import com.yjfei.paas.util.IPUtil;

public class PaasLeaderLatch extends LeaderLatch {

  private static final String LEADER_PATH = "/leader";

  
  public PaasLeaderLatch(
      final CuratorFramework curatorFramework, final Set<LeaderLatchListener> listeners) throws Exception {
    super(curatorFramework, LEADER_PATH, IPUtil.getLocalIP());

    for (LeaderLatchListener listener : listeners) {
      addListener(listener);
    }
  }

  @PreDestroy
  public void stop() throws Exception {
    super.close();
  }

}