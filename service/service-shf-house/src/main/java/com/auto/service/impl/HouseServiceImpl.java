package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.House;
import com.auto.mapper.HouseMapper;
import com.auto.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
    @Autowired
    private HouseMapper houseMapper;

    @Override
    public BaseMapper<House> getBaseMapper() {
        return houseMapper;
    }

}
