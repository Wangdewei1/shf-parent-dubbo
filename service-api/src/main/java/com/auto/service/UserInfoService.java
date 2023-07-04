package com.auto.service;

import com.auto.base.BaseService;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.LoginVo;
import com.auto.entity.vo.RegisterVo;
import com.auto.result.Result;

import javax.servlet.http.HttpSession;

/**
 * 用户相关
 */
public interface UserInfoService extends BaseService<UserInfo> {
    Result register(RegisterVo registerVo);

    Result login(LoginVo loginVo , UserInfo userInfo);

    UserInfo findByPhone(String phone);
}
