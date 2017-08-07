package com.yjfei.paas.dao;

import java.util.List;

import com.yjfei.paas.entity.SlaveEntity;

public interface SlaveRepository extends BaseJpaRepository<SlaveEntity, Long> {
	List<SlaveEntity> findByStatus(int status);
	List<SlaveEntity> findByRackId(String rackid);
	SlaveEntity findBySlaveId(String slaveId);
}
