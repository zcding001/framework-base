package com.base.framework.cache.redis.test;

import com.base.framework.cache.redis.utils.DistributedLock;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;

/**
 * 测试分布式锁
 * @author zc.ding
 * @since  2019/3/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplicationTest.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDistributeLock {
    
    private static int sum = 3;
    
    @Test
    public void testGetLock() {
        assertTrue(DistributedLock.tryLock("lock", 60, 60));
    }

    @Test
    public void testFreeLock() {
        assertTrue(DistributedLock.tryLock("lock", 60, 60));
        assertTrue(DistributedLock.unLock("lock"));
    }
    
    @Test
    public void testConcurrentGetLock() throws Exception{
        int count = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for( int i = 0; i < count; i++) {
            executorService.execute(() -> {
                try {
                    if(DistributedLock.tryLock("lock")){
                        System.out.println("我拿到了锁");
                        if(sum == 0){
                            System.out.println("sum已经是0");
                            return;
                        }
                        System.out.println("还没有停止，sum=" + sum);
                        sum = sum -1;
                    }else{
                        System.out.println("等的花都谢了!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    DistributedLock.unLock("lock");
                }
            });
        }
        executorService.shutdown();
        Thread.sleep(30000);
        System.out.println("finish, sum = " + sum);
    }
}
