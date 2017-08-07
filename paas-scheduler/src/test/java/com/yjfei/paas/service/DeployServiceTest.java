package com.yjfei.paas.service;

import com.google.common.collect.Lists;
import com.yjfei.paas.PaaSApplication;
import com.yjfei.paas.common.DeployState;
import com.yjfei.paas.dao.DeployRepository;
import com.yjfei.paas.entity.DeployEntity;
import com.yjfei.paas.service.DeployService;
import com.yjfei.paas.vo.DeployVO;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
public class DeployServiceTest {

	@Autowired
	private DeployService deployService;
//
//	@Test
//	public void testAddDeploy()  throws Exception {
//		DeployEntity deploy = new DeployEntity();
//		deploy.setAppId("1000012001");
//		deploy.setCmd("test");
//		deploy.setCreateTime(new Date());
//		deploy.setPackageId("1111");
//		deploy.setModifyTime(new Date());
//		deploy.setKeepAlive(true);
//		deploy.setHealthPath("/api");
//		deploy.setSlbPath("/");
//		deploy.setTag("v1");
//
//		deployRepository.save(deploy);
//		System.out.println("fsaf");
//	}
//
//	@Test
//	public void testUpdateDeploy() throws Exception {
//
//		DeployEntity deploy = new DeployEntity();
//		deploy.setId(111);
//		deploy.setAppId("1000012001");
//		deploy.setCmd("test");
//		deploy.setCreateTime(new Date());
//		deploy.setPackageId("1111");
//		deploy.setModifyTime(new Date());
//		deploy.setKeepAlive(true);
//		deploy.setHealthPath("/api");
//		deploy.setSlbPath("/");
//		deploy.setTag("v1");
//
//		deployRepository.saveAndFlush(deploy);
//	}

	@Test
	public void testQueryDeploy() throws Exception {
		Page<DeployEntity> pageResult = deployService.queryDeployByField("appId", "%", 1, 10);
		List<DeployVO> list = Lists.newArrayList();
		for (DeployEntity entity : pageResult.getContent()) {
			System.out.println(entity.toString());
			list.add(new DeployVO(entity));
		}
		System.out.println("---------------------------------------------------");
		for(DeployVO vo : list){
			System.out.println(vo.toString());
		}
//		List<DeployEntity> deploy = deployService.listDeployByState(DeployState.ACTIVE);
//		System.out.println(deploy.toString());
//		Assert.assertNotNull(deploy);
	}
	

}