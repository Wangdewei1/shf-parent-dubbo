package com.auto.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * aspect redis缓存 切面 配置类
 */
@Aspect
@Component
public class RedisCatchAspect {
    //注入redisPool
    @Autowired
    private JedisPool jedisPool;

    //配置切入点
    @Pointcut("@annotation(com.auto.anno.RedisCatch)")
    private void redisCatchPointCut(){

    }

    @Around(value = "redisCatchPointCut()")
    public Object aroundRedisCatch(ProceedingJoinPoint proceedingJoinPoint){
        //获取jedis对象
        Jedis jedis = jedisPool.getResource();
        //1.目标方法执行之前，先从缓存中查询数据，并判断缓存中是否有数据

        //1.1获取key

        //1.1.1获取目标方法的RedisCatch注解

        //1.1.2获取目标方法的方法名

        //1.1.3获取目标方法的第一个参数（如果有的话），目的是保证存入redis缓存中的key不同

        //1.1.4根据key获取redis中的值

        //2.判断获取的值是否为空或空字符串

        //2.1缓存中如果没有，则执行目标方法查询数据

        //2.2将获取到的数据转成JSON字符串，并存入redis中

        //2.2.1保证redis中，不会出现大量的缓存数据，同时过期

        //2.2.2并设置新的key和过期时间

        //3.如果缓存中有数据，则直接将缓存中的数据转成对象，并返回

        //3.1获取目标方法的返回值类型 methodSignature.getReturnType()

        //3.2判断他是否为String类型

        //3.2.1如果是String类型，则直接返回获取的值value

        //3.3判断它是否是List，并可以确定类型 通过反射的方式 methodSignature.getMethod().getGenericReturnType()

        //3.3.1判断这个返回值类型  是否是 ParameterizedType TODO 查找的这是什么类型

        //3.3.1.1如果是这个 ParameterizedType 类型，则将返回值强转成 ParameterizedType

        //3.3.1.2获取List的泛型参数

        //3.3.1.2.1 通过type.getActualTypeArguments()[0] 确定返回值的泛型 TODO 这个方法的具体含义

        //3.4返回结果 ，注意将获取的value值转成JSON数组 ，并且将返回值的类型强转 成类类型

        //4.否则返回的是对象类型

        //4.1将value值转成对象，并返回 JSON.parseObject(value, returnType);

        //5.记得要把jedis关闭 close





        return null;
    }


}
