package com.base.framework.core.support;

import com.base.framework.core.commons.Constants;
import com.base.framework.core.annotations.Token;
import com.base.framework.core.models.ResponseEntity;
import com.base.framework.core.utils.AopUtils;
import com.base.framework.core.utils.CommonUtils;
import com.base.framework.core.utils.CookieUtil;
import com.base.framework.core.utils.HttpSessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * token切面，添加、验证、移除token<br/>
 * 逻辑：<br/>
 * 1、js中通过ajax，添加submitToken到cookie中，即使提交的请求中参数中没有携带，那么服务端会从cookie中获取<br/>
 * 2、先获得参数或是cookie中的submitToken中，服务端先获得session中的submitToken值，<br/>
 * 如果没有以submitToken为key从redis中加载，然后进行对比，结果存储到request中
 *
 * @author zc.ding
 * @since 2017年5月21日
 */
@Component
@Aspect
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class TokenValidateAop {

    @Autowired(required = false)
    private TokenHandler tokenHandler;
    
    private static final Logger logger = LoggerFactory.getLogger(TokenValidateAop.class);

    @Around("@annotation(com.base.framework.core.annotations.Token)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Token token = AopUtils.getAnnotation(point, Token.class);
        String tokenValue = HttpSessionUtil.getRequest().getParameter(Constants.WEB_SUBMIT_TOKEN);
        logger.info("client params submit token value: {}", tokenValue);
        if (token != null) {
            if(!this.validateToken(token, tokenValue)){
            	return new ResponseEntity<>(Constants.ERROR, "无效的token参数");
            }
        }
        return point.proceed();
    }

    /**
     * 验证Token
     * @param token
     * @param tokenValue 
     * @author zc.ding
     * @since 2017年5月21日
     */
    private boolean validateToken(Token token, String tokenValue) {
    	boolean flag = true;
    	Token.Ope type = token.operate();
        //加载cookie中的submitToken
        String submitTokenKey = CookieUtil.getCookieValue(CommonUtils.getSubmitTokenKey());
        logger.info("get submit token from cookie is {}", submitTokenKey);
        switch (type) {
            case ADD:
                //添加
                tokenHandler.refreshSumbToken();
                break;
            case REFRESH:
                //验证&刷新
                flag = doValidateToken(submitTokenKey, tokenValue);
                tokenHandler.refreshSumbToken();
                break;
            case REMOVE:
                //删除
            	flag = doValidateToken(submitTokenKey, tokenValue);
                break;
            default:
                break;
        }
        if(StringUtils.isNotBlank(submitTokenKey)) {
            //删除使用后服务端存储的token
            tokenHandler.delSumbTokenFromServer(submitTokenKey);
        }
        return flag;
    }

    /**
     * 验证token是否有效
     *
     * @param submitTokenKey 服务端缓存token值的的key
     * @param tokenValue     客户端传递的token值
     * @author zc.ding
     * @since 2017年5月21日
     */
    private boolean doValidateToken(String submitTokenKey, String tokenValue) {
        //获得服务端存储的submitToken
        String tv = HttpSessionUtil.getAttrFromSession(CommonUtils.getSubmitTokenKey());
        // 删除使用过的session中的submitToken
        HttpSessionUtil.removeAttrFromSession(CommonUtils.getSubmitTokenKey());
        if (StringUtils.isBlank(tv) && StringUtils.isNotBlank(submitTokenKey)) {
            tv = tokenHandler.getSumbTokenFromServer(submitTokenKey);
        }
        //获得客户端传过来的submitToken
        String tokenValueTmp = tokenValue;
        if (StringUtils.isBlank(tokenValueTmp) && StringUtils.isNotBlank(submitTokenKey)) {
            tokenValueTmp = submitTokenKey;
        }
        if (StringUtils.isBlank(tv) || StringUtils.isBlank(tokenValueTmp) || !tv.equals(tokenValueTmp)) {
            HttpSessionUtil.addAttrToRequest(Constants.WEB_SUBMIT_TOKEN_STATUS, "0");
            logger.info("submit_token is invalid. value : {}", tokenValueTmp);
            return false;
        } else {
            logger.info("submit_token verify successfully");
            return true;
        }
    }
}
