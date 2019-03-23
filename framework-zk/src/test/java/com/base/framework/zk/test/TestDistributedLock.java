package com.base.framework.zk.test;

import com.base.framework.zk.config.ZkConfig;
import com.base.framework.zk.utils.DistributedLock;
import org.apache.zookeeper.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    
    @Autowired
    private ZkConfig zkConfig;
    private static final byte[] buf = new byte[0];
    
    @Test
    public void test() throws Exception{
        String rootPath = "/locks";
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(zkConfig.getAddress(), 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                switch (watchedEvent.getState()) {
                    case Expired:
                        System.out.println("连接超时");
                        break;
                    case SyncConnected:
                        countDownLatch.countDown();
                        default:
                }
            }
        });
        countDownLatch.await();
        for (int i = 0; i < 20; i++) {
            new Thread(() ->{
                try {
                    System.out.println(zooKeeper.create(rootPath + "/member_", buf, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        
        Thread.sleep( 10000);
        System.out.println(zooKeeper.getChildren(rootPath, false));
    }
    
    @Test
    public void testSort() {
        List<String> list = Arrays.asList("member_0000000051", "member_0000000052", "member_0000000042");
        list.sort(String::compareTo);
        System.out.println(list);
    }
}
