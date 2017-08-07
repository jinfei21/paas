package com.yjfei.paas.dao;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.dao.AppRepository;
import com.yjfei.paas.entity.AppEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
public class AppRepositoryTest {

	@Autowired
	private AppRepository appRepository;

	@Test
	public void testAddApp()  throws Exception {
		AppEntity app = new AppEntity();
		app.setAppId("1000012001");
		app.setAppName("test");
		app.setCpu(5);
		app.setCreateTime(new Date());
		app.setModifyTime(new Date());
		app.setMemory(1000);
		app.setPorts(1);
		app.setType(1);
		app.setDepart("1");
		app.setDesc("test app");
		app.setDisk(100);
		app.setOwner("test");		
		appRepository.save(app);
		System.out.println("fsaf");
	}
	
	@Test
	public void testUpdateApp() throws Exception {
		
		AppEntity app = new AppEntity();
		app.setId(111);
		app.setAppId("1000012001");
		app.setAppName("test");
		app.setCpu(5);
		app.setCreateTime(new Date());
		app.setModifyTime(new Date());
		app.setMemory(1000);
		app.setPorts(1);
		app.setType(1);
		app.setDepart("1");
		app.setDesc("test app");
		app.setDisk(100);
		app.setOwner("test");
		
		appRepository.saveAndFlush(app);
	}

	@Test
	public void testFindById() throws Exception {
		AppEntity app = appRepository.findOne(1L);
		Assert.assertNotNull(app);
	}

}