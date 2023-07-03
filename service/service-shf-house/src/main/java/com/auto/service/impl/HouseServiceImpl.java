package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.entity.House;
import com.auto.entity.vo.HouseQueryVo;
import com.auto.entity.vo.HouseVo;
import com.auto.mapper.HouseMapper;
import com.auto.service.HouseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 房源
 */
@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
    @Autowired
    private HouseMapper houseMapper;

    @Override
    public BaseMapper<House> getBaseMapper() {
        return houseMapper;
    }

    /**
     * 根据id查询房源的数量
      * @param id
     * @return
     */
    @Override
    public Long findHouseCountByCommunityId(Long id) {
        return houseMapper.findHouseCountByCommunityId(id);
    }

    /**
     * 根据 页码 每页显示数 查询房源信息
     * @param pageNum
     * @param pageSize
     * @param houseQueryVo
     * @return
     */
    @Override
    public PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {

        //1.开启分页
        PageHelper.startPage(pageNum, pageSize);

        //2.查询房源分页信息
        List<HouseVo> page = houseMapper.findListPage(houseQueryVo);

        return new PageInfo<>(page);
    }
}
