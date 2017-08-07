package com.yjfei.paas.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.yjfei.paas.entity.DeployStatusEntity;

public interface DeployStatusRepository extends BaseJpaRepository<DeployStatusEntity, Long> {

	List<DeployStatusEntity> findByStatus(int status);
	
	List<DeployStatusEntity> findByStatusIn(Collection<Integer> status);
	
	DeployStatusEntity findByDeployId(long deployId);
	
    @Modifying
    @Transactional
    @Query("UPDATE DeployStatusEntity a SET a.status=?2 , a.modifyTime=?3 WHERE a.deployId=?1")
	void updateStatusByDeployId(long deployId,int status,Date modifyTime);

}
