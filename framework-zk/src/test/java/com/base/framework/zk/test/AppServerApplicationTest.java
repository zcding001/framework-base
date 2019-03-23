package com.base.framework.zk.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
*  启动项
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
@SpringBootApplication(scanBasePackages = {"com.base.framework"})
public class AppServerApplicationTest {
    
    public static void main(String[] args) {
        SpringApplication.run(AppServerApplicationTest.class, args);
    }

}
