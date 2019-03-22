package com.base.framework.cache.redis.utils;

import com.base.framework.core.utils.ApplicationContextUtils;
import io.lettuce.core.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 *
 * @author zc.ding
 * @since 2019/3/21
 */
@Component
@DependsOn("applicationContextUtils")
public class DistributedLock implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(DistributedLock.class);
    
    private static RedisTemplate<String, Object> redisTemplate;

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    
    /** 最小轮询间隔时间1000毫秒 **/
    private final static long DEFAULT_POLL_TIME = 1000;
    /** 默认等待时间5秒 **/
    private final static long DEFAULT_WAIT_TIME = 5;
    /** 默认过期时间5秒 **/
    private final static long DEFAULT_EXPIRE_TIME = 5;
    /** 最小默认时间1秒 **/
    private final static long MIN_DEFAULT_TIME = 1;
    /** 最大默认时间30秒 **/
    private final static long MAX_DEFAULT_TIME = 30;


    /**
    *  获得分布式锁
    *  @param key   键
    *  @return boolean
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean tryLock(String key) {
        return tryLock(key, DEFAULT_EXPIRE_TIME, DEFAULT_WAIT_TIME);    
    }
    
    /**
    *  获得分布式锁
    *  @param key   键
    *  @param expire    过期时间
    *  @param wait  等待时间
    *  @return boolean  true：拿到锁 false:未拿到锁
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean tryLock(String key, long expire, long wait) {
        if (expire < MIN_DEFAULT_TIME || expire > MAX_DEFAULT_TIME || wait < MIN_DEFAULT_TIME || wait > MAX_DEFAULT_TIME) {
            throw new RedisException("过期时间, 有效时间必须在[1, 30]之间");
        }
        try {
            wait = wait * 1000;
            expire = expire * 1000;
            long currTime = System.currentTimeMillis();
            threadLocal.set(currTime);
            //轮询时间为expire的2/3与DEFAULT_POLL_TIME中最大的那个值
            Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, currTime, expire, TimeUnit.MILLISECONDS);
            if(lock != null && lock){
                return true;
            }
            while (lock != null && !lock && (System.currentTimeMillis() - currTime) <= wait) {
                lock = redisTemplate.opsForValue().setIfAbsent(key, currTime, expire, TimeUnit.MILLISECONDS);
                if(lock != null && lock){
                    return true;
                }
                Thread.sleep(10);
            }
        } catch (Exception e) {
            logger.error("获取分布式锁异常.", e);
        }
        return false;
    }

    /**
    *  释放锁
    *  @param key   键
    *  @return boolean
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean freeLock(String key) {
        long oldCurrTime = threadLocal.get();
        threadLocal.remove();
        Long deadTime = (Long)redisTemplate.opsForValue().get(key);
        //如果没有说明锁已经释放
        if(deadTime == null){
            return true;
        }
        //如果存储的时间大于当前时间，说明此锁不是当前线程创建的锁，当前线程创建的所有已经过期自动自动释放了
        if(deadTime.equals(oldCurrTime)){
            return redisTemplate.delete(key) != null;
        }
        return false;
    }
    
    @Override
    public void afterPropertiesSet() {
        redisTemplate = ApplicationContextUtils.getBean("redisTemplate");
    }
}
