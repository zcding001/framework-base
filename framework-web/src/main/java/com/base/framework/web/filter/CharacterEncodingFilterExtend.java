package com.base.framework.web.filter;

import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
*  编码过滤，后期可扩展
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class CharacterEncodingFilterExtend extends CharacterEncodingFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		super.doFilterInternal(request, response, filterChain);
	}
}
