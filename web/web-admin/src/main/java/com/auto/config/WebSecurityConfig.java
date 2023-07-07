package com.auto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * springSecurity 配置类
 */
@Configuration
@EnableWebSecurity  //默认开启springSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;


    /**
     * 重写 配置
     * AuthenticationManagerBuilder
     * 是 Spring Security 框架中的一个类，用于配置身份验证（Authentication）的管理器（Manager）。
     *
     * AuthenticationManagerBuilder 提供了一种简化配置身份验证的方式，它允许开发人员定义和定制如何验证用户的身份
     *
     * 通过使用 AuthenticationManagerBuilder，可以在 Spring Security 配置类中定义身份验证规则、用户存储方式、密码编码策略等。
     *
     *
     *
     * @param auth
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //配置 在内存中用户名 及 密码
//        //注意 密码不可以是明文  需要加密  框架内置很多加密方式
//        auth.inMemoryAuthentication()
//                .withUser("zhangsan")
//                .password(passwordEncoder.encode("12345678"))
//                .authorities(new ArrayList());
////        super.configure(auth);
//    }

    /**
     * security 登入后 不允许 页面嵌套
     *
     * 在给定的代码中，http 变量是 HttpSecurity 类型的实例。它可能会在 WebSecurityConfigurerAdapter 类的方法中进行初始化和配置，以定义应用程序的安全设置。
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //1.调用父类
//        super.configure(http);
        //2.http 允许放行后 页面嵌套
        http.headers().frameOptions().disable();
        //3.http 自定义页面 允许匿名用户访问资源
        //3.1 静态资源 允许我们访问自定义的login.html页面 antMatchers 是用于定义请求路径的匹配规则，并为这些路径配置安全规则
        //注意 因为登陆页面定义在WEB-INF中，是不能在浏览器直接访问的，所以这是 在WEB-INF中的请求 ，要想让springSecurity匿名用户有权限访问，需要访问的是请求url
        http.authorizeRequests().antMatchers("/static/**","/loginPage").permitAll()
                //其他页面全部需要权限校验
                .anyRequest().authenticated()
                //3.2配置跳转自定义登陆页面
                .and().formLogin().loginPage("/loginPage")
                //认证成功后默认访问 第一次访问的路径
                .defaultSuccessUrl("/")
                //配置处理登录请求的url,其实就是登录页面的form表单的action属性
                .loginProcessingUrl("/login")
                //3.3配置注销登录
                .and().logout().logoutUrl("/logout")
                //用户退出后，要被重定向到登录页
                .logoutSuccessUrl("/loginPage");

        //关闭跨域请求伪造
        http.csrf().disable();

        //配置访问拒绝，处理器
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }



}
