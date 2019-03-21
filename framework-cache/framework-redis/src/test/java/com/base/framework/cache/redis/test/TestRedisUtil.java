package com.base.framework.cache.redis.test;

import com.base.framework.cache.redis.AppServerApplication;
import com.base.framework.cache.redis.RedisUtil;
import com.base.framework.cache.redis.test.model.Person;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * ReidsUtil测试类
 * @author zc.ding
 * @since  2019/3/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRedisUtil {

    @Test()
    public void testSetString(){
        assertTrue(RedisUtil.set("sbr-key", "hello"));
    }

    @Test
    public void testSetWithExpire() {
        assertTrue(RedisUtil.set("junit", "hello wrold", 30));
    }
    
    @Test
    public void testGetString(){
        assertEquals(RedisUtil.get("sbr-key"), "hello");
    }
    
    @Test
    public void testSetObject(){
        Person person = new Person("person", 20, 1);
        assertTrue(RedisUtil.set("person", person));
    }
    
    @Test
    public void testGetObjet(){
        Person person = new Person("person", 20, 1);
        assertEquals(RedisUtil.get("person"), person);
    }
    
    @Test
    public void testExpire() {
        assertTrue(RedisUtil.expire("sbr-key", 300));
    }
    
    @Test
    public void testGetExpire() {
        assertTrue(RedisUtil.getExpire("sbr-key") > 0);
    }

    @Test
    public void testHasKey() {
        assertTrue(RedisUtil.hasKey("person"));
    }

    @Test
    public void testIncrement() {
        RedisUtil.set("number", 2);
        assertEquals(RedisUtil.increment("number", 2), Long.valueOf(4));
        assertEquals(RedisUtil.increment("number", -1), Long.valueOf(3));
    }
    
    @Test
    public void testDel() {
        assertTrue(RedisUtil.del("name00"));
    }

    @Test
    public void testDelBatch() {
        assertEquals(RedisUtil.delBatch(Arrays.asList("name00", "name01", "name02")), Long.valueOf(2));
    }


    @Test
    public void testMset() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "dobe");
        map.put("age", 20);
        map.put("sex", 1);
        assertTrue(RedisUtil.mSet("pmap", map));
    }
    
    private static Map<String, Object> getMap(){
        return new HashMap<String, Object>(){{
            put("name", "lisi");
            put("age", 30);
            put("sex", 2);
        }};
    }
    
    @Test
    public void testMsetWithExpire() {
        assertTrue(RedisUtil.mSet("lisi", getMap(), 600));
    }
    
    @Test
    public void testMget() {
        assertEquals(RedisUtil.mGet("lisi"), getMap());
    }

    @Test
    public void testMgetByItem() {
        assertTrue(RedisUtil.mSet("lisi", getMap()));
        assertEquals(RedisUtil.mGet("lisi", "name"), "lisi");
        assertEquals(RedisUtil.mGet("lisi", "age"), Integer.valueOf(30));
        assertEquals(RedisUtil.mGet("lisi", "sex"), Integer.valueOf(2));
    }

    @Test
    public void testMsetKeyItem() {
        assertTrue(RedisUtil.mSet("pmap", "height", 180));
        assertEquals(RedisUtil.mGet("pmap", "height"), Integer.valueOf(180));
    }

    @Test
    public void testMhasItem() {
        assertTrue(RedisUtil.mHaskey("pmap", "height"));
//        assertTrue(RedisUtil.mhaskey("pmap", "girlfriend"));
    }

    @Test
    public void testMincrement() {
        assertEquals(RedisUtil.mIncrement("pmap", "age", 1), Long.valueOf(21));
        assertEquals(RedisUtil.mIncrement("pmap", "sw", 30), Long.valueOf(30));
    }

    @Test
    public void testMdel() {
        assertTrue(RedisUtil.mSet("wangwu", getMap()));
        assertEquals(RedisUtil.mDel("wangwu", "name", "age"), Long.valueOf(2));
    }
    
}

