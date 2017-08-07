package com.yjfei.paas.restful;

import com.google.common.collect.Lists;
import com.yjfei.paas.common.PageResult;
import com.yjfei.paas.common.Result;
import com.yjfei.paas.entity.SlaveEntity;
import com.yjfei.paas.service.SlaveService;
import com.yjfei.paas.vo.RackVO;
import com.yjfei.paas.vo.SlaveVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/slave")
@Slf4j
public class SlaveApi {

    @Autowired
    private SlaveService slaveService;

    @ResponseBody
    @RequestMapping(value = "/status/{slaveId}/up", method = RequestMethod.GET)
    public Result<SlaveVO> slaveUp(@PathVariable("slaveId") String slaveId) {
        Result<SlaveVO> result = new Result<>();
        try {
            SlaveEntity slaveEntity = slaveService.statusShift(slaveId, true);
            slaveEntity.setModifyTime(new Date());
            result.setData(new SlaveVO(slaveEntity));
            result.setMessage("Succeeded on Slave Up operation.");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed on Slave Up operation. ", e);
            result.setSuccess(false);
            result.setMessage("Failed on Slave Up operation. " + e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/status/{slaveId}/down", method = RequestMethod.GET)
    public Result<SlaveVO> slaveDown(@PathVariable("slaveId") String slaveId) {
        Result<SlaveVO> result = new Result<>();
        try {
            SlaveEntity slaveEntity = slaveService.statusShift(slaveId, false);
            slaveEntity.setModifyTime(new Date());
            result.setData(new SlaveVO(slaveEntity));
            result.setMessage("Succeeded on Slave Down operation.");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed on Slave Down operation. ", e);
            result.setSuccess(false);
            result.setMessage("Failed on Slave Down operation. "+e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<SlaveVO>> querySlave() {
        Result<List<SlaveVO>> result = new Result<>();
        List<SlaveVO> list = Lists.newArrayList();
        try {
            List<SlaveEntity> slaveEntityList = slaveService.listSlave();
            for (SlaveEntity slaveEntity : slaveEntityList) {
                list.add(new SlaveVO(slaveEntity));
            }
            result.setData(list);
            result.setMessage("Succeeded on slave_query operation.");
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("Failed on slave_query operation.",e);
            result.setSuccess(false);
            result.setMessage("Failed on slave_add operation." + e.getMessage());
        }
        return result;
    }

    @ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<String> addSlave(@RequestBody SlaveVO vo) {
		Result<String> result = new Result<String>();
		try {
			SlaveEntity slave = vo.buildSlaveEntity();
			slave.setCreateTime(new Date());
			slave.setModifyTime(new Date());
			slaveService.addSlave(slave);
			result.setMessage("Succeeded on slave_add operation.");
            result.setSuccess(true);
		} catch (Exception e) {
			log.error("Failed on slave_add operation.", e);
			result.setSuccess(false);
			result.setMessage("Failed on slave_add operation. " + e.getMessage());
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
    public Result<String> updateApp(@RequestBody SlaveVO vo) {
        Result<String> result = new Result<String>();
        try {
            SlaveEntity slave = vo.buildSlaveEntity();
            slave.setModifyTime(new Date());
            slaveService.addSlave(slave);
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
