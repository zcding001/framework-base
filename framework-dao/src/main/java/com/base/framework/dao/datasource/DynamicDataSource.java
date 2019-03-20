package com.base.framework.dao.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
*  动态数据源
*  @since                   ：0.0.1
*  @author                  ：zc.ding@foxmail.com
*/
public class DynamicDataSource extends AbstractRoutingDataSource {

	public static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();

	@Override
	protected Object determineCurrentLookupKey() {
		return (String) CONTEXT_HOLDER.get();
	}
}
