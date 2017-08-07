package com.yjfei.paas.manage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yjfei.paas.common.AppType;
import com.yjfei.paas.common.DeployState;
import com.yjfei.paas.data.App;
import com.yjfei.paas.data.Deploy;
import com.yjfei.paas.data.DeployStatistics;
import com.yjfei.paas.data.Resources;
import com.yjfei.paas.entity.AppEntity;
import com.yjfei.paas.service.AppService;
import com.yjfei.paas.service.DeployService;

@Component
public class DeployManager {

	@Autowired
	private DeployService deployService;

	@Autowired
	private AppService appService;

	public List<Deploy> getAllPendingDeploy() {
		return deployService.listAllPendingDeploy();
	}

	public void updateStatusByDeployId(long deployId, DeployState status) {
		deployService.updateDeployStatus(deployId, status);
	}

	public Optional<DeployStatistics> getDeployStatistics(long deployId) {
		DeployStatistics statistics = deployService.findDeployStatistics(deployId);
		if (statistics == null) {
			return Optional.<DeployStatistics>absent();
		}
		return Optional.of(statistics);
	}
	
	public Optional<Deploy> getDeploy(long deployId){
		return deployService.findDeployByDeployId(deployId);
	}
	
	public Optional<DeployState> getDeployStatus(long deployId){
		return deployService.findDeployStatusByDeployId(deployId);
	}
	
	public void saveDeployStatistics(DeployStatistics statistics ){
		deployService.saveDeployStatistics(statistics);
	}
	
	public List<Deploy> getAllCoolDownDeploy(){		
		return deployService.listDeployByState(DeployState.COOLDOWN);
	}
	
	public void finish(long deployId){
		deployService.updateDeployStatus(deployId, DeployState.FINISHED);
	}
	
	public void coolDown(long deployId){
		deployService.updateDeployStatus(deployId, DeployState.COOLDOWN);
	}
	
	public void active(long deployId){
		deployService.updateDeployStatus(deployId, DeployState.ACTIVE);
	}

	public Map<String, Resources> getAllResourceByAppId(Collection<String> appIds) {
		Map<String, Resources> map = Maps.newHashMap();
		List<AppEntity> entityList = appService.listAppByAppId(appIds);

		if (!CollectionUtils.isEmpty(entityList)) {

			for (AppEntity app : entityList) {
				String affinityStr = app.getAffinity();
				List<String> affinity = Lists.newArrayList();
				if(!StringUtils.isEmpty(affinityStr)){
					affinity = JSON.parseArray(affinityStr, String.class);
				}
				map.put(app.getAppId(), new Resources(app.getCpu(), app.getMemory(), app.getPorts(), app.getDisk(),affinity));
			}
		}
		return map;
	}
	
	public  Map<Long, Deploy> getAllDeployByDeployId(Collection<Long> deployIds) {
		Map<Long, Deploy> map = Maps.newHashMap();
		List<Deploy> deployList = deployService.listDeployByDeployId(deployIds);
		
		for(Deploy deploy:deployList){
			map.put(deploy.getId(), deploy);
		}
		return map;
	}
	
	
	public  Map<String, Deploy> getAllDeployByAppId(Collection<String> appIds) {
		Map<String, Deploy> map = Maps.newHashMap();
		List<Deploy> deployList = deployService.listDeployByAppId(appIds);
		
		for(Deploy deploy:deployList){
			map.put(deploy.getAppId(), deploy);
		}
		return map;
	}
	
	public Optional<AppType> getAppTypeByAppId(String appId){
		App app = appService.findAppByAppId(appId);
		
		if(app != null){
			Optional.of(app.getType());
		}
		
		return Optional.<AppType>absent();
	}
	
}
