package com.yjfei.paas.service;

import com.yjfei.paas.common.AppType;
import com.yjfei.paas.dao.AppsRepository;
import com.yjfei.paas.data.App;
import com.yjfei.paas.entity.AppEntity;
import com.yjfei.paas.entity.AppsEntity;
import com.yjfei.paas.util.ConvertUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;

@Service
public class AppsService {
	
    @Autowired
    private AppsRepository appsRepository;

	public Page<AppsEntity> queryAppByField(final String field, final String value, int page, int count){
		Specification<AppsEntity> specification = new Specification<AppsEntity>() {
			@Override
			public Predicate toPredicate(Root<AppsEntity> root,
					CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				Path<String> _name = root.get(field);
				Predicate _key = criteriaBuilder.like(_name, "%" + value + "%");
				return criteriaBuilder.and(_key);
			}
		};
		Sort sort = new Sort(Direction.ASC, "id");
		Pageable pageable = new PageRequest(page - 1, count, sort);
		return appsRepository.findAll(specification, pageable);
	}
	
	
	public void addApp(AppsEntity app){
		appsRepository.save(app);
	}
	
    public void updateApp(AppsEntity app){
    	appsRepository.saveAndFlush(app);
    }
    
//    public List<AppsEntity> listAppByAppId(Collection<String> ids){
//    	return appsRepository.findByAppIdIn(ids);
//    }
    
//    public Apps findAppByAppId(String appId){
//    	AppsEntity entity = appsRepository.findByAppId(appId);
//    	Apps app = null;
//    	if(entity != null){
//    		app = ConvertUtil.convert(entity, App.class);
////    		app.setType(AppType.getAppTypeByCode(entity.getType()));
//    	}
//    	return app;
//    }
}
