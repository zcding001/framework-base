package com.base.framework.zk.test;

import com.base.framework.zk.utils.DistributedLock;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试zookeeper分布式锁
 *
 * @author zc.ding
 * @since 2019/3/23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplicationTest.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDistributedLock {
    
    @Test
    public void test() {
        DistributedLock.tryLock();
    }
}
