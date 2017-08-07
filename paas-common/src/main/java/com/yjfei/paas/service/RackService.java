package com.yjfei.paas.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.yjfei.paas.common.MachineState;
import com.yjfei.paas.dao.RackRepository;
import com.yjfei.paas.data.Rack;
import com.yjfei.paas.entity.RackEntity;
import com.yjfei.paas.util.ConvertUtil;

@Service
public class RackService {

    @Autowired
    private RackRepository rackRepository;


    public List<RackEntity> listRack() {
        return rackRepository.findAll(new Sort(Direction.DESC, "id"));
    }

    public void addRack(RackEntity rack) {
        this.rackRepository.save(rack);
    }

    public void updateRack(RackEntity rack) {
        this.rackRepository.saveAndFlush(rack);
    }

    public List<Rack> listRack(MachineState state) {
        List<RackEntity> entityList = rackRepository.findByStatus(state.getCode());

        List<Rack> rackList = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(entityList)) {

            rackList = ConvertUtil.convert(entityList,
                    entity -> {
                        Rack rack = ConvertUtil.convert(entity, Rack.class);
                        rack.setStatus(MachineState.getStateByCode(entity.getStatus()));
                        return rack;
                    });
        }

        return rackList;
    }

    public boolean isDecomission(String rackId) {
        RackEntity entity = rackRepository.findByRackId(rackId);
        if (entity != null) {
            return entity.getStatus() == MachineState.RACK_DECOMISSION.getCode();
        }
        return false;
    }

    /**
     * Shift the status of rack.
     *
     * @params  rackId
     * @params  upOrDown: true->up,false->down;
     *
     * @throws
     *      1、 已经上线的rack再进行上线操作会报出异常:"There is no need to shift the rack status again."
     *          状态为下线的rack同理。
     *      2、 输入一个不存在的rackId进行上下线操作会报出异常："Can't find the Rack you want."
     *
     */
    public RackEntity statusShift(String rackId, boolean upOrDown) throws Exception {
        RackEntity rackEntity = rackRepository.findByRackId(rackId);
        if (rackEntity != null) {
            if (rackEntity.getStatus() == (upOrDown ? 1 : 3))
                throw new Exception("There is no need to shift the status again.");
            if (upOrDown) {
                rackEntity.setStatus(MachineState.UP.getCode());
            } else {
                rackEntity.setStatus(MachineState.RACK_DECOMISSION.getCode());
            }
        } else {
            throw new Exception("Can't find the Rack you want.");
        }
        rackRepository.saveAndFlush(rackEntity);
        return rackEntity;
    }
}
