package com.yjfei.paas.service;

import java.util.Collection;
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

import com.yjfei.paas.common.AppType;
import com.yjfei.paas.dao.AppRepository;
import com.yjfei.paas.data.App;
import com.yjfei.paas.entity.AppEntity;
import com.yjfei.paas.util.ConvertUtil;

@Service
public class AppService {
	
    @Autowired
    private AppRepository appRepository; 

	public Page<AppEntity> queryAppByField(final String field,final String value, int page, int count){
		Specification<AppEntity> specification = new Specification<AppEntity>() {
			@Override
			public Predicate toPredicate(Root<AppEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Path<String> _name = root.get(field);
				Predicate _key = criteriaBuilder.like(_name, "%" + value + "%");
				return criteriaBuilder.and(_key);
			}
		};
		Sort sort = new Sort(Direction.ASC, "id");
		Pageable pageable = new PageRequest(page - 1, count, sort);
		return appRepository.findAll(specification, pageable);
	}

	public void addApp(AppEntity app){
		appRepository.save(app);
	}
	
    public void updateApp(AppEntity app){
    	appRepository.saveAndFlush(app);
    }
    
    public List<AppEntity> listAppByAppId(Collection<String> ids){
    	return appRepository.findByAppIdIn(ids);
    }
    
    public App findAppByAppId(String appId){
    	AppEntity entity = appRepository.findByAppId(appId);
    	App app = null;
    	if(entity != null){
    		app = ConvertUtil.convert(entity, App.class);
    		app.setType(AppType.getAppTypeByCode(entity.getType()));
    	}
    	return app;
    }
}
