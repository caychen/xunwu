package com.caychen.micro.xunwu.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:        登录失败处理器
 */
public class LoginAuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

	private LoginUrlEntryPoint loginUrlEntryPoint;

	public LoginAuthFailHandler(LoginUrlEntryPoint loginUrlEntryPoint) {
		this.loginUrlEntryPoint = loginUrlEntryPoint;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		//获取目标跳转
		String targetUrl = loginUrlEntryPoint.determineUrlToUseForThisRequest(request, response, exception);

		//加上异常信息
		targetUrl += "?" + exception.getMessage();

		super.setDefaultFailureUrl(targetUrl);
		super.onAuthenticationFailure(request, response, exception);
	}
}
