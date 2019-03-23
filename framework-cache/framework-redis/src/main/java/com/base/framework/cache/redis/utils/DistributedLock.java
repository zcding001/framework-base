package com.base.framework.cache.redis.utils;

import com.base.framework.cache.redis.exceptions.RedisFrameworkExpception;
import com.base.framework.core.commons.Constants;
import com.base.framework.core.utils.ApplicationContextUtils;
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

    private static final Logger LOG = LoggerFactory.getLogger(DistributedLock.class);
    
    private static RedisTemplate<String, Object> redisTemplate;
    /**用于存储锁的内容**/
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> KEY_THREAD_LOCAK = new ThreadLocal<>();
    
//    /** 默认等待时间5秒 **/
//    private final static long DEFAULT_WAIT_TIME = 5;
//    /** 默认过期时间5秒 **/
//    private final static long DEFAULT_EXPIRE_TIME = 5;
//    /** 最小默认时间1秒 **/
//    private final static long MIN_DEFAULT_TIME = 1;
//    /** 最大默认时间30秒 **/
//    private final static long MAX_DEFAULT_TIME = 30;

    /**
    *  获得分布式锁
    *  @param key   键
    *  @return boolean
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean tryLock(String key) {
        return tryLock(key, Constants.LOCK_EXPIRES, Constants.LOCK_WAITTIME);    
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
        if (expire < Constants.LOCK_MIN_TIME || expire > Constants.LOCK_MAX_TIME || wait < Constants.LOCK_MIN_TIME || 
                wait > Constants.LOCK_MAX_TIME) {
            throw new RedisFrameworkExpception("过期时间, 有效时间必须在[1, 30]之间");
        }
        KEY_THREAD_LOCAK.set(key);
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
            LOG.error("获取分布式锁异常.", e);
        }
        return false;
    }

    /**
    *  释放锁
    *  @return boolean
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean unLock() {
        long oldCurrTime = threadLocal.get();
        threadLocal.remove();
        String key = KEY_THREAD_LOCAK.get();
        KEY_THREAD_LOCAK.remove();
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
