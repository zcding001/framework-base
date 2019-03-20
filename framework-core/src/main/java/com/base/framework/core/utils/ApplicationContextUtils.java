package com.base.framework.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *  获得ApplicationContext和Bean的工具类
 * 					<p>在application.xml中配置如下节点
 * 						<bean id="applicationContextUtils" class="com.yirun.framework.core.utils.ApplicationContextUtils"/>
 * 					</p>
 * @author        : zc.ding@foxmail.com
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
 
    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }
 
    /**
     *  获取applicationContext对象
     *  @return         : ApplicationContext
     *  @since          : 2017年6月6日 下午6:05:33 
     *  @author         : zc.ding@foxmail.com
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
     
    /**
     *  根据bean的id来查找对象
     *  @param id       ：名称
     *  @return         : Object
     *  @since          : 2017年6月6日 下午6:05:41 
     *  @author         : zc.ding@foxmail.com
     */
	public static <T> T getBean(String id){
        return (T) applicationContext.getBean(id);
    }
     
    /**
     *  根据bean的class来查找对象
     *  @param clazz    ：对象类型
     *  @return         : T
     *  @since          : 2017年6月6日 下午6:05:52 
     *  @author         : zc.ding@foxmail.com
     */
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
    
    /**
     *  从上下文中通过name获得指定的实例对象
     *  @param name     ：名称
     *  @param clazz    ：对象类型
     *  @return         : T
     *  @since          : 2017年6月6日 下午6:06:00 
     *  @author         : zc.ding@foxmail.com
     */
    public static <T> T getBean(String name, Class<T> clazz){
    	return applicationContext.getBean(name, clazz);
    }

    /**
     *  根据bean的class来查找所有的对象(包括子类)
     *  @param clazz    ：对象类型
     *  @return         : Map<String,?>
     *  @since          : 2017年6月6日 下午6:06:08 
     *  @author         : zc.ding@foxmail.com
     */
    public static Map<String, ?> getBeansByClass(Class<?> clazz){
        return applicationContext.getBeansOfType(clazz);
    }
}
