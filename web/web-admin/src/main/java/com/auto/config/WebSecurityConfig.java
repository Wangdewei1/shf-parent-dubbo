package com.auto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * springSecurity 配置类
 */
@Configuration
@EnableWebSecurity  //默认开启springSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

}
