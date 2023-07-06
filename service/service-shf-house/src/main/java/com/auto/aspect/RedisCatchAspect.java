package com.auto.aspect;

import com.alibaba.fastjson.JSON;
import com.auto.anno.RedisCatch;
import com.auto.constant.RedisConstant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * aspect redis缓存 切面 配置类
 *
 *   含义：
 *   ParameterizedType接口是java.lang.reflect包中的一部分，用于提供有关参数化类型的信息。它是Type接口的子接口，表示具有实际类型参数的参数化类型
 *   具体方法：
 *   Type[] getActualTypeArguments(): 返回参数化类型的实际类型参数的数组。例如，对于List<String>，该方法将返回一个包含String的Type数组。
 *
 *   Type getRawType(): 返回原始类型（非参数化类型）的Type。例如，对于List<String>，该方法将返回List的Type。
 *
 *   Type getOwnerType(): 返回拥有此参数化类型的类型的Type。如果参数化类型是一个嵌套类型，那么该方法将返回其外部类的Type。否则，如果参数化类型是顶层类型，那么该方法将返回null
 *
 *  含义：ParameterizedType对象中获取实际类型参数的第一个类型。
 *
 *  解释：getActualTypeArguments()是ParameterizedType接口定义的方法之一。它返回一个Type数组，该数组包含了参数化类型的实际类型参数
 */
@Aspect
@Component
public class RedisCatchAspect {
    //注入redisPool
    @Autowired
    private JedisPool jedisPool;

    //配置切入点
    @Pointcut("@annotation(com.auto.anno.RedisCatch)")
    private void redisCatchPointCut() {

    }

    @Around(value = "redisCatchPointCut()")
    public Object aroundRedisCatch(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //获取jedis对象
        Jedis jedis = jedisPool.getResource();

        //获取切入点方法
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        try {
            //1.目标方法执行之前，先从缓存中查询数据，并判断缓存中是否有数据

            //1.1获取key
            String key = "";
            //1.1.1获取目标方法的RedisCatch注解
            RedisCatch redisCatch = methodSignature.getMethod().getAnnotation(RedisCatch.class);
            key = redisCatch.keyPrefix();
            //1.1.2获取目标方法的方法名
            String methodName = proceedingJoinPoint.getSignature().getName();
            key = "".equals(key) ? methodName : key + RedisConstant.DEFAULT_COLON + methodName;
            //1.1.3获取目标方法的第一个参数（如果有的话），目的是保证存入redis缓存中的key不同
            Object[] args = proceedingJoinPoint.getArgs();
            if (args != null || args.length > 0) {
                key += RedisConstant.DEFAULT_COLON + args[0];
            }
            //1.1.4根据key获取redis中的值
            String value = jedis.get(key);
            //2.判断获取的值是否为空或空字符串
            if (value == null || "".equals(value)) {
                //2.1缓存中如果没有，则执行目标方法查询数据
                Object result = proceedingJoinPoint.proceed();
                //2.2将获取到的数据转成JSON字符串，并存入redis中
                int randomSeconds = Integer.parseInt(new Random().ints(1, 10).limit(3).mapToObj(String::valueOf).collect(Collectors.joining()));
                //2.2.1保证redis中，不会出现大量的缓存数据，同时过期
                //2.2.2并设置新的key和过期时间
                jedis.setex(key, RedisConstant.DEFAULT_KEY_EXPIRE + randomSeconds, JSON.toJSONString(result));
                return result;
            } else {
                //3.如果缓存中有数据，则直接将缓存中的数据转成对象，并返回
                //3.1获取目标方法的返回值类型 methodSignature.getReturnType()
                Class returnType = methodSignature.getReturnType();
                //3.2判断他是否为String类型
                //3.2.1如果是String类型，则直接返回获取的值value
                if (returnType == String.class) {
                    return value;
                }
                //3.3判断它是否是List，并可以确定类型 通过反射的方式 methodSignature.getMethod().getGenericReturnType()
                if (returnType == List.class) {
                    //将返回的值转成List，并返回
                    Type genericReturnType = methodSignature.getMethod().getGenericReturnType();
                    //3.3.1判断这个返回值类型  是否是 ParameterizedType
                    Type typeArgument = null;
                    if (genericReturnType instanceof ParameterizedType) {
                        //3.3.1.1如果是这个 ParameterizedType 类型，则将返回值强转成 ParameterizedType
                        ParameterizedType type = (ParameterizedType) genericReturnType;
                        //3.3.1.2获取List的泛型参数
                        //3.3.1.2.1 通过type.getActualTypeArguments()[0] 确定返回值的泛型
                        typeArgument = type.getActualTypeArguments()[0];
                    }
                    //3.4返回结果 ，注意将获取的value值转成JSON数组 ，并且将返回值的类型强转 成类类型
                    //注意 你尝试将typeArgument强制转换为Class类型。然而，typeArgument实际上是一个ParameterizedTypeImpl对象，而不是Class对象，因此导致了类型转换异常。
                    //return JSON.parseArray(value, (Class) typeArgument);
                    //通过将typeArgument强制转换为ParameterizedType，然后使用getRawType()方法获取原始类型，再将其转换为Class对象，就可以正确地构造出List对象。
                    return JSON.parseArray(value, (Class<?>) ((ParameterizedType) typeArgument).getRawType());
                } else {
                    //4.否则返回的是对象类型
                    //4.1将value值转成对象，并返回 JSON.parseObject(value, returnType);
                    return JSON.parseObject(value, returnType);
                }
            }
        } finally {
            //5.记得要把jedis关闭 close
            jedis.close();
        }
    }
}
