package com.basics.framework.core.utils;

import com.basics.framework.core.exceptions.InnerErrorException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.AnnotationUtils;

import javax.script.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 加载AOP的调用方法的接口
 * @author 	 zc.ding
 * @since 	 2017年5月21日
 * @version  1.1
 */
public interface AopUtils {

	static final Logger LOG = LoggerFactory.getLogger(AopUtils.class);
	
	/**
	 * 解析代理方法的上的注解
	 * @author	 zc.ding
	 * @since 	 2017年5月21日
	 * @param point 切入点
	 * @param clazz 代理对象
	 * @return T
	 * @throws NoSuchMethodException    未找到方法
	 */
	static <T extends Annotation> T getAnnotation(JoinPoint point, Class<T> clazz) throws NoSuchMethodException{
		return AnnotationUtils.findAnnotation(getTargetMethod(point), clazz);
	}
	
	/**
	 * 解析代理类上的注解
	 * @author	 zc.ding
	 * @since 	 2017年5月22日
	 * @param point 切入点
	 * @param clazz 代理对象
	 * @return T
	 * @throws NoSuchMethodException    未找到方法
	 */
    static <T extends Annotation> T getClassAnnotation(JoinPoint point, Class<T> clazz) throws NoSuchMethodException{
		return AnnotationUtils.findAnnotation(point.getTarget().getClass(), clazz);
	}

	/**
	 * 解析代理方法的名称
	 * @author	 zc.ding
	 * @since 	 2017年5月21日
	 * @param joinPoint 切入点
	 * @return Method
	 * @throws NoSuchMethodException    未找到方法
	 */
    static Method getTargetMethod(JoinPoint joinPoint) throws NoSuchMethodException{
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		return ReflectUtils.findDeclaredMethod(
				joinPoint.getTarget().getClass(), 
				joinPoint.getSignature().getName(),
				methodSignature.getParameterTypes()
				);
	}

	/**
	 * 从参数中获得注解中属性的值
	 * @author	 zc.ding
	 * @since 	 2017年5月21日
	 * @param key
	 * @param args
	 * @return  String
	 */
    static String getParamValue(String key, Object[] args) {
		ScriptEngine engine = (new ScriptEngineManager()).getEngineByName("js");
		ScriptContext context = new SimpleScriptContext();
		context.setAttribute("args", args, ScriptContext.ENGINE_SCOPE);
		try {
			String value = String.valueOf(engine.eval(key, context));
			return (value == null || "null".equalsIgnoreCase(value)) ? "" : value;
		} catch (ScriptException e) {
			if(LOG.isDebugEnabled()){
				LOG.debug("fail to eval express", e);
			}
			throw new InnerErrorException("fail to eval express", e);
		}
	}
	
	/**
	 *  执行公式 例如  a == b ? "1" : "0"
	 *  @param formula  公式
	 *  @return         : String
	 *  @since          : 2017年12月5日 下午3:10:17 
	 *  @author         : zhichaoding@hongkun.com zc.ding
	 */
    static String getParamValue(String formula) {
		ScriptEngine engine = (new ScriptEngineManager()).getEngineByName("js");
		try {
			return (String) engine.eval(formula);
		} catch (ScriptException e) {
			if(LOG.isDebugEnabled()){
				LOG.debug("fail to eval express", e);
			}
			throw new InnerErrorException("fail to eval express", e);
		}
	}
}
