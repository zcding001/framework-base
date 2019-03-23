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

    private String address;
    private int sessionTimeout;
    private long waitConnTimeout;
    private int fairLock;
    
    
}
