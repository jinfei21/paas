package com.yjfei.paas.dao;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.dao.AppsRepository;
import com.yjfei.paas.entity.AppsEntity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
public class AppsRepositoryTest {

	@Autowired
	private AppsRepository appsRepository;

	@Test
	public void testAddApps()  throws Exception {
		AppsEntity app = new AppsEntity();
		app.setAppId("1000012001");
		app.setName("zy_test0801");
		app.setCreateTime(new Date());
		app.setModifyTime(new Date());

		app.setDepart("zy_test0801");

		app.setOwner("zy_test0801");
		app.setEmail("yzhong@seu.edu.cn");
		appsRepository.save(app);
		System.out.println(app.toString());
	}
	
	@Test
	public void testUpdateApp() throws Exception {
		
		AppsEntity app = new AppsEntity();
		app.setId(111);
		app.setAppId("1000012001");

		app.setCreateTime(new Date());
		app.setModifyTime(new Date());

		app.setDepart("1");

		app.setOwner("test");
		
		appsRepository.saveAndFlush(app);
	}

	@Test
	public void testFindappId() throws Exception {
		AppsEntity app = appsRepository.findOne(47L);
		System.out.println(app.toString());
		Assert.assertNotNull(app);
	}

}