package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseImageMapper extends BaseMapper<HouseImage> {
    List<HouseImage> findHouseImageList(@Param("houseId") Long houseId,@Param("type") Integer type);
}
