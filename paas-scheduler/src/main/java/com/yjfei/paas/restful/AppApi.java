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
import com.yjfei.paas.entity.AppEntity;
import com.yjfei.paas.service.AppService;
import com.yjfei.paas.vo.AppVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/app")
@Slf4j
public class AppApi {

    @Autowired
    private AppService appService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResult<List<AppVO>> queryApp(@RequestParam(value = "field", defaultValue = "appId") String field,
                                            @RequestParam(value = "keyword", defaultValue = "%") String value,
                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int count) {
        PageResult<List<AppVO>> result = new PageResult<List<AppVO>>();
        try {
            Page<AppEntity> pageResult = appService.queryAppByField(field, value, page, count);
            List<AppVO> list = Lists.newArrayList();
            for (AppEntity entity : pageResult.getContent()) {
                list.add(new AppVO(entity));
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
    public Result<String> addApp(@RequestBody AppVO vo) {
        Result<String> result = new Result<String>();
        try {
            AppEntity app = vo.buildAppEntity();
            app.setCreateTime(new Date());
            app.setModifyTime(new Date());
            appService.addApp(app);
            result.setData("OK");
        } catch (Throwable t) {
            log.error("addApp error!", t);
            result.setSuccess(false);
            result.setMessage(t.getMessage());
        }
        return result;
    }

    /**
     * 在不改变当前service写法的前提下，
     * 和add传入的vo实例相比，
     * update的vo中createTime和id不得为空。
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<String> updateApp(@RequestBody AppVO vo) {
        Result<String> result = new Result<String>();
        try {
            AppEntity app = vo.buildAppEntity();
            app.setModifyTime(new Date());
            appService.updateApp(app);
            result.setData("OK");
        } catch (Throwable t) {
            log.error("updateApp error!", t);
            result.setSuccess(false);
            result.setMessage(t.getMessage());
        }
        return result;
    }

}
