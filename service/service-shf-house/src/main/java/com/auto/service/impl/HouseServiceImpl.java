package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.auto.base.BaseMapper;
import com.auto.base.BaseServiceImpl;
import com.auto.en.HouseImageType;
import com.auto.entity.*;
import com.auto.entity.pojo.ResultHouseInfo;
import com.auto.entity.vo.HouseQueryVo;
import com.auto.entity.vo.HouseVo;
import com.auto.mapper.*;
import com.auto.service.HouseService;
import com.auto.service.UserFollowService;
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

    @Autowired
    private HouseImageMapper houseImageMapper;

    @Autowired
    private HouseBrokerMapper houseBrokerMapper;

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private HouseUserMapper houseUserMapper;

    @Reference
    private UserFollowService userFollowService;

    @Override
    public BaseMapper<House> getBaseMapper() {
        return houseMapper;
    }

    /**
     * 根据id查询房源的数量
     *
     * @param id
     * @return
     */
    @Override
    public Long findHouseCountByCommunityId(Long id) {
        return houseMapper.findHouseCountByCommunityId(id);
    }

    /**
     * 根据 页码 每页显示数 查询房源信息
     *
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

    /**
     * 封装房源详情的方法
     *
     * @param houseId
     * @return
     */
    @Override
    public ResultHouseInfo findHouseInfos(Long houseId , UserInfo userInfo) {

        //1.根据houseId获取小区信息
        House house = houseMapper.getById(houseId);
        //2.根据房源id和小区类型获取房源图片信息
        List<HouseImage> houseImage1List = houseImageMapper.findHouseImageList(houseId, HouseImageType.HOUSE_IMAGE_TYPE.getType());
        //3.根据房源id查询经纪人列表
        List<HouseBroker> houseBrokerList = houseBrokerMapper.findHouseBrokerList(houseId);
        //4.根据用户id查询房东信息
        List<HouseUser> houseUserList = houseUserMapper.findHouseUserList(houseId);
        //根据小区获取小区信息
        Community community = communityMapper.getById(house.getCommunityId());
        //返回封装房源详情的结果

        //是否关注 默认
        boolean isFollow = false;
        //如果当前没有登陆，isFollow一定为false，未关注
        if (userInfo != null){
            UserFollow userFollow = userFollowService.findUserFollowByUserIdAndHouseId(houseId, userInfo);
            isFollow = (userFollow != null && userFollow.getIsDeleted() == 0);
        }

        return new ResultHouseInfo(house, community, houseBrokerList, houseImage1List, houseUserList,isFollow);
    }
}
