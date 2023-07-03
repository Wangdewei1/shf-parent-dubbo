package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.RegisterVo;
import com.auto.result.Result;

/**
 * 用户相关
 */
public interface UserInfoService extends BaseService<UserInfo> {
    Result register(RegisterVo registerVo);
}
