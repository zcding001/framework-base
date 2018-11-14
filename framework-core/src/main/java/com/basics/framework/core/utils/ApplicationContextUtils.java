package com.basics.framework.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description   : 获得ApplicationContext和Bean的工具类
 * 					<p>在application.xml中配置如下节点
 * 						<bean id="applicationContextUtils" class="com.yirun.framework.core.utils.ApplicationContextUtils"/>
 * 					</p>
 * @Project       : finance-user-models
 * @Program Name  : com.yirun.finance.user.utils.ApplicationContextUtils.java
 * @Author        : zc.ding@foxmail.com
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
 
    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }
 
    /**
     *  @Description    : 获取applicationContext对象
     *  @Method_Name    : getApplicationContext
     *  @return
     *  @return         : ApplicationContext
     *  @Creation Date  : 2017年6月6日 下午6:05:33 
     *  @Author         : zc.ding@foxmail.com
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
     
    /**
     *  @Description    : 根据bean的id来查找对象
     *  @Method_Name    : getBean
     *  @param id
     *  @return         : Object
     *  @Creation Date  : 2017年6月6日 下午6:05:41 
     *  @Author         : zc.ding@foxmail.com
     */
	public static <T> T getBean(String id){
        return (T) applicationContext.getBean(id);
    }
     
    /**
     *  @Description    : 根据bean的class来查找对象
     *  @Method_Name    : getBean
     *  @param clazz
     *  @return         : T
     *  @Creation Date  : 2017年6月6日 下午6:05:52 
     *  @Author         : zc.ding@foxmail.com
     */
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
    
    /**
     *  @Description    : 从上下文中通过name获得指定的实例对象
     *  @Method_Name    : getBean
     *  @param name
     *  @param clazz
     *  @return         : T
     *  @Creation Date  : 2017年6月6日 下午6:06:00 
     *  @Author         : zc.ding@foxmail.com
     */
    public static <T> T getBean(String name, Class<T> clazz){
    	return applicationContext.getBean(name, clazz);
    }

    /**
     *  @Description    : 根据bean的class来查找所有的对象(包括子类)
     *  @Method_Name    : getBeansByClass
     *  @param clazz
     *  @return
     *  @return         : Map<String,?>
     *  @Creation Date  : 2017年6月6日 下午6:06:08 
     *  @Author         : zc.ding@foxmail.com
     */
    public static Map<String, ?> getBeansByClass(Class<?> clazz){
        return applicationContext.getBeansOfType(clazz);
    }
}
