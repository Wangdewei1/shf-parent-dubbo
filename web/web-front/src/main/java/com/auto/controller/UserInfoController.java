package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.RedisConstant;
import com.auto.en.UserLoginStatusLocked;
import com.auto.entity.UserInfo;
import com.auto.entity.vo.LoginVo;
import com.auto.entity.vo.RegisterVo;
import com.auto.result.Result;
import com.auto.result.ResultCodeEnum;
import com.auto.service.UserInfoService;
import com.auto.util.CastUtil;
import com.auto.util.MD5;
import com.auto.util.VerificationUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

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
        //检查当前手机号是否已经发送过验证码，并且发送时间是否在一分钟之内
        Long ttl = jedis.ttl(phone);
        //判断是否在一分钟之内
        if (ttl > RedisConstant.DEFAULT_SEND_VERIFICATION_TIME - RedisConstant.DEFAULT_SEND_VERIFICATION_TIME_INTERVAL){
            return Result.build(null,ResultCodeEnum.NOT_SEND_MORE_THAN_VERIFICATION_TWICE);
        }
        //拼接当前的年月日
        Calendar calendar = Calendar.getInstance();

        String codeCountKey = phone + RedisConstant.DEFAULT_SEND_VERIFICATION_PHONE_KEY_STR +
                RedisConstant.DEFAULT_COLON + calendar.get(Calendar.YEAR) +
                RedisConstant.DEFAULT_COLON + calendar.get(Calendar.MONTH) +
                RedisConstant.DEFAULT_COLON + calendar.get(Calendar.DAY_OF_MONTH);

        //检查当前手机号是否发送超过五次 给默认值0
        int count = CastUtil.castInt(jedis.get(codeCountKey), 0);

        //判断当前次数
        if (count == RedisConstant.DEFAULT_SEND_VERIFICATION_COUNT){
            return Result.build(null,ResultCodeEnum.MESSAGE_SEND_MAX_COUNT);
        }

        try {
            //1.获取4位随机验证码
            String verificationCode = VerificationUtil.getVerificationCode();

//            logger.debug(verificationCode);
            //将这个手机号今天你发送的验证码储存到redis中（并做过期处理）

            //发送次数
            jedis.incr(verificationCode);

            //今天第一次发验证码的时候，设置过期时间
            if (count == 0){
                jedis.expire(codeCountKey, RedisConstant.DEFAULT_SEND_VERIFICATION_TIME_EXPIRE);
            }

            //2.将验证码和随机验证码
            //将手机号和验证码存到redis中
            jedis.setex(phone + RedisConstant.DEFAULT_SEND_VERIFICATION_PHONE_KEY_STR, RedisConstant.DEFAULT_SEND_VERIFICATION_TIME, verificationCode);

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
            String checkCode = jedis.get(registerVo.getPhone() + RedisConstant.DEFAULT_SEND_VERIFICATION_PHONE_KEY_STR);
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

    /**
     * 登录功能
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo, HttpSession session){

        //1.根据手机号获取UserInfo对象
        UserInfo userInfo = userInfoService.findByPhone(loginVo.getPhone());

        //将userInfo对象存到session中
        session.setAttribute("USER", userInfo);

        return userInfoService.login(loginVo,userInfo);
    }

}
