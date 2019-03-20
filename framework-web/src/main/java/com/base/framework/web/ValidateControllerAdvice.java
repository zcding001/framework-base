package com.base.framework.web;

import com.base.framework.core.models.ResponseEntity;
import com.base.framework.web.utils.ParamValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;


/**
*  验证参数绑定的的切面
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
@ControllerAdvice
public class ValidateControllerAdvice {
    /**
     * bean校验未通过异常
     */
    @ExceptionHandler(BindException.class)
    public String validExceptionHandler(BindException exception, WebRequest request, HttpServletResponse response) {
        //获取所有的异常
        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult.hasErrors()&&isAjaxRequest(request)) {
            //如果验证中存在错误,直接返回
            responseRequestBreak(response, ResponseEntity.error(ParamValidator.getParamsErroMessages(bindingResult)));
            return null;
        }
        return "/validError";
    }

    /**
    *  验证是否是ajax请求
    *  @param webRequest    请求
    *  @return boolean
    *  @author                  ：zc.ding@foxmail.com
    */
    private  boolean isAjaxRequest(WebRequest webRequest) {
        String requestedWith = webRequest.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }

    /**
    *  中断请求
    *  @param response  响应
    *  @param responseEntity    结果
    *  @author                  ：zc.ding@foxmail.com
    */
    protected void responseRequestBreak(HttpServletResponse response, ResponseEntity responseEntity) {
//        String responseStr = JsonUtils.toJson(responseEntity);
//        ResponseUtils.responseJson(response, responseStr);
    }


}