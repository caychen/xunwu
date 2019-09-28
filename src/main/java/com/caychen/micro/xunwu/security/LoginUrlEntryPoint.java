package com.caychen.micro.xunwu.security;

import com.google.common.collect.Maps;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:        基于角色的登录入口控制
 */
public class LoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {

	//配置不同的登录入口
	private final Map<String, String> authEntryPoint;

	private PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * @param loginFormUrl URL where the login page can be found. Should either be
	 *                     relative to the web-app context path (include a leading {@code /}) or an absolute
	 *                     URL.
	 */
	public LoginUrlEntryPoint(String loginFormUrl) {
		super(loginFormUrl);

		authEntryPoint = Maps.newHashMap();
		authEntryPoint.put("/user/**", "/user/login");
		authEntryPoint.put("/admin/**", "/admin/login");
	}

	/**
	 * 根据请求跳转到指定的页面，父类是默认使用loginFormUrl
	 *
	 * @param request
	 * @param response
	 * @param exception
	 * @return
	 */
	@Override
	protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
		//将context替换成空串
		String uri = request.getRequestURI().replace(request.getContextPath(), "");

		for (Map.Entry<String, String> entry : authEntryPoint.entrySet()) {
			if (this.pathMatcher.match(entry.getKey(), uri)) {
				//查找对应角色的登录入口
				return entry.getValue();
			}
		}

		return super.determineUrlToUseForThisRequest(request, response, exception);
	}
}
