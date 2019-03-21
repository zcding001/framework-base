package com.base.framework.cache.redis.test;

import com.base.framework.cache.redis.AppServerApplication;
import com.base.framework.cache.redis.RedisUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

/**
 * 测试分布式锁
 * @author zc.ding
 * @create 2019/3/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDistributeLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Test()
    public void testSetString(){
        assertTrue(redisTemplate.opsForValue().setIfPresent("lock1", "lock"));
    }
    
}
