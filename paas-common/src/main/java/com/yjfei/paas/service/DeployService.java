package com.yjfei.paas.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.yjfei.paas.common.DeployState;
import com.yjfei.paas.dao.DeployRepository;
import com.yjfei.paas.dao.DeployStatisticsRepository;
import com.yjfei.paas.dao.DeployStatusRepository;
import com.yjfei.paas.data.Deploy;
import com.yjfei.paas.data.DeployStatistics;
import com.yjfei.paas.entity.DeployEntity;
import com.yjfei.paas.entity.DeployStatisticsEntity;
import com.yjfei.paas.entity.DeployStatusEntity;
import com.yjfei.paas.util.ConvertUtil;

@Service
public class DeployService {

	@Autowired
	private DeployRepository deployRepository;

	@Autowired
	private DeployStatisticsRepository deployStatisticsRepository;

	@Autowired
	private DeployStatusRepository deployStatusRepository;

	public Page<DeployEntity> queryDeployByField(final String field, final String value, int page, int count) {
		Specification<DeployEntity> specification = new Specification<DeployEntity>() {
			@Override
			public Predicate toPredicate(Root<DeployEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Path<String> _name = root.get(field);
				Predicate _key = criteriaBuilder.like(_name, "%" + value + "%");
				return criteriaBuilder.and(_key);
			}
		};
		Sort sort = new Sort(Direction.ASC, "id");
		Pageable pageable = new PageRequest(page - 1, count, sort);
		return deployRepository.findAll(specification, pageable);
	}

	public void addDeploy(DeployEntity app) {
		deployRepository.save(app);
	}

	public void updateDeploy(DeployEntity app) {
		deployRepository.saveAndFlush(app);
	}

	public void addDeployStatistics(DeployStatisticsEntity deployStatistics) {
		this.deployStatisticsRepository.save(deployStatistics);
	}

	public DeployStatisticsEntity findDeployStatisticsByDeployId(long deployId) {
		return this.deployStatisticsRepository.findByDeployId(deployId);
	}

	public List<Deploy> listAllPendingDeploy() {

		List<Deploy> deployList = Lists.newArrayList();
		List<DeployStatusEntity> statusList = deployStatusRepository.findByStatus(DeployState.ACTIVE.getCode());

		if (!CollectionUtils.isEmpty(statusList)) {
			List<String> appIdList = Lists.newArrayList();

			for (DeployStatusEntity status : statusList) {
				appIdList.add(status.getAppId());
			}

			deployList = listDeployByAppId(appIdList);
		}

		return deployList;
	}

	public void updateDeployStatus(long deployId, DeployState status) {
		deployStatusRepository.updateStatusByDeployId(deployId, status.getCode(), new Date());
	}
	
	public Optional<Deploy> findDeployByDeployId(long deployId){
		DeployEntity entity = this.deployRepository.findOne(deployId);
		
		if(entity != null){
			Deploy deploy = ConvertUtil.convert(entity, Deploy.class);
			return Optional.of(deploy);
		}
		
		return Optional.<Deploy>absent();
	}
	
	public Optional<DeployState> findDeployStatusByDeployId(long deployId){
		DeployStatusEntity entity = this.deployStatusRepository.findByDeployId(deployId);
		if(entity != null){
			DeployState deployState = DeployState.getStateByCode(entity.getStatus());
	        return Optional.of(deployState);
		}
		return Optional.<DeployState>absent();
	}
	
	public List<Deploy> listDeployByAppId(Collection<String> appIds){
		List<Deploy> deployList = Lists.newArrayList();
		List<DeployEntity> entityList = deployRepository.findByAppIdIn(appIds);

		if (!CollectionUtils.isEmpty(entityList)) {
			deployList = ConvertUtil.convert(entityList, entity -> {
				Deploy deploy = ConvertUtil.convert(entity, Deploy.class);
				return deploy;
			});
		}
		return deployList;
	}
	
	public List<Deploy> listDeployByDeployId(Collection<Long> deployIds){
		List<Deploy> deployList = Lists.newArrayList();
		List<DeployEntity> entityList = deployRepository.findAll(deployIds);

		if (!CollectionUtils.isEmpty(entityList)) {
			deployList = ConvertUtil.convert(entityList, entity -> {
				Deploy deploy = ConvertUtil.convert(entity, Deploy.class);
				return deploy;
			});
		}
		return deployList;
	}	

	public DeployStatistics findDeployStatistics(long deployId) {
		DeployStatisticsEntity entity = this.deployStatisticsRepository.findByDeployId(deployId);
		DeployStatistics statistics = null;
		if (entity != null) {
			statistics = ConvertUtil.convert(entity, DeployStatistics.class);
		}
		return statistics;
	}
	
	public void saveDeployStatistics(DeployStatistics statistics) {
		DeployStatisticsEntity entity =  ConvertUtil.convert(statistics,DeployStatisticsEntity.class);
		entity.setLastFinishState(statistics.getLastTaskStateAt().getCode());
		deployStatisticsRepository.saveAndFlush(entity);
	}

	public List<Deploy> listDeployByState(DeployState state){
		List<Deploy> deployList = Lists.newArrayList();
		List<DeployStatusEntity> statusList = deployStatusRepository.findByStatus(state.getCode());
		
		if (!CollectionUtils.isEmpty(statusList)) {
			List<Long> ids = Lists.newArrayList();
			statusList.forEach(entity -> {
				ids.add(entity.getId());
			});

			List<DeployEntity> entityList = deployRepository.findAll(ids);
			
			if(!CollectionUtils.isEmpty(entityList)){
				deployList = ConvertUtil.convert(entityList, entity -> {
					Deploy deploy = ConvertUtil.convert(entity, Deploy.class);
					return deploy;
				});
			}
		}
		
		return deployList;
	}
}
