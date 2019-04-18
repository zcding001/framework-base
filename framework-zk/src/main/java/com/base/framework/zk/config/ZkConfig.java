package com.base.framework.zk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * zk 配置信息
 * @author zc.ding
 * @since 2019/3/23
 */
@Component
@PropertySource("classpath:zookeeper.properties")
@ConfigurationProperties(prefix = "zk")
@Data
public class ZkConfig {

    /** zk地址 **/
    private String address;
    /** session超时时间 **/
    private int sessionTimeout;
    /** 等待连接时间 **/
    private long waitConnTimeout;
    /** 锁模式 0：非公平锁 1：公平锁 **/
    private int fairLock;
    
    
}
