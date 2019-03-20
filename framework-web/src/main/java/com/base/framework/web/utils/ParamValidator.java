package com.base.framework.web.utils;

import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

/**
*  统一处理参数验证类
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public interface ParamValidator {
    
    /**
    *  返回参数中的错误的提示信息
    *  @param bindingResult 绑定参数
    *  @return java.util.List
    *  @author                  ：zc.ding@foxmail.com
    */
    static List getParamsErroMessages(BindingResult bindingResult) {
       return  bindingResult.getAllErrors().stream().map((error) -> error.getDefaultMessage()).collect(Collectors.toList());
    }
}
