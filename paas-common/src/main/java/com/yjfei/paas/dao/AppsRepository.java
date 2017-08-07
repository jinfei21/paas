package com.yjfei.paas.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yjfei.paas.entity.AppsEntity;

import java.util.Collection;
import java.util.List;

public interface AppsRepository extends BaseJpaRepository<AppsEntity, Long>,JpaSpecificationExecutor<AppsEntity>{
	    
//	List<AppsEntity> findByAppIdIn(Collection<String> appId);
//	AppsEntity findByAppId(String appId);
}
