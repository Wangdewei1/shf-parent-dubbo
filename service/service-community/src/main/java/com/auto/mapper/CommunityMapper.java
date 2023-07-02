package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.Community;

import java.util.List;

public interface CommunityMapper extends BaseMapper<Community> {
    List<Community> findCommunityList(Long communityId);
}
