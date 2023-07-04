package com.auto.interceptor;

import com.alibaba.fastjson.JSON;
import com.auto.constant.LoginUserInfoConstant;
import com.auto.result.Result;
import com.auto.result.ResultCodeEnum;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //判断session域中是否有用户信息
        if (request.getSession().getAttribute(LoginUserInfoConstant.LOGIN_USER_INFO) == null) {
            response.getWriter().write(JSON.toJSONString(Result.build(null, ResultCodeEnum.LOGIN_AUTH)));
            return false;
        }
        //否则用户已登录
        return true;
    }
}
