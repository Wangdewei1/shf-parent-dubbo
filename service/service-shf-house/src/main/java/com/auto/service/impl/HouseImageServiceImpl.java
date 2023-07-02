package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.HouseImage;
import com.auto.mapper.HouseImageMapper;
import com.auto.service.HouseImageService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 房源图片 业务逻辑
 */
@Service(interfaceClass = HouseImageService.class)
public class HouseImageServiceImpl extends BaseServiceImpl<HouseImage> implements HouseImageService{
    @Autowired
    private HouseImageMapper houseImageMapper;

    @Override
    public BaseMapper<HouseImage> getBaseMapper() {
        return houseImageMapper;
    }

    @Override
    public List<HouseImage> findHouseImageList(Long houseId, Integer type) {
        return houseImageMapper.findHouseImageList(houseId,type);
    }
}
