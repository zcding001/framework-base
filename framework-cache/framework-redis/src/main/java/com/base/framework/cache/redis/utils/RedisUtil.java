package com.base.framework.cache.redis.utils;

import com.base.framework.cache.redis.exceptions.RedisFrameworkExpception;
import com.base.framework.core.utils.ApplicationContextUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * redis 工具类
 *
 * @author zc.ding
 * @since  2018/11/15
 */
@Component
@DependsOn({"applicationContextUtils"})
public class RedisUtil implements InitializingBean {
    
    private static RedisTemplate<String, Object> redisTemplate;
    private static HashOperations<String, String, Object> hashOperations;
    private static ListOperations<String, Object> listOperations;
    private static SetOperations<String, Object> setOperations;
    private static ZSetOperations<String, Object> zSetOperations;
    
    private RedisUtil(){}

    /**
     *  执行命令
     *  @param consumer  命令函数
     *  @return java.lang.Boolean
     *  @since                   ：2019/3/21
     *  @author                  ：zc.ding@foxmail.com
     */
    private static <T> Boolean exeCommand(Consumer<T> consumer) {
        try {
            consumer.accept(null);
        } catch (RedisFrameworkExpception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
    *  执行命令
    *  @param supplier  函数
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    private static Long exeCommandForLong(Supplier<Long> supplier) {
        try {
            return supplier.get();
        } catch (RedisFrameworkExpception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     *  执行命令
     *  @param supplier  函数
     *  @return java.lang.Long
     *  @since                   ：2019/3/21
     *  @author                  ：zc.ding@foxmail.com
     */
    private static Boolean exeCommandForBoolean(Supplier<Boolean> supplier) {
        try {
            return supplier.get();
        } catch (RedisFrameworkExpception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  执行命令
     *  @param supplier  函数
     *  @return java.lang.Long
     *  @since                   ：2019/3/21
     *  @author                  ：zc.ding@foxmail.com
     */
    private static Object exeCommandForObject(Supplier<Object> supplier) {
        try {
            return supplier.get();
        } catch (RedisFrameworkExpception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public static HashOperations<String, String, Object> getHashOperations() {
        return hashOperations;
    }

    public static ListOperations<String, Object> getListOperations() {
        return listOperations;
    }

    public static SetOperations<String, Object> getSetOperations() {
        return setOperations;
    }

    public static ZSetOperations<String, Object> getzSetOperations() {
        return zSetOperations;
    }

    @Override
    public void afterPropertiesSet() {
        redisTemplate = ApplicationContextUtils.getBean("redisTemplate");
        hashOperations = redisTemplate.opsForHash();
        listOperations = redisTemplate.opsForList();
        setOperations = redisTemplate.opsForSet();
        zSetOperations = redisTemplate.opsForZSet();
    }

    /**
    *  设置缓存失效时间
    *  @param key   键
    *  @param time  失效时间(秒)
    *  @return boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
    *  获取键的缓存时间
    *  @param key   键
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
    *  判断key是否存在
    *  @param key   键
    *  @return java.lang.Boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
    *  删除键值对
    *  @param key   键
    *  @return java.lang.Boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean del(String key) {
        return redisTemplate.delete(key);
    }
    
    /**
    *  批量删除键值对
    *  @param keys  键集合
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long del(List<String> keys){
        return redisTemplate.delete(keys);
    }
    
    /**
    *  放入对象
    *  @param key   键
    *  @param value    值
    *  @return boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean set(String key, Object value) {
        return exeCommand((o) -> redisTemplate.opsForValue().set(key, value));
    }

    /**
    *  批量放入键值对
    *  @param map   键值对集合
    *  @return boolean
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    public static <T> boolean set(Map<String, T> map) {
        return exeCommand((o) -> redisTemplate.opsForValue().multiSet(map));
    }
    
    /**
    *  获取对象
    *  @param key 键
    *  @return T
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return Objects.isNull(key) ? null : (T)(redisTemplate.opsForValue().get(key));
    }

    /**
    *  批量获得键值对
    *  @param keys  键集合
    *  @return java.util.List<T>
    *  @since                   ：2019/3/22
    *  @author                  ：zc.ding@foxmail.com
    */
    @SuppressWarnings("unchecked")
    public static <T> List<T> get(List<String> keys) {
        return (List<T>)redisTemplate.opsForValue().multiGet(keys);
    }

    /**
    *  设置键值对同时设置过期时间
    *  @param key   键
    *  @param value 值
    *  @param time  过期时间(秒)
    *  @return java.lang.Boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean set(String key, Object value, long time) {
        return exeCommand((o) -> redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS));
    }
    
    /**
    *  将键的值递增num
    *  @param key   键
    *  @param num   增加|减少的值不能等于0
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long increment(String key, long num) {
        return num == 0 ? Long.valueOf(num) : redisTemplate.opsForValue().increment(key, num);
    }
    
    
//    =============================== hashmap ====================================
    /**
    *  存储Map
    *  @param key   键
    *  @param map   值
    *  @return java.lang.Boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean hSet(String key, Map<String, Object> map) {
        return exeCommand((o) -> redisTemplate.opsForHash().putAll(key, map));
    }
    
    /**
    *  存储Map并设置过期时间
    *  @param key   键
    *  @param map   值
    *  @param time  过期时间
    *  @return java.lang.Boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean hSet(String key, Map<String, Object> map, long time) {
        return hSet(key, map) && expire(key, time);
    }

    /**
    *  向一张hash表中放入数据,如果不存在将创建
    *  @param key   键
    *  @param item  map中的键
    *  @param value map中的键对应的值
    *  @return java.lang.Boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean hSet(String key, String item, Object value) {
        return exeCommand((o) -> redisTemplate.opsForHash().put(key, item, value));
    }
    
    /**
    *  获取键对应的map
    *  @param key   键
    *  @return java.util.Map<java.lang.Object,java.lang.Object>
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Map<Object, Object> hGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
    
    /**
    *  获取键对应的map中的item对应的值
    *  @param key   键
    *  @param item  map中的键
    *  @return java.lang.Object
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    @SuppressWarnings("unchecked")
    public static <T> T hGet(String key, String item) {
        return (T)redisTemplate.opsForHash().get(key, item);
    }
    
    /**
    *  判断hash表中是否有该项的值
    *  @param key   键
    *  @param item  值中键
    *  @return java.lang.Boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean hHaskey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
    *  递增 如果不存在,就会创建一个 并把新增后的值返回
    *  @param key   键
    *  @param item  值中的键
    *  @param num   增减|减少的书
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long hIncrement(String key, String item, long num) {
        return num == 0 ? Long.valueOf(num) : redisTemplate.opsForHash().increment(key, item, num);
    }
    
    /**
    *  删除hash表中的值
    *  @param key   键
    *  @param items 值中的键
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long hDel(String key, Object... items) {
        return redisTemplate.opsForHash().delete(key, items);
    }


    //    ===============================  set zset ==================================
    /**
    *  存入set
    *  @param key   键
    *  @param values    值
    *  @return java.lang.Boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long sSet(String key, Object... values) {
        return exeCommandForLong(() -> setOperations.add(key, values));
    }

    /**
    *  存入set并设置有效期
    *  @param key   键
    *  @param time  有效时间
    *  @param values    值
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long sSet(String key, long time, Object... values) {
        Long count = exeCommandForLong(() -> setOperations.add(key, values));
        expire(key, time);
        return count;
    }

    /**
    *  获取set
    *  @param key   键
    *  @return java.util.Set<java.lang.Object>
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> sGet(String key) {
        return (Set<T>)setOperations.members(key);
    }

    /**
    *  判断set是否含有指定值
    *  @param key   键
    *  @param value 值
    *  @return java.lang.Boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Boolean sHasValue(String key, Object value) {
        return exeCommandForBoolean(() -> setOperations.isMember(key, value));
    }

    /**
    *  获取set长度
    *  @param key   键
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long sSize(String key) {
        return exeCommandForLong(() -> setOperations.size(key));
    }

    /**
    *  移除set中指定的值
    *  @param key   键
    *  @param values    值
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long sDel(String key, Object... values) {
        return exeCommandForLong(() -> setOperations.remove(key, values));
    }


    //    ========================  list ==========================
    
    /**
    *  存储list
    *  @param key   键
    *  @param list 值
    *  @return long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long lSet(String key, List<Object> list) {
        del(key);
        return exeCommandForLong(() -> listOperations.rightPushAll(key, list));
    }

    /**
    *  存储list并设置过期时间
    *  @param key   键
    *  @param list  值
    *  @param time  过期时间
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long lSet(String key, List<Object> list, long time) {
        Long count = exeCommandForLong(() -> listOperations.rightPushAll(key, list));
        expire(key, time);
        return count;
    }

    /**
    *  根据索引修改list中的某条数据
    *  @param key   键
    *  @param index 索引
    *  @param value 新值
    *  @return boolean
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static boolean lUpdate(String key, long index, Object value) {
        return exeCommand((o) -> listOperations.set(key, index, value));
    }

    /**
    *  获得所有List
    *  @param key   键
    *  @return java.util.List<T>
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static <T> List<T> lGet(String key) {
        return lGet(key, 0, -1);
    }

    /**
    *  获取指定范围的List， 0到-1标识取所有值
    *  @param key   键
    *  @param start 开始
    *  @param end   结束
    *  @return java.util.List<T>
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    @SuppressWarnings("unchecked")
    public static <T> List<T> lGet(String key, long start, long end) {
        return (List<T>)(listOperations.range(key, start, end));
    }

    /**
    *  获取List长度
    *  @param key   键
    *  @return java.lang.Long
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    public static Long lSize(String key) {
        return exeCommandForLong(() -> listOperations.size(key));
    }

    /**
    *  通过索引 获取list中的值
    *  @param key   键
    *  @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
    *  @return T
    *  @since                   ：2019/3/21
    *  @author                  ：zc.ding@foxmail.com
    */
    @SuppressWarnings("unchecked")
    public static <T> T lGet(String key, long index) {
        return (T)exeCommandForObject(() -> listOperations.index(key, index));
    }
    
}
