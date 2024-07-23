package com.polaris.project.aop;


import cn.dev33.satoken.stp.StpUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.polaris.project.annotation.BlackListInterceptor;
import com.polaris.project.common.ErrorCode;
import com.polaris.project.exception.BusinessException;
import com.polaris.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.polaris.project.constant.CommonConstant.LIMIT_PREFIX;


@Aspect
@Component
@Slf4j
public class RageLimitInterceptor {
    private static final String DEFAULT_KEY = "defaultKey";
    private final RedissonClient redissonClient;
    @Resource
    private UserService userService;
    private RMapCache<String, Long> blacklist;

    // 用来存储用户ID与对应的RateLimiter对象
    private final Cache<String, RRateLimiter> userRateLimiters = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    public RageLimitInterceptor (RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        if (redissonClient != null) {
            log.info("Redisson object is not null, using Redisson...");
            // 使用 Redisson 对象执行相关操作
            // 个人限频黑名单24h
            blacklist = redissonClient.getMapCache("blacklist");
            blacklist.expire(24, TimeUnit.HOURS);// 设置过期时间
        } else {
            log.error("Redisson object is null!");
        }
    }

    /**
     * @Description 重用切入点：注解标注过的地方
     * @author polaris
     * @create 2024/6/10
     */
    @Pointcut("@annotation(com.polaris.project.annotation.BlackListInterceptor)")
    public void aopPoint() {
    }

    /**
     * @Description 拦截请求，进行限流处理
     * 对用户的userAccount作为key限流，超过10次每秒限流加入黑名单（24小时），黑名单内用户拒绝请求快速失败
     * @author polaris
     * @create 2024/6/10
     * @return {@link Object}
     */
    @Around("aopPoint() && @annotation(blacklistInterceptor)")
    public Object doRouter(ProceedingJoinPoint jp, BlackListInterceptor blacklistInterceptor) throws Throwable {
        String key = blacklistInterceptor.key();

        // 获取请求属性
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        //获取 IP
        String remoteHost = httpServletRequest.getRemoteHost();
        if (StringUtils.isBlank(key)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "拦截的 key 不能为空");
        }
        String business = blacklistInterceptor.business();
        // 获取拦截字段
        String keyAttr;
        if (key.equals("default")) {
            keyAttr = StpUtil.getLoginId().toString();
        } else if(business.equals("invoke")){
            keyAttr = getUserAccount(jp.getArgs());
        } else if(business.equals("register") || key.equals("login")){
            log.info(Arrays.toString(jp.getArgs()));
            keyAttr = getAttrValue(key, jp.getArgs());
        } else keyAttr = DEFAULT_KEY;
        keyAttr=LIMIT_PREFIX + business + ":" + keyAttr;
        log.info("aop attr {}", keyAttr);

        // 黑名单拦截
        if (blacklistInterceptor.protectLimit() != 0 && null != blacklist.getOrDefault(keyAttr, null) && (blacklist.getOrDefault(keyAttr, 0L) >= blacklistInterceptor.protectLimit()
                ||blacklist.getOrDefault(remoteHost, 0L) >= blacklistInterceptor.protectLimit())) {
            log.info("有小黑子被我抓住了！给他 24 小时封禁套餐吧：{}", keyAttr);
            return fallbackMethodResult(jp, blacklistInterceptor.fallbackMethod());
        }

        // 获取限流
        RRateLimiter rateLimiter;
        if (!userRateLimiters.asMap().containsKey(keyAttr)) {
            rateLimiter = redissonClient.getRateLimiter(keyAttr);
            // 设置RateLimiter的速率，每秒发放10个令牌
            rateLimiter.trySetRate(RateType.OVERALL, blacklistInterceptor.rageLimit(), 1, RateIntervalUnit.SECONDS);
            userRateLimiters.put(keyAttr, rateLimiter);
        } else {
            rateLimiter = userRateLimiters.getIfPresent(keyAttr);
        }

        // 限流拦截
        if (rateLimiter != null && !rateLimiter.tryAcquire()) {
            if (blacklistInterceptor.protectLimit() != 0) {
                //封标识
                blacklist.put(keyAttr,blacklist.getOrDefault(keyAttr, 0L) + 1L);
                //封 IP
                blacklist.put(remoteHost, blacklist.getOrDefault(remoteHost, 0L) + 1L);
            }
            log.info("你刷这么快干嘛黑子：{}", keyAttr);
            return fallbackMethodResult(jp, blacklistInterceptor.fallbackMethod());
        }

        // 返回结果放行
        return jp.proceed();
    }

    private String getUserAccount (Object[] args){
        for(Object arg:args){
            if(arg instanceof HttpServletRequest){
                return userService.getLoginUser((HttpServletRequest) arg).getUserAccount();
            }
        }
        log.error("获取用户账号失败");
        return null;
    }

    /**
     * @Description 利用反射将fallbackMethod方法作为限流后的返回值返回
     * @author polaris
     * @create 2024/6/10
     * @return {@link Object}
     */
    private Object fallbackMethodResult(JoinPoint jp, String fallbackMethod) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        Method method = jp.getTarget().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        return method.invoke(jp.getThis(), jp.getArgs());
    }


    /**
     * 实际根据自身业务调整，主要是为了获取通过某个值做拦截
     */
    public String getAttrValue(String attr, Object[] args) {
        if (args[0] instanceof String) {
            return args[0].toString();
        }
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) {
                    break;
                }
                filedValue = String.valueOf(this.getValueByName(arg, attr));
            } catch (Exception e) {
                log.error("获取路由属性值失败 attr：{}", attr, e);
            }
        }
        return filedValue;
    }

    /**
     * 获取对象的特定属性值
     *
     * @param item 对象
     * @param name 属性名
     * @return 属性值
     * @author tang
     */
    private Object getValueByName(Object item, String name) {
        try {
            Field field = getFieldByName(item, name);
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            Object o = field.get(item);
            field.setAccessible(false);
            return o;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 根据名称获取方法，该方法同时兼顾继承类获取父类的属性
     *
     * @param item 对象
     * @param name 属性名
     * @return 该属性对应方法
     * @author tang
     */
    private Field getFieldByName(Object item, String name) {
        try {
            Field field;
            try {
                field = item.getClass().getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                field = item.getClass().getSuperclass().getDeclaredField(name);
            }
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

}