package com.yjfei.paas.poller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.yjfei.paas.data.Deploy;
import com.yjfei.paas.data.DeployStatistics;
import com.yjfei.paas.manage.DeployManager;
import com.yjfei.paas.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Scope("singleton")
public class CoolDownPoller extends BasePoller {

	@Autowired
	private CooldownChecker cooldown;

	@Autowired
	private DeployManager deplopyManager;

	public CoolDownPoller() {
		super("CoolDownPoller");
	}

	@Override
	public void process() {

		final long start = System.currentTimeMillis();

		List<Deploy> deployList = deplopyManager.getAllCoolDownDeploy();
		if (deployList.isEmpty()) {
			log.trace("No cooldown deploy");
			return;
		}

		int exitedCooldown = 0;

		for (Deploy deploy : deployList) {
			if (checkCooldown(deploy)) {
				exitedCooldown++;
			}
		}

		log.info("{} out of {} cooldown deploy exited cooldown in {}", exitedCooldown, deployList.size(),
				TimeUtil.duration(start));
	}

	private boolean checkCooldown(Deploy deploy) {
		if (shouldExitCooldown(deploy)) {
			deplopyManager.active(deploy.getId());
			return true;
		}

		return false;
	}
	
	  private boolean shouldExitCooldown(Deploy deploy) {
		  
		  Optional<DeployStatistics> maybeDeployStatistics=  deplopyManager.getDeployStatistics(deploy.getId());
		  	
		    if (!maybeDeployStatistics.isPresent()) {
		      log.trace("appId {} deployId {} had no deploy statistics, exiting cooldown", deploy.getAppId(),deploy.getId());
		      return true;
		    }

		    Date lastFinishAt = maybeDeployStatistics.get().getLastFinishAt();

		    if (lastFinishAt == null) {
		      log.trace("appId {} deployId {} had no last finish, exiting cooldown",deploy.getAppId(),deploy.getId());
		      return true;
		    }

		    if (cooldown.hasCooldownExpired(maybeDeployStatistics.get())) {
		      return true;
		    }

		    return false;
		  }
}
