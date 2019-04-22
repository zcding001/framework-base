package com.base.framework.cache.redis.utils;

import com.base.framework.cache.redis.exceptions.RedisFrameworkExpception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * redis分布式锁
 *
 * @author zc.ding
 * @since 2019/3/21
 */
@Component
public class DistributedLock implements InitializingBean, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(DistributedLock.class);
    
    private static RedisTemplate<String, Object> redisTemplate;
    /**用于存储锁的内容**/
    private static ThreadLocal<Long> VALUE_THREAD_LOCAL = new ThreadLocal<>();
    private static ThreadLocal<String> KEY_THREAD_LOCAL = new ThreadLocal<>();
    private ApplicationContext applicationContext;
    
    /** 默认等待时间5秒 **/
    private final static long DEFAULT_WAIT_TIME = 5;
    /** 默认过期时间5秒 **/
    private final static long DEFAULT_EXPIRE_TIME = 5;
    /** 最小默认时间1秒 **/
    private final static long MIN_DEFAULT_TIME = 1;
    /** 最大默认时间30秒 **/
    private final static long MAX_DEFAULT_TIME = 30;

    private final static String LUA_LOCK = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end else return 0 end";
    private final static String LUA_UNLOCK = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
    private static DefaultRedisScript<Boolean> LUA_LOCK_SCRIPT;
    private static DefaultRedisScript<Boolean> LUA_UNLOCK_SCRIPT;
    static{
        LUA_LOCK_SCRIPT = new DefaultRedisScript<>();
        LUA_LOCK_SCRIPT.setScriptText(LUA_LOCK);
        LUA_LOCK_SCRIPT.setResultType(Boolean.class);
        LUA_UNLOCK_SCRIPT = new DefaultRedisScript<>();
        LUA_UNLOCK_SCRIPT.setScriptText(LUA_UNLOCK);
        LUA_UNLOCK_SCRIPT.setResultType(Boolean.class);
    }
    
    /**
    *  获得分布式锁, 推荐使用{@link DistributedLock#tryLockLua(String)}}
    *  @param key   键
    *  @return boolean
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean tryLock(String key) {
        return tryLock(key, DEFAULT_EXPIRE_TIME, DEFAULT_WAIT_TIME);    
    }
    
    /**
    *  获得分布式锁，推荐使用{@link DistributedLock#tryLockLua(String, long, long)}}
    *  @param key   键
    *  @param expire    过期时间
    *  @param wait  等待时间
    *  @return boolean  true：拿到锁 false:未拿到锁
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean tryLock(String key, long expire, long wait) {
        validParam(key, expire, wait);
        try {
            wait = wait * 1000;
            expire = expire * 1000;
            long currTime = Thread.currentThread().getId();
            VALUE_THREAD_LOCAL.set(currTime);
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
    *  释放锁, 推荐使用{@link DistributedLock#unLockLua()}
    *  @return boolean
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean unLock() {
        long oldCurrTime = VALUE_THREAD_LOCAL.get();
        VALUE_THREAD_LOCAL.remove();
        String key = KEY_THREAD_LOCAL.get();
        KEY_THREAD_LOCAL.remove();
        Long deadTime = (Long)redisTemplate.opsForValue().get(key);
        //如果没有说明锁已经释放
        if(deadTime == null){
            return true;
        }
        return redisTemplate.opsForValue().setIfPresent(key, oldCurrTime, 1, TimeUnit.MILLISECONDS);
    }

    /**
     *  获得分布式锁
     *  @param key   键
     *  @return boolean
     *  @since                   ：2019/3/22
     *  @author                  ：zc.ding@foxmail.com
     */
    public static boolean tryLockLua(String key) {
        return tryLockLua(key, DEFAULT_EXPIRE_TIME, DEFAULT_WAIT_TIME);
    }

    /**
     *  获得分布式锁，保证操作原子性，防止在setnx和expire之间异常导致死锁
     *  @param key   键
     *  @param expire    过期时间
     *  @param wait  等待时间
     *  @return boolean  true：拿到锁 false:未拿到锁
     *  @since                   ：2019/3/22
     *  @author                  ：zc.ding@foxmail.com
     */
    public static boolean tryLockLua(String key, long expire, long wait) {
        validParam(key, expire, wait);
        try {
            wait = wait * 1000;
            long currTime = Thread.currentThread().getId();
            VALUE_THREAD_LOCAL.set(currTime);
            Boolean lock = redisTemplate.execute(LUA_LOCK_SCRIPT, Collections.singletonList(key), currTime, expire);
            if(lock != null && lock){
                return true;
            }
            while (lock != null && !lock && (System.currentTimeMillis() - currTime) <= wait) {
                lock = redisTemplate.execute(LUA_LOCK_SCRIPT, Collections.singletonList(key), currTime, expire);
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
     *  @author                  ：zc.ding@foxmail.com
     */
    public static Boolean unLockLua() {
        long oldCurrTime = VALUE_THREAD_LOCAL.get();
        VALUE_THREAD_LOCAL.remove();
        String key = KEY_THREAD_LOCAL.get();
        KEY_THREAD_LOCAL.remove();
        return redisTemplate.execute(LUA_UNLOCK_SCRIPT, Collections.singletonList(key), oldCurrTime);
    }

    /**
     *  验证锁时间和过期时间
     *  @param key   键
     *  @param expire    过期时间
     *  @param wait  等待时间
     *  @since                   ：2019/3/22
     *  @author                  ：zc.ding@foxmail.com
     */
    private static void validParam(String key, long expire, long wait) {
        if (expire < MIN_DEFAULT_TIME || expire > MAX_DEFAULT_TIME || wait < MIN_DEFAULT_TIME ||
                wait > MAX_DEFAULT_TIME) {
            throw new RedisFrameworkExpception("过期时间, 有效时间必须在[1, 30]之间");
        }
        KEY_THREAD_LOCAL.set(key);
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() {
        redisTemplate = (RedisTemplate<String, Object>)applicationContext.getBean("redisTemplate");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
