package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.House;
import com.auto.entity.UserInfo;
import com.auto.entity.pojo.ResultHouseInfo;
import com.auto.entity.vo.HouseQueryVo;
import com.auto.entity.vo.HouseVo;
import com.github.pagehelper.PageInfo;

public interface HouseService extends BaseService<House> {
    Long findHouseCountByCommunityId(Long id);

    PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo);

    ResultHouseInfo findHouseInfos(Long houseId , UserInfo userInfo);
}
