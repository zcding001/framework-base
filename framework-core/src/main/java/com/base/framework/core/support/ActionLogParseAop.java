package com.base.framework.core.support;

import com.base.framework.core.annotations.ActionLog;
import com.base.framework.core.utils.AopUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
*  操作日志
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
@Component
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE + 1)
public class ActionLogParseAop{

	private static final Logger logger = LoggerFactory.getLogger(ActionLogParseAop.class);
    //用于匹配msg中{args[0].id} 或 {args[1].state == 1 ? '禁用' : '解禁'}
    private final static Pattern pattern = Pattern.compile("([\\{ *args\\[\\d]+\\][^\\}]* *\\})");
    
	@Autowired
    ActionLogHandler actionLogHandler;
	
	private ExecutorService executorService = new ForkJoinPool(Runtime.getRuntime().availableProcessors(),
			ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

	@Around("@annotation(com.base.framework.core.annotations.ActionLog)")
	public Object around(ProceedingJoinPoint point) throws Throwable {
	    logger.info("target object is {}, execute method is ", point.getTarget().getClass().getName(), point.getSignature().getName());
		ActionLog actionLog = AopUtils.getAnnotation(point, ActionLog.class);
		if(actionLog != null) {
			final Object[] args = point.getArgs();
            executorService.execute(() ->{
			    try{
                    String msg = actionLog.msg();
                    Matcher m = pattern.matcher(msg);
                    while(m.find()) {
                        String eval = m.group();
                        if(eval.contains("?") && eval.contains(":")) {
                            String[] arr = eval.split("[=<>]++");
                            String tmp = AopUtils.getParamValue(arr[0].replace("{", "").replace("}", "").trim(), args);
                            msg = msg.replace(eval, AopUtils.getParamValue(eval.replace(arr[0], tmp).replace("}", "").trim()));
                        }else {
                            msg = msg.replace(eval, AopUtils.getParamValue(eval.replace("{", "").replace("}", "").trim(), args));
                        }
                    }
                    if(actionLogHandler != null){
                        actionLogHandler.handler(point.getTarget().getClass(), point.getSignature(), args, msg);
                    }
			    }catch(Exception e){
			        logger.error("parse action log fail, msg is {}", actionLog.msg(), e);
			    }
			});
		}
		return point.proceed();
	}
}
