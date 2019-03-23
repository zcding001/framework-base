package com.base.framework.zk.utils;

import com.base.framework.core.utils.ApplicationContextUtils;
import com.base.framework.zk.config.ZkConfig;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * zk分布式锁
 * @author zc.ding
 * @since 2019/3/23
 */
@Component
@DependsOn({"zkConfig", "applicationContextUtils"})
public class DistributedLock implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DistributedLock.class);
    private static ZkConfig zkConfig;

    public static void lock() {
        
    }
    
    public static boolean tryLock() {
        System.out.println(zkConfig);
        return false;
    }

    public static void unLock() {
        
    }

    @Override
    public void afterPropertiesSet() {
        zkConfig = ApplicationContextUtils.getBean(ZkConfig.class);
    }
}
