package com.yjfei.paas.dao;

import java.util.List;

import com.yjfei.paas.entity.CleanupTaskEntity;

public interface CleanupTaskRepository extends BaseJpaRepository<CleanupTaskEntity, Long> {

	List<CleanupTaskEntity> findByType(int type);
}
