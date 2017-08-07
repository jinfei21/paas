package com.yjfei.paas.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yjfei.paas.entity.DeployEntity;

public interface DeployRepository extends BaseJpaRepository<DeployEntity, Long> ,JpaSpecificationExecutor<DeployEntity>{
	
	List<DeployEntity> findByAppIdIn(Collection<String> appId);
	
}
