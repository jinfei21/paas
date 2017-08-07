package com.yjfei.paas.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.yjfei.paas.common.PaasTaskState;
import com.yjfei.paas.dao.DeployTaskRepository;
import com.yjfei.paas.data.ScheduleTask;
import com.yjfei.paas.entity.DeployTaskEntity;
import com.yjfei.paas.util.ConvertUtil;

@Service
public class ScheduleTaskService {

    @Autowired
    private DeployTaskRepository deployTaskRepository;

    public List<DeployTaskEntity> listDeployTask() {
        return this.deployTaskRepository.findAll();
    }

    public List<DeployTaskEntity> listByStatusIn(Collection<Integer> status) {
        return this.deployTaskRepository.findByStatusIn(status);
    }

    public List<DeployTaskEntity> listDeployTaskByStatus(int status) {
        return this.deployTaskRepository.findByStatus(status);
    }

    public List<DeployTaskEntity> findByDeployId(long deployId){
        return  deployTaskRepository.findByDeployId(deployId);
    }

    public List<DeployTaskEntity> findByAppIdAndStatusIn(String appId, Collection<Integer> status) {
        return this.deployTaskRepository.findByAppIdAndStatusIn(appId, status);
    }

    public List<ScheduleTask> listScheduleTask(PaasTaskState... states) {

        List<Integer> statusList = Lists.newArrayList();

        for (PaasTaskState state : states) {
            statusList.add(state.getCode());
        }

        List<DeployTaskEntity> entityList = this.deployTaskRepository.findByStatusIn(statusList);

        List<ScheduleTask> taskList = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(entityList)) {

            taskList = ConvertUtil.convert(entityList, entity -> {
                ScheduleTask task = ConvertUtil.convert(entity, ScheduleTask.class);
                task.setStatus(PaasTaskState.getStateByCode(entity.getStatus()));
                return task;
            });
        }

        return taskList;
    }

    public List<ScheduleTask> listScheduleTaskByDeployId(long deployId) {

        List<DeployTaskEntity> entityList = this.deployTaskRepository.findByDeployId(deployId);
        List<ScheduleTask> taskList = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(entityList)) {

            taskList = ConvertUtil.convert(entityList, entity -> {
                ScheduleTask task = ConvertUtil.convert(entity, ScheduleTask.class);
                task.setStatus(PaasTaskState.getStateByCode(entity.getStatus()));
                return task;
            });
        }
        return taskList;

    }

    public List<ScheduleTask> listScheduleTaskByDeployId(long deployId, PaasTaskState... statuss) {

        List<Integer> statusList = Lists.newArrayList();
        for (PaasTaskState status : statuss) {
            statusList.add(status.getCode());
        }

        List<DeployTaskEntity> entityList = this.deployTaskRepository.findByDeployIdAndStatusIn(deployId, statusList);
        List<ScheduleTask> taskList = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(entityList)) {

            taskList = ConvertUtil.convert(entityList, entity -> {
                ScheduleTask task = ConvertUtil.convert(entity, ScheduleTask.class);
                task.setStatus(PaasTaskState.getStateByCode(entity.getStatus()));
                return task;
            });
        }
        return taskList;

    }

    public void createScheduleTask(ScheduleTask scheduleTask) {
        DeployTaskEntity entity = ConvertUtil.convert(scheduleTask, DeployTaskEntity.class);

        entity.setStatus(scheduleTask.getStatus().getCode());

        this.deployTaskRepository.save(entity);
    }

    public void createScheduleTasks(List<ScheduleTask> scheduleTasks) {
        List<DeployTaskEntity> entityList = ConvertUtil.convert(scheduleTasks, scheduleTask -> {
            DeployTaskEntity entity = ConvertUtil.convert(scheduleTask, DeployTaskEntity.class);

            entity.setStatus(scheduleTask.getStatus().getCode());
            return entity;
        });

        this.deployTaskRepository.save(entityList);
    }

    @Transactional
    public void updateScheduleTaskStatus(long id, PaasTaskState status) {
        this.deployTaskRepository.updateStatusById(id, status.getCode(), new Date());
    }


    public void updateScheduleTask(ScheduleTask scheduleTask) {
        DeployTaskEntity entity = ConvertUtil.convert(scheduleTask, DeployTaskEntity.class);

        entity.setStatus(scheduleTask.getStatus().getCode());
        entity.setModifyTime(new Date());

        this.deployTaskRepository.saveAndFlush(entity);
    }

    public Optional<ScheduleTask> findScheduleTaskById(long id) {

        DeployTaskEntity entity = deployTaskRepository.findOne(id);
        if (entity != null) {
            ScheduleTask task = ConvertUtil.convert(entity, ScheduleTask.class);
            task.setStatus(PaasTaskState.getStateByCode(entity.getStatus()));
            return Optional.of(task);
        }
        return Optional.<ScheduleTask>absent();
    }
}
