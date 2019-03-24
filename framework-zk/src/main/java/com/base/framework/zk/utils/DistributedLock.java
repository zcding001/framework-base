package com.base.framework.zk.utils;

import com.base.framework.core.commons.Constants;
import com.base.framework.core.utils.ApplicationContextUtils;
import com.base.framework.core.utils.ThreadPoolUtil;
import com.base.framework.zk.config.ZkConfig;
import com.base.framework.zk.exceptions.ZkFrameworkExpception;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * zk实现公平/非公平分布式锁
 * @author zc.ding
 * @since 2019/3/23
 */
@Component
@DependsOn({"zkConfig", "applicationContextUtils"})
public class DistributedLock implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DistributedLock.class);
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<ZooKeeper> ZK_THREAD_LOCAL = new ThreadLocal<>();
    /** 锁的根路径 **/
    private static final String LOCK_ROOT_PATH = "/locks";
    /** 锁后缀 **/
    private static final String LOCK_SUFFIX = "_NO_";
    /** 创建根节点同步锁 **/
    private static final String CREATE_ROOT_LOCK = "LOCK";
    /** 公平锁 **/
    private static final int LOCK_FAIR = 1;
    private final static byte[] BUF = new byte[0];
    private static ZkConfig zkConfig;

    /**
    *  获得所有
    *  @param key   路径不含‘/’
    *  @return boolean
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean tryLock(String key) {
        return tryLock(key, Constants.LOCK_EXPIRES, Constants.LOCK_WAITTIME);    
    }
    
    /**
    *  获得锁
    *  @param key   键/路径
    *  @param expire    过期时间
    *  @param wait  等待时间
    *  @return boolean
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean tryLock(String key, long expire, long wait) {
        ZooKeeper zooKeeper = getZooKeeper();
        ZK_THREAD_LOCAL.set(zooKeeper);
        return tryLock(zooKeeper, key, expire, wait);
    }
    
    /**
    *  获得锁
    *  @param zooKeeper zk连接
    *  @param key   路径
    *  @param expire    过期时间
    *  @param wait  等待时间
    *  @return boolean
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */
    private static boolean tryLock(ZooKeeper zooKeeper, String key, long expire, long wait) {
        expire = expire * 1000;
        wait = wait * 1000;
        final String currNode;
        String path = LOCK_ROOT_PATH + "/" + key + LOCK_SUFFIX;
        try {
            currNode = zooKeeper.create(path, BUF, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            //步骤一
            List<String> nodes = zooKeeper.getChildren(LOCK_ROOT_PATH, false);
            //过滤掉集合中不是当前业务的临时节点
            nodes = nodes.stream().filter(o -> o.startsWith(key)).collect(Collectors.toList());
            nodes.sort(String::compareTo);
            //如果集合为空说明当前创建节点的session在步骤一处已经断开，并且创建的节点已经被zk服务器删除, 此种情况比较极端
            if (nodes.size() == 0) {
                return false;
            }
            //最小的节点就是自己创建的节点表示拿到锁
            if (currNode.endsWith(nodes.get(0))) {
                runExpireThread(zooKeeper, currNode, expire);
                return true;
            }
            //没有拿到锁
            CountDownLatch countDownLatch = new CountDownLatch(1);
            //非公平锁
            if(zkConfig.getFairLock() == LOCK_FAIR){
                for (int i = 0; i < nodes.size(); i++) {
                    String node = nodes.get(i);
                    if (currNode.endsWith(node)) {
                        runExpireThread(zooKeeper, currNode, expire);
                        return true;
                    }
                    Stat stat = zooKeeper.exists(LOCK_ROOT_PATH + "/" + node, new LockWatcher(countDownLatch));
                    if (stat != null) {
                        delPath(zooKeeper);
                        //等待锁超时
                        if(!countDownLatch.await(wait, TimeUnit.MILLISECONDS)){
                            return tryLock(zooKeeper, key, expire, wait);
                        }
                    }
                }
            }else{
                for (int i = 0; i < nodes.size(); i++) {
                    String node = nodes.get(i);
                    if (currNode.endsWith(node)) {
                        runExpireThread(zooKeeper, currNode, expire);
                        return true;
                    }
                    //当前节点的前一个节点
                    if (currNode.endsWith(nodes.get(i + 1))) {
                        Stat stat = zooKeeper.exists(LOCK_ROOT_PATH + "/" + node, new LockWatcher(countDownLatch));
                        if (stat != null) {
                            //等待锁超时
                            if(!countDownLatch.await(wait + 5000, TimeUnit.MILLISECONDS)){
                                delPath(zooKeeper);
                                return false;
                            }
                            return true;
                        }
                    }
                }
            }
        } catch (KeeperException | InterruptedException e) {
            LOG.error("create '{}' node fail.", key, e);
        }
        return false;
    }

    /**
    *  释放锁
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */
    public static void unLock() {
        ZooKeeper zooKeeper = ZK_THREAD_LOCAL.get();
        delPath(zooKeeper);
        close(ZK_THREAD_LOCAL.get());
        THREAD_LOCAL.remove();
        ZK_THREAD_LOCAL.remove();
    }

    

    /**
    *  创建分布式锁的根路径
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */
    private static void createLockRootPath() {
        ZooKeeper zooKeeper = getZooKeeper();
        try {
            Stat stat = zooKeeper.exists(LOCK_ROOT_PATH, false);
            if (stat == null) {
                synchronized (CREATE_ROOT_LOCK) {
                    stat = zooKeeper.exists(LOCK_ROOT_PATH, false);
                    if (stat == null) {
                        LOG.info("create lock root path '{}'", LOCK_ROOT_PATH);
                        zooKeeper.create(LOCK_ROOT_PATH, BUF, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
                    }
                }
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
    *  获得zooke会话连接
    *  @return org.apache.zookeeper.ZooKeeper
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */
    private static ZooKeeper getZooKeeper() {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            final ZooKeeper zooKeeper = new ZooKeeper(zkConfig.getAddress(), zkConfig.getSessionTimeout(), null);
            zooKeeper.register((watchedEvent) -> {
                switch (watchedEvent.getState()) {
                    case Expired:
                        close(zooKeeper);
                        break;
                    case SyncConnected:
                        countDownLatch.countDown();
                    default:
                }
            });

            if(!countDownLatch.await(zkConfig.getWaitConnTimeout(), TimeUnit.MILLISECONDS)){
                close(zooKeeper);
                throw new ZkFrameworkExpception("wait for creating zookeeper connection timeout, timeout is [" + zkConfig.getWaitConnTimeout() + "]");
            }
            return zooKeeper;
        } catch (IOException | InterruptedException e) {
            throw new ZkFrameworkExpception("create Zookeeper instance fail.", e);
        }
    }
    
    /**
    *  启动一个线程来判断锁的过期时间，方式业务假死，zk不断开导致死锁
    *  @param zooKeeper zk连接
    *  @param currNode  当前节点
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */
    private static void runExpireThread(final ZooKeeper zooKeeper, String currNode, long expire){
        THREAD_LOCAL.set(currNode);
        ThreadPoolUtil.callFixedThreadPool(() -> {
            Thread.sleep(expire * 1000);
            LOG.info("等待了{}秒, 主动结束.", expire);
            delPath(zooKeeper);
            return null;
        });
    }

    /**
    *  删除创建的路径
    *  @param zooKeeper zk连接
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */
    private static void delPath(ZooKeeper zooKeeper) {
        try {
            //无论节点是否存在，直接执行删除操作
            zooKeeper.delete(THREAD_LOCAL.get(), -1);
        } catch (Exception e){
            LOG.error("lock expire, delete lock");
        }
    }

    /**
    *  断开连接
    *  @param zooKeeper zk连接
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */    
    private static void close(ZooKeeper zooKeeper) {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void afterPropertiesSet() {
        zkConfig = ApplicationContextUtils.getBean(ZkConfig.class);
        createLockRootPath();
    }

    /**
    * 
    *  监听节点删除事件
    *  @since                   ：2019/3/23
    *  @author                  ：zc.ding@foxmail.com
    */
    static class LockWatcher implements Watcher {
        private CountDownLatch latch;

        public LockWatcher(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void process(WatchedEvent event) {
            if(event.getType() == Event.EventType.NodeDeleted){
                latch.countDown();
            }
        }
    }
}
