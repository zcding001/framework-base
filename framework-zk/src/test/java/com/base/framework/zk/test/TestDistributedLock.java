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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static int sum = 3;
    
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

    @Test
    public void testConcurrentGetLock() throws Exception{
        int count = 50;
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
                        sum = sum - 1;
                        System.out.println(Thread.currentThread().getName() + ": 我要等待6秒，测试其他等人超市是否有效.");
                        Thread.sleep(6000);
                        System.out.println("我的等待结束了，其他人可以过来拿锁了.");
                    }else{
                        System.out.println("zk等的花都谢了!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    DistributedLock.unLock();
                }
            });
        }
        executorService.shutdown();
        Thread.sleep(30000);
        System.out.println("finish, sum=" + sum);
    }
    
    
}
