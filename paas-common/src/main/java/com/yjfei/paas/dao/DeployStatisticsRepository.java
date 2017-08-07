package com.yjfei.paas.dao;

import com.yjfei.paas.entity.DeployStatisticsEntity;

public interface DeployStatisticsRepository extends BaseJpaRepository<DeployStatisticsEntity, Long> {

	DeployStatisticsEntity findByDeployId(long deployId);

}
