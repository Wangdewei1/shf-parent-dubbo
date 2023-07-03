package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.RedisConstant;
import com.auto.entity.vo.RegisterVo;
import com.auto.result.Result;
import com.auto.result.ResultCodeEnum;
import com.auto.service.UserInfoService;
import com.auto.util.VerificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 用户相关 功能
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    private UserInfoService userInfoService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送验证码
     */
    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone){

        Logger logger = LoggerFactory.getLogger(UserInfoController.class);

        Jedis jedis = jedisPool.getResource();
        try {
            //1.获取4位随机验证码
            String verificationCode = VerificationUtil.getVerificationCode();
            logger.debug(verificationCode);
            //2.将验证码和随机验证码
            //将手机号和验证码存到redis中
            jedis.setex(phone, RedisConstant.DEFAULT_SEND_VERIFICATION_TIME, verificationCode);

            return Result.ok();

        } finally {
            jedis.close();
        }
    }

    /**
     * 注册功能
     */
    @PostMapping("/register")
    public Result registerUser(@RequestBody RegisterVo registerVo){
        Jedis jedis = jedisPool.getResource();
        try {
            //校验验证码是否正确

            //1.获取存到redis中的验证码 根据键 手机号
            String checkCode = jedis.get(registerVo.getPhone());
            //判断验证码是否正确
            if (!registerVo.getCode().equals(checkCode)){
                //提示返回错误信息
                return Result.build(null, ResultCodeEnum.CODE_ERROR);
            }
            //2.判断验证码成功后，要删除redis中的验证码
            jedis.del(registerVo.getPhone());
            return userInfoService.register(registerVo);
        } finally {
            jedis.close();
        }
    }

}
