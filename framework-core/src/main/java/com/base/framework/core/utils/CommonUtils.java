package com.base.framework.core.utils;

import com.base.framework.core.commons.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
*  通用工具类
*  @since                   ：2018/8/10
*  @author                  ：zc.ding@foxmail.com
*/
public interface CommonUtils {
	/**
	*  校验list是否为空
	*  @param list 集合
	*  @return boolean
	*  @since                   ：2018/11/13
	*  @author                  ：zc.ding@foxmail.com
	*/
	static boolean isEmpty(List<?> list){
		return list == null || list.isEmpty();
	}
	
	/**
	 *  判断list非空
	 *  @param list 集合
	 *  @return         : boolean true：非空， false：空
	 *  @since          : 2017年7月20日 上午10:47:06 
	 *  @author         : zc.ding
	 */
	static boolean isNotEmpty(List<?> list){
		return !isEmpty(list);
	}
	
	

    /**
     * 组装用于获得submitToken的key
     * @author : zhichaoding@hongkun.com zc.ding
     * @return : String
     */
    static String getSubmitTokenKey() {
        return Constants.WEB_SUBMIT_TOKEN + getServerType();
    }

    /**
     * 获得服务类型是前端项目还是后台项目
     * @author : zhichaoding@hongkun.com zc.ding
     * @return : String
     */
    static String getServerType() {
        String serverType = PropertiesHolder.getProperty(Constants.SERVER_TYPE);
        if (StringUtils.isNotBlank(serverType) && !"null".equals(serverType)) {
            return "_" + serverType;
        }
        return "";
    }
}
