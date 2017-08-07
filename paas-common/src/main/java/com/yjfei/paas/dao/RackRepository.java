package com.yjfei.paas.dao;

import java.util.List;

import com.yjfei.paas.entity.RackEntity;

public interface RackRepository extends BaseJpaRepository<RackEntity, Long> {
	List<RackEntity> findByStatus(int status);
	RackEntity findByRackId(String rackId);
}
