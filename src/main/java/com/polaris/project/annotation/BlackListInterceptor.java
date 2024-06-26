package com.polaris.project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author polaris
 * @version 1.0
 * ClassName BlackList
 * Package com.polaris.project.constant
 * Description
 * @create 2024-06-02 21:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface BlackListInterceptor {
    /**
     * 拦截字段的标识符
     *
     * @return {@link String }
     */
    String key () default "default";

    /**
     * 频率限制 每秒请求次数
     *
     * @return double
     */
    long rageLimit () default 10;

    /**
     * 业务类型
     *
     * @return double
     */
    String business () default "";

    /**
     * 保护限制 命中频率次数后触发保护，默认触发限制就保护进入黑名单
     *
     * @return int
     */
    int protectLimit () default 1;

    /**
     * 回调方法
     *
     * @return {@link String }
     */
    String fallbackMethod ();


}
