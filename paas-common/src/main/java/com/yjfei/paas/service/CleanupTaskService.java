package com.yjfei.paas.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yjfei.paas.common.CleanupType;
import com.yjfei.paas.dao.CleanupTaskRepository;
import com.yjfei.paas.data.CleanupTask;
import com.yjfei.paas.entity.CleanupTaskEntity;
import com.yjfei.paas.util.ConvertUtil;

@Service
public class CleanupTaskService {

	@Autowired
	private CleanupTaskRepository cleanupTaskRepository;
 	
	
	public List<CleanupTaskEntity> listCleanupTask(CleanupType type){
		return this.cleanupTaskRepository.findByType(type.getCode());
	}
	
	public void updateCleanupTask(CleanupTaskEntity cleanupTask){
		this.cleanupTaskRepository.saveAndFlush(cleanupTask);
	}
	
	public void addCleanupTask(CleanupTaskEntity cleanupTask){
		this.cleanupTaskRepository.save(cleanupTask);
	}
	
	public void addCleanupTask(CleanupTask cleanupTask){
		CleanupTaskEntity entity = ConvertUtil.convert(cleanupTask, CleanupTaskEntity.class);		
		entity.setType(cleanupTask.getType().getCode());
		entity.setCreateTime(new Date());
		entity.setModifyTime(new Date());
		this.cleanupTaskRepository.save(entity);
	}
	
	public void updateCleanupTask(CleanupTask cleanupTask){
		CleanupTaskEntity entity = ConvertUtil.convert(cleanupTask, CleanupTaskEntity.class);		
		entity.setType(cleanupTask.getType().getCode());
		entity.setModifyTime(new Date());
		this.cleanupTaskRepository.saveAndFlush(entity);
	}
	
	public void deleteCleanupTask(long id){
		this.cleanupTaskRepository.delete(id);
	}
}
