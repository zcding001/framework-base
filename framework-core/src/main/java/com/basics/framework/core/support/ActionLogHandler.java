package com.basics.framework.core.support;

import org.aspectj.lang.Signature;

/**
*  ActionLog处理器
 *      如需要记录操作日志，只需要实现此接口即可
*  @date                    ：2018/8/10
*  @author                  ：zc.ding@foxmail.com
*/
public interface ActionLogHandler {

    /**
    *  ActionLog处理方法
    *  @param clazz 切入的对象
    *  @param signature 切入的方法
    *  @param args  入参
    *  @param msg   解析后的收据
    *  @date                    ：2018/11/14
    *  @author                  ：zc.ding@foxmail.com
    */
    void handler(Class<?> clazz, Signature signature, Object[] args, String msg);
}
