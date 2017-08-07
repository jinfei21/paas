package com.yjfei.paas.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.yjfei.paas.entity.DeployTaskEntity;

public interface DeployTaskRepository extends BaseJpaRepository<DeployTaskEntity, Long> {
    List<DeployTaskEntity> findByStatus(int status);

    List<DeployTaskEntity> findByDeployId(long deployId);

    List<DeployTaskEntity> findByDeployIdAndStatusIn(long deployId, Collection<Integer> status);

    List<DeployTaskEntity> findByStatusIn(Collection<Integer> status);

    List<DeployTaskEntity> findByStatusNotIn(Collection<Integer> status);

    List<DeployTaskEntity> findByAppIdAndStatusIn(String appId, Collection<Integer> status);

    @Modifying
    @Transactional
    @Query("UPDATE DeployTaskEntity a SET a.status=?2 , a.modifyTime=?3 WHERE a.id=?1")
    void updateStatusById(long id, int status, Date modifyTime);

}
