package com.base.framework.core.utils.mail;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
*  TODO
*  @date                    ：2018/8/10
*  @author                  ：zc.ding@foxmail.com
*/
public class MailAuthenticator extends Authenticator {
    /**
     * 用户账号
     */
    private String userName = null;
    /**
     * 用户口令
     */
    private String password = null;

    /**
     * @param userName
     * @param password
     */
    public MailAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * 身份验证
     *
     * @return
     */
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}

