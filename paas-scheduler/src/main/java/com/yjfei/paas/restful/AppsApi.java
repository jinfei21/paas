package com.yjfei.paas.restful;

import com.google.common.collect.Lists;
import com.yjfei.paas.common.PageResult;
import com.yjfei.paas.common.Result;
import com.yjfei.paas.entity.AppsEntity;
import com.yjfei.paas.service.AppsService;
import com.yjfei.paas.vo.AppsVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/apps")
@Slf4j
public class AppsApi {

	@Autowired
	private AppsService appsService;

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageResult<List<AppsVO>> queryApp(@RequestParam(value = "field", defaultValue = "appId") String field,
			@RequestParam(value = "keyword", defaultValue = "%") String value,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "pageSize", defaultValue = "10") int count) {
		PageResult<List<AppsVO>> result = new PageResult<List<AppsVO>>();
		try {
			Page<AppsEntity> pageResult = appsService.queryAppByField(field, value, page, count);
			List<AppsVO> list = Lists.newArrayList();
			for (AppsEntity entity : pageResult.getContent()) {
				list.add(new AppsVO(entity));
			}
			result.setData(list);
			result.getPage().setCurrent(page);
			result.getPage().setPageSize(pageResult.getSize());
			result.getPage().setTotal(pageResult.getTotalElements());
		} catch (Throwable t) {
			log.error("queryApp error,field=" + field + ",value=" + value + ",page=" + page + ",count=" + count, t);
			result.setSuccess(false);
			result.setMessage(t.getMessage());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<String> addApp(@RequestBody AppsVO vo) {
		Result<String> result = new Result<String>();

		try {
			AppsEntity app = vo.buildAppEntity();
			app.setCreateTime(new Date());
			app.setModifyTime(new Date());
			appsService.addApp(app);
			result.setData("OK");
		} catch (Throwable t) {
			log.error("addApp error!", t);
			result.setSuccess(false);
			result.setMessage(t.getMessage());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Result<String> updateApp(@RequestBody AppsVO vo) {
		Result<String> result = new Result<String>();

		try {
			AppsEntity app = vo.buildAppEntity();
			app.setModifyTime(new Date());
			appsService.updateApp(app);
			result.setData("OK");
		} catch (Throwable t) {
			log.error("updateApp error!", t);
			result.setSuccess(false);
			result.setMessage(t.getMessage());
		}
		return result;
	}

}
