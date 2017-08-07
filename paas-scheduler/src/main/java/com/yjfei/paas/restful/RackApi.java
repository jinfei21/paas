package com.yjfei.paas.restful;

import com.google.common.collect.Lists;
import com.yjfei.paas.common.PageResult;
import com.yjfei.paas.common.Result;
import com.yjfei.paas.entity.RackEntity;
import com.yjfei.paas.service.RackService;
import com.yjfei.paas.vo.RackVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/rack")
@Slf4j
public class RackApi {

    @Autowired
    private RackService rackService;

    @ResponseBody
    @RequestMapping(value = "/status/{rackId}/up", method = RequestMethod.GET)
    public Result<RackVO> rackUp(@PathVariable("rackId") String rackId) {
        Result<RackVO> result = new Result<>();
        try {
            RackEntity rackEntity = rackService.statusShift(rackId, true);
            rackEntity.setModifyTime(new Date());
            result.setData(new RackVO(rackEntity));
            result.setMessage("Succeeded on Rack Up operation.");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed on Rack Up operation.", e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/status/{rackId}/down", method = RequestMethod.GET)
    public Result<RackVO> rackDown(@PathVariable(value = "rackId") String rackId) {
        Result<RackVO> result = new Result<>();
        try {
            RackEntity rackEntity = rackService.statusShift(rackId, false);
            rackEntity.setModifyTime(new Date());
            result.setData(new RackVO(rackEntity));
            result.setMessage("Succeeded on Rack Down operation.");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed on Rack Down operation.", e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<RackVO>> queryRack() {
        Result<List<RackVO>> result = new Result<>();
        List<RackVO> list = Lists.newArrayList();
        try {
            List<RackEntity> rackEntityList = rackService.listRack();
            for (RackEntity rackEntity : rackEntityList) {
                list.add(new RackVO(rackEntity));
            }
            result.setData(list);
            result.setMessage("Succeeded on rack_query operation.");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed on rack_query operation. ", e);
            result.setSuccess(false);
            result.setMessage("Failed on rack_add operation. " + e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<String> addRack(@RequestBody RackVO vo) {
        Result<String> result = new Result<String>();
        try {
            RackEntity rackEntity = vo.buildRackEntity();
            rackEntity.setCreateTime(new Date());
            rackEntity.setModifyTime(new Date());
            rackService.addRack(rackEntity);
            result.setMessage("Succeeded on rack_add operation.");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed on rack_add operation. ", e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
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
    public Result<String> updateRack(@RequestBody RackVO vo) {
        Result<String> result = new Result<String>();
        try {
            RackEntity slave = vo.buildRackEntity();
            slave.setModifyTime(new Date());
            rackService.addRack(slave);
            result.setMessage("Succeeded on slave_update operation.");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed on slave_update operation.", e);
            result.setSuccess(false);
            result.setMessage("Failed on slave_update operation. " + e.getMessage());
        }
        return result;
    }
}
