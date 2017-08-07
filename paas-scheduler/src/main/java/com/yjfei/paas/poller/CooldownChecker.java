package com.yjfei.paas.poller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yjfei.paas.configuration.PaasConfiguration;
import com.yjfei.paas.data.DeployStatistics;
import com.yjfei.paas.util.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CooldownChecker {

	@Autowired
	private PaasConfiguration configuration;

	public boolean hasCooldownExpired(DeployStatistics deployStatistics) {
		if (configuration.getCooldownExpiresAfterMinutes() < 1 || deployStatistics.getLastFinishAt() == null) {
			return false;
		}

		final long cooldownExpiresMillis = TimeUnit.MINUTES.toMillis(configuration.getCooldownExpiresAfterMinutes());

		final long lastFinishAt = deployStatistics.getLastFinishAt().getTime();
		final long timeSinceLastFinish = System.currentTimeMillis() - lastFinishAt;

		final boolean hasCooldownExpired = timeSinceLastFinish > cooldownExpiresMillis;

		if (hasCooldownExpired) {
			log.trace(
					"Request {} cooldown has expired or is not valid because the last task finished {} ago (cooldowns expire after {})",
					deployStatistics.getDeployId(), TimeUtil.duration(timeSinceLastFinish),
					TimeUtil.duration(cooldownExpiresMillis));
		}

		return hasCooldownExpired;
	}
}