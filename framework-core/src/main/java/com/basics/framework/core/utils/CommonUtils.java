package com.basics.framework.core.utils;

import com.basics.framework.core.commons.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
*  通用工具类
*  @date                    ：2018/8/10
*  @author                  ：zc.ding@foxmail.com
*/
public class CommonUtils {
	/**
	*  校验list是否为空
	*  @param list
	*  @return boolean
	*  @date                    ：2018/11/13
	*  @author                  ：zc.ding@foxmail.com
	*/
	public static boolean isEmpty(List<?> list){
		return list == null || list.isEmpty();
	}
	
	/**
	 *  @Description    : 判断list非空
	 *  @Method_Name    : isNotEmpty
	 *  @param list
	 *  @return         : boolean true：非空， false：空
	 *  @Creation Date  : 2017年7月20日 上午10:47:06 
	 *  @Author         : zc.ding
	 */
	public static boolean isNotEmpty(List<?> list){
		return !isEmpty(list);
	}
	
	/**
	*  输出异常信息和异常堆栈信息
	*  @param throwable 异常对象
	*  @return java.lang.String
	*  @date                    ：2018/11/13
	*  @author                  ：zc.ding@foxmail.com
	*/
	public static String printStackTraceToString(Throwable throwable) {
		StringWriter stringWriter = null;
		PrintWriter printWriter = null;
		try{
			stringWriter = new StringWriter();
			printWriter = new PrintWriter(stringWriter,true);
			throwable.printStackTrace(printWriter);
		}finally {
			if (stringWriter != null){
				try{
					stringWriter.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
			if (printWriter != null){
				printWriter.close();
			}
		}
		return stringWriter.getBuffer().toString();
	}

    /**
     * @return : String
     * @Description : 组装用于获得submitToken的key
     * @Method_Name : getSubmitTokenKey
     * @Creation Date : 2017年12月7日 上午10:51:54
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    public static String getSubmitTokenKey() {
        return Constants.WEB_SUBMIT_TOKEN + getServerType();
    }

    /**
     * @return : String
     * @Description : 获得服务类型是前端项目还是后台项目
     * @Method_Name : getServerType
     * @Creation Date : 2017年12月6日 下午4:40:09
     * @Author : zhichaoding@hongkun.com zc.ding
     */
    public static String getServerType() {
        String serverType = PropertiesHolder.getProperty(Constants.SERVER_TYPE);
        if (StringUtils.isNotBlank(serverType) && !"null".equals(serverType)) {
            return "_" + serverType;
        }
        return "";
    }
}
