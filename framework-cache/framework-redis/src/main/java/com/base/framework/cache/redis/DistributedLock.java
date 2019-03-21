package com.base.framework.cache.redis;

import com.base.framework.core.utils.ApplicationContextUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis分布式锁
 *
 * @author zc.ding
 * @since 2019/3/21
 */
@Component
@DependsOn("applicationContextUtils")
public class DistributedLock implements InitializingBean {

    private static RedisTemplate<String, Object> redisTemplate;

    public static boolean tryLock(String key, long time) {

        
        return true;
    }
    
    @Override
    public void afterPropertiesSet() {
        redisTemplate = ApplicationContextUtils.getBean("redisTemplate");
    }
}
