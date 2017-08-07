package com.yjfei.paas.dao;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.dao.DeployRepository;
import com.yjfei.paas.entity.DeployEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
public class DeployRepositoryTest {

	@Autowired
	private DeployRepository deployRepository;

	@Test
	public void testAddDeploy()  throws Exception {
		DeployEntity deploy = new DeployEntity();
		deploy.setAppId("1000012001");
		deploy.setCmd("test");
//		deploy.setCreateTime(new Date());
		deploy.setPackageId("1111");
//		deploy.setModifyTime(new Date());
		deploy.setKeepAlive(true);
		deploy.setHealthPath("/api");
		deploy.setSlbPath("/");
		deploy.setTag("v1");
		System.out.println(deploy.toString());
		deployRepository.save(deploy);
		System.out.println("fsaf");
	}
	
	@Test
	public void testUpdateDeploy() throws Exception {
		
		DeployEntity deploy = new DeployEntity();
		deploy.setId(111);
		deploy.setAppId("1000012001");
		deploy.setCmd("test");
		deploy.setCreateTime(new Date());
		deploy.setPackageId("1111");
		deploy.setModifyTime(new Date());
		deploy.setKeepAlive(true);
		deploy.setHealthPath("/api");
		deploy.setSlbPath("/");
		deploy.setTag("v1");

		deployRepository.saveAndFlush(deploy);
	}

	@Test
	public void testFindById() throws Exception {
		DeployEntity deploy = deployRepository.findOne(3L);
		System.out.println(deploy.toString());
		Assert.assertNotNull(deploy);
	}
	

}