package com.auto.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限访问 处理
 */
@Configuration
@Component
public class ShfAccessDeniedHandler implements AccessDeniedHandler {
    //配置日志
    Logger logger = LoggerFactory.getLogger(ShfAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        //1.获取登入的对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("{}没有权限,访问{}时被拒绝",user.getUsername(),httpServletRequest.getRequestURL());
        //2.重定向权限不足页面请求
        httpServletResponse.sendRedirect("/auth");
    }
}
