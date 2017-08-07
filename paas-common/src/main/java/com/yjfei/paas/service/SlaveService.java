package com.yjfei.paas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.dao.SlaveRepository;
import com.yjfei.paas.data.Rack;
import com.yjfei.paas.data.Slave;
import com.yjfei.paas.entity.SlaveEntity;
import com.yjfei.paas.util.ConvertUtil;

@Service
public class SlaveService {

    @Autowired
    private SlaveRepository slaveRepository;

    public List<SlaveEntity> listSlave() {
        return slaveRepository.findAll(new Sort(Direction.DESC, "id"));
    }

    public void addSlave(SlaveEntity slave) {
        this.slaveRepository.save(slave);
    }

    public void updateSlave(SlaveEntity slave) {
        this.slaveRepository.saveAndFlush(slave);
    }

    public List<Slave> listSlave(MachineState state) {
        List<SlaveEntity> entityList = slaveRepository.findByStatus(state.getCode());

        List<Slave> slaveList = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(entityList)) {

            slaveList = ConvertUtil.convert(entityList, entity -> {
                Slave slave = ConvertUtil.convert(entity, Slave.class);
                slave.setStatus(MachineState.getStateByCode(entity.getStatus()));
                return slave;
            });
        }

        return slaveList;
    }

    public List<Slave> listSlave(Rack rack) {
        List<SlaveEntity> entityList = slaveRepository.findByRackId(rack.getRackid());
        List<Slave> slaveList = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(entityList)) {

            slaveList = ConvertUtil.convert(entityList, entity -> {
                Slave slave = ConvertUtil.convert(entity, Slave.class);
                slave.setStatus(MachineState.getStateByCode(entity.getStatus()));
                return slave;
            });
        }

        return slaveList;
    }

    public boolean isDecomission(String slaveId) {
        SlaveEntity entity = slaveRepository.findBySlaveId(slaveId);
        if (entity != null) {
            return entity.getStatus() == MachineState.SLAVE_DECOMISSION.getCode();
        }
        return false;
    }


    /**
     * Shift the status of slave.
     *
     * @params  slaveId
     * @params  upOrDown: true->up,false->down;
     *
     * @throws
     *      1、 已经上线的slave再进行上线操作会报出异常:"There is no need to shift the status again."
     *          状态为下线的slave同理。
     *      2、 输入一个不存在的slaveId进行上下线操作会报出异常："Can't find the Slave you want."
     */
    public SlaveEntity statusShift(String slaveId, boolean upOrDown) throws Exception {
        SlaveEntity slaveEntity = slaveRepository.findBySlaveId(slaveId);
        if (slaveEntity != null) {
            if (slaveEntity.getStatus() == (upOrDown ? 1 : 2))
                throw new Exception("There is no need to shift the status again.");
            if (upOrDown) {
                slaveEntity.setStatus(MachineState.UP.getCode());
            } else {
                slaveEntity.setStatus(MachineState.SLAVE_DECOMISSION.getCode());
            }
        } else {
            throw new Exception("Can't find the Slave you want.");
        }
        slaveRepository.saveAndFlush(slaveEntity);
        return slaveEntity;
    }
}
