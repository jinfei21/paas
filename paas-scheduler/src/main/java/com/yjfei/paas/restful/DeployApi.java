package com.yjfei.paas.restful;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.yjfei.paas.common.PageResult;
import com.yjfei.paas.common.Result;
import com.yjfei.paas.entity.DeployEntity;
import com.yjfei.paas.service.DeployService;
import com.yjfei.paas.vo.DeployVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/deploy")
@Slf4j
public class DeployApi {

	@Autowired
	private DeployService deployService;

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageResult<List<DeployVO>> queryDeploy(@RequestParam(value = "field", defaultValue = "appid") String field,
			@RequestParam(value = "keyword", defaultValue = "%") String value,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "pageSize", defaultValue = "10") int count) {
		PageResult<List<DeployVO>> result = new PageResult<List<DeployVO>>();
		try {
			Page<DeployEntity> pageResult = deployService.queryDeployByField(field, value, page, count);
			List<DeployVO> list = Lists.newArrayList();
			for (DeployEntity entity : pageResult.getContent()) {
				list.add(new DeployVO(entity));
			}
			result.setData(list);
			result.getPage().setCurrent(page);
			result.getPage().setPageSize(pageResult.getSize());
			result.getPage().setTotal(pageResult.getTotalElements());
		} catch (Throwable t) {
			log.error("queryDeploy error,field=" + field + ",value=" + value + ",page=" + page + ",count=" + count, t);
			result.setSuccess(false);
			result.setMessage(t.getMessage());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<String> addDeploy(@RequestBody DeployVO vo) {
		Result<String> result = new Result<String>();

		try {
			DeployEntity deploy = vo.buildDeployEntity();
			deploy.setCreateTime(new Date());
			deploy.setModifyTime(new Date());
			deployService.addDeploy(deploy);
			result.setData("OK");
		} catch (Throwable t) {
			log.error("addDeploy error!", t);
			result.setSuccess(false);
			result.setMessage(t.getMessage());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Result<String> updateDeploy(@RequestBody DeployVO vo) {
		Result<String> result = new Result<String>();

		try {
			DeployEntity deploy = vo.buildDeployEntity();
			deploy.setModifyTime(new Date());
			deployService.updateDeploy(deploy);
			result.setData("OK");
		} catch (Throwable t) {
			log.error("updateDeploy error!", t);
			result.setSuccess(false);
			result.setMessage(t.getMessage());
		}
		return result;
	}
}
