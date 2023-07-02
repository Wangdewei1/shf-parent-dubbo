package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.HouseImage;

import java.util.List;

/**
 * 房源图片 业务接口
 */
public interface HouseImageService extends BaseService<HouseImage> {
    List<HouseImage> findHouseImageList(Long houseId, Integer type);
}
