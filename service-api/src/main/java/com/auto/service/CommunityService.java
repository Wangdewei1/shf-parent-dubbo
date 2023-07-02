package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.Community;

import java.util.List;

public interface CommunityService extends BaseService<Community> {
    //根据用户小区id获取详情
    List<Community> findCommunityList(Long communityId);
}
