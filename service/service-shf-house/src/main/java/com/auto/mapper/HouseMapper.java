package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.House;
import com.auto.entity.vo.HouseQueryVo;
import com.auto.entity.vo.HouseVo;

import java.util.List;

public interface HouseMapper extends BaseMapper<House> {
    Long findHouseCountByCommunityId(Long id);

    List<HouseVo> findListPage(HouseQueryVo houseQueryVo);
}
