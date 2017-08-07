package com.yjfei.paas.service;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.entity.AppsEntity;
import com.yjfei.paas.service.AppsService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
//@Transactional
//@Rollback
public class AppsServiceTest {

    @Autowired
    private AppsService appsService;

    @Test
    public void testAddApps(){
        System.out.println(new Date());
        AppsEntity appsEntity = new AppsEntity();
        appsEntity.setName("zy_test_service_20170801");
        appsEntity.setAppId("zy_test_service_20170801");
        appsEntity.setOwner("zy");
        appsEntity.setDepart("基础框架");
        appsEntity.setCreateTime(new Date());
        appsEntity.setModifyTime(new Date());
        appsEntity.setEmail("zhongyi@ppdai.com");
        appsService.addApp(appsEntity);
    }

}
