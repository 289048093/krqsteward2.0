package com.ejushang.steward.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 验证码校验
 * User: liubin
 * Date: 14-02-07
 */
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;

	public String getCaptcha() {
        return captcha;
	}

	public UsernamePasswordCaptchaToken() {
		super();
	}

	public UsernamePasswordCaptchaToken(String username, char[] password,
			boolean rememberMe, String host, String captcha) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
	}

}