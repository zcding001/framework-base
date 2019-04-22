package com.base.framework.cache.redis.test;

import com.base.framework.cache.redis.utils.DistributedLock;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

/**
 * 测试分布式锁
 *
 * @author zc.ding
 * @since 2019/3/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplicationTest.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDistributeLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static int sum = 3;

    @Test
    public void testGetLock() {
        assertTrue(DistributedLock.tryLock("lock", 60, 60));
    }

    @Test
    public void testFreeLock() {
        assertTrue(DistributedLock.tryLock("lock", 60, 60));
        assertTrue(DistributedLock.unLock());
    }

    @Test
    public void testConcurrentGetLock() throws Exception {
        int count = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                try {
                    if (DistributedLock.tryLock("lock")) {
                        System.out.println("我拿到了锁");
                        if (sum == 0) {
                            System.out.println("sum已经是0");
                            return;
                        }
                        System.out.println("还没有停止，sum=" + sum);
                        sum = sum - 1;
                    } else {
                        System.out.println("等的花都谢了!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    DistributedLock.unLock();
                }
            });
        }
        executorService.shutdown();
        Thread.sleep(30000);
        System.out.println("finish, sum = " + sum);
    }

    @Test
    public void testLuaLock() throws Exception {
        int count = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++) {
            executorService.execute(() -> {
                try {
                    if (DistributedLock.tryLockLua("lock")) {
                        System.out.println("我拿到了锁");
                        if (sum == 0) {
                            System.out.println("sum已经是0");
                            return;
                        }
                        System.out.println("还没有停止，sum=" + sum);
                        sum = sum - 1;
                    } else {
                        System.out.println("等的花都谢了!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    DistributedLock.unLockLua();
                }
            });
        }
        executorService.shutdown();
        Thread.sleep(30000);
        System.out.println("finish, sum = " + sum);
    }

    @Test
    public void testLua() {
        String LUA_LOCK = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end else return 0 end";
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_LOCK);
        script.setResultType(Boolean.class);
        System.out.println(redisTemplate.execute(script, Collections.singletonList("keyLock"), "hello", 60));
    }
    
    @Test
    public void testUnLock(){
        String LUA_UNLOCK = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_UNLOCK);
        script.setResultType(Boolean.class);
        System.out.println(redisTemplate.execute(script, Collections.singletonList("keyLock"), "hello"));
    }
}
