package com.yjfei.paas.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yjfei.paas.entity.AppEntity;

public interface AppRepository extends BaseJpaRepository<AppEntity, Long>,JpaSpecificationExecutor<AppEntity>{
	    
	List<AppEntity> findByAppIdIn(Collection<String> appId);
	AppEntity findByAppId(String appId);
}
