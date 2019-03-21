package com.base.framework.cache.redis.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试人员类
 *
 * @author zc.ding
 * @since  2019/3/21
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String name;
    private Integer age;
    private Integer sex;
    
}
