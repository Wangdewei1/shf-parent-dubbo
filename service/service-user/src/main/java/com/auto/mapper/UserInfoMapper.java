package com.auto.mapper;

import com.auto.base.BaseMapper;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.RegisterVo;

/**
 * 用户相关 持久层
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    void register(RegisterVo registerVo);

    UserInfo findByPhone(String phone);

    UserInfo findByNickName(String nickName);
}
