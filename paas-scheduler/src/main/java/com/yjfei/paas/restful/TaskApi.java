package com.yjfei.paas.restful;


import com.google.common.collect.Lists;
import com.yjfei.paas.common.Result;
import com.yjfei.paas.entity.DeployTaskEntity;
import com.yjfei.paas.service.ScheduleTaskService;
import com.yjfei.paas.vo.TaskVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/task")
@Slf4j
public class TaskApi {

    private static List<Integer> status;

    static {
        status = Lists.newArrayList();
        status.add(0);
        status.add(1);
        status.add(2);
        status.add(3);
    }

    @Autowired
    private ScheduleTaskService scheduleTaskService;

    /**
     * @param: appId
     * @return: Result<List<TaskVO>>
     * @Description: we use the appId to query the alive tasks with the task status which are [0,1,2,3]
     */
    @ResponseBody
    @RequestMapping(value = "/{appId}/alive", method = RequestMethod.GET)
    public Result<List<TaskVO>> queryAliveTask(@PathVariable String appId) {

        Result<List<TaskVO>> result = new Result<List<TaskVO>>();
        try {

            List<DeployTaskEntity> entitylist = scheduleTaskService.findByAppIdAndStatusIn(appId, status);
            List<TaskVO> taskVOList = Lists.newArrayList();
            for (DeployTaskEntity entity : entitylist) {
                taskVOList.add(new TaskVO(entity));
            }
            result.setData(taskVOList);
        } catch (Throwable t) {
            log.error("queryAliveTask error!", t);
            result.setSuccess(false);
            result.setMessage(t.getMessage());
        }
        return result;
    }


    /**
     * @param: deployId
     * @return: Result<List<TaskVO>>
     * @Description: we use the deployId to query the all task which have been deployed
     */
    @ResponseBody
    @RequestMapping(value = "/{deployId}/history", method = RequestMethod.GET)
    public Result<List<TaskVO>> queryTaksbyDeployId(@PathVariable long deployId) {

        Result<List<TaskVO>> result = new Result<List<TaskVO>>();
        try {
            List<DeployTaskEntity> entitylist = scheduleTaskService.findByDeployId(deployId);
            List<TaskVO> taskVOList = Lists.newArrayList();
            for (DeployTaskEntity entity : entitylist) {
                taskVOList.add(new TaskVO(entity));
            }
            result.setData(taskVOList);
        } catch (Throwable t) {
            log.error("queryTaksbyDeployId error!", t);
            result.setSuccess(false);
            result.setMessage(t.getMessage());
        }
        return result;
    }
}
