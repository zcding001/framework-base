package com.base.framework.cache.redis.test;

import com.base.framework.cache.redis.utils.RedisUtil;
import com.base.framework.cache.redis.test.model.Person;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
//import static org.junit.matchers.JUnitMatchers.*;

/**
 * ReidsUtil测试类
 * @author zc.ding
 * @since  2019/3/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplicationTest.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRedisUtil {

    @Test
    public void testNotNull(){
        assertNotNull(RedisUtil.getHashOperations());    
        assertNotNull(RedisUtil.getRedisTemplate());    
        assertNotNull(RedisUtil.getListOperations());    
        assertNotNull(RedisUtil.getSetOperations());    
        assertNotNull(RedisUtil.getzSetOperations());    
    }
    
    @Test
    public void testString(){
        String key = "redis";
        assertTrue(RedisUtil.set(key, "hello"));
        assertTrue(RedisUtil.set(key + "1", "hello"));
        assertTrue(RedisUtil.set(key + "expire", "hello wrold", 30));
        assertEquals(RedisUtil.get(key), "hello");
        assertTrue(RedisUtil.expire(key, 300));
        assertTrue(RedisUtil.getExpire(key) > 0);
        assertTrue(RedisUtil.del(key));
        assertEquals(RedisUtil.del(Arrays.asList(key, key + "1")), Long.valueOf(1));
        key = "number";
        RedisUtil.set(key, 2);
        assertEquals(RedisUtil.increment(key, 2), Long.valueOf(4));
        assertEquals(RedisUtil.increment(key, -1), Long.valueOf(3));
        
        Map<String, Object> map = new HashMap<String, Object>(){{
            put("a1", "a1");
            put("a2", "a2");
            put("a3", "a3");
        }};
        assertTrue(RedisUtil.set(map));
        assertEquals(RedisUtil.del(Arrays.asList("a1", "a2", "a3")), Long.valueOf(3));
    }
    
    @Test
    public void testObject(){
        String key = "person";
        Person person = new Person("person", 20, 1);
        assertTrue(RedisUtil.set(key, person));
        assertEquals(RedisUtil.get(key), person);
        assertTrue(RedisUtil.hasKey(key));

        Map<String, Person> map = new HashMap<String, Person>(){{
            put("zhangSan", new Person("zhangSan", 20, 1));
            put("liSi", new Person("liSi", 21, 2));
            put("wangWu", new Person("wangWu", 22, 1));
        }};
        assertTrue(RedisUtil.set(map));
        assertThat(RedisUtil.get(Arrays.asList("zhangSan", "liSi", "wangWu")), hasItem(new Person("zhangSan", 20, 1)));
    }
    
    @Test
    public void testHash() {
        String key = "map";
        RedisUtil.del(key);
        Map<String, Object> map = new HashMap<String, Object>(){{
            put("name", "zhangSan");
            put("age", 20);
            put("sex", 1);
        }};
        assertTrue(RedisUtil.hSet(key, map));
        assertTrue(RedisUtil.hSet(key + "expire", map, 600));
        assertEquals(RedisUtil.hGet(key), map);
        assertEquals(RedisUtil.hGet(key, "name"), "zhangSan");
        assertEquals(RedisUtil.hGet(key, "age"), Integer.valueOf(20));
        assertEquals(RedisUtil.hGet(key, "sex"), Integer.valueOf(1));
        assertTrue(RedisUtil.hSet(key, "height", 180));
        assertEquals(RedisUtil.hGet(key, "height"), Integer.valueOf(180));
        assertTrue(RedisUtil.hHaskey(key, "height"));
        assertEquals(RedisUtil.hIncrement(key, "age", 1), Long.valueOf(21));
        assertEquals(RedisUtil.hIncrement(key, "sw", 30), Long.valueOf(30));
        assertEquals(RedisUtil.hDel(key, "name", "age"), Long.valueOf(2));
    }
    
    @Test
    public void testStringSet(){
        String key = "set";
        List<String> list = Arrays.asList("zhangSan", "siLi", "wangWu");
        RedisUtil.sSet(key, list.toArray());
        RedisUtil.sSet("setExpire", 60, list.toArray());
        assertThat(RedisUtil.sGet(key), hasItem("zhangSan"));
        assertTrue(RedisUtil.sHasValue(key, "siLi"));
        assertFalse(RedisUtil.sHasValue("mySet", "siLi"));
        assertEquals(RedisUtil.sSize(key), Long.valueOf(3));
        assertEquals(RedisUtil.sDel(key, "wangWu"), Long.valueOf(1));
    }
    
    @Test
    public void testObjectSet(){
        String key = "objSet";
        List<Person> list = Arrays.asList(
                new Person("zhangSan", 20, 1),
                new Person("liSi", 23, 2),
                new Person("wangWu", 30, 2)
                );
        assertThat(RedisUtil.sSet(key, list.toArray()), anything());
        assertThat(RedisUtil.sSet("setExpire", 60, list.toArray()), anything());
        assertThat(RedisUtil.sGet(key), hasItem(new Person("liSi", 23, 2)));
        assertTrue(RedisUtil.sHasValue(key, new Person("liSi", 23, 2)));
        assertEquals(RedisUtil.sSize(key), Long.valueOf(3));
        assertEquals(RedisUtil.sDel(key, new Person("liSi", 23, 2)), Long.valueOf(1));
    }
    
    @Test
    public void testStringList(){
        String key = "list";
        assertThat(RedisUtil.lSet(key, Arrays.asList("zhangSan", "liSi", "wangWu")), anything());
        assertThat(RedisUtil.lSet(key + "Expire", Arrays.asList("zhangSan", "liSi", "wangWu"), 60), anything());
        assertTrue(RedisUtil.lUpdate(key, 0, "dobe"));
        assertThat(RedisUtil.lGet(key), hasItem("dobe"));
        assertThat(RedisUtil.lGet(key, 1, 2), hasItem("liSi"));
        assertThat(RedisUtil.lGet(key, 1), equalTo("liSi"));
        assertThat(RedisUtil.lSize(key), equalTo(3L));
    }
    
    @Test
    public void testPattern() {
        List<String> keys = RedisUtil.keys("");
        System.out.println(keys);
    }
    
}

