package com.caychen.micro.xunwu.config;

import com.caychen.micro.xunwu.security.AuthProvider;
import com.caychen.micro.xunwu.security.LoginAuthFailHandler;
import com.caychen.micro.xunwu.security.LoginUrlEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Author:       Caychen
 * Date:         2019/9/6
 * Desc:
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(HttpSecurity http) throws Exception {

		//配置资源访问权限
		http.authorizeRequests()
				.antMatchers("/admin/login").permitAll()
				.antMatchers("/static/**").permitAll()
				.antMatchers("/user/login").permitAll()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")

				.and()
				.formLogin().loginProcessingUrl("/login")//配置角色登录处理入口
				.failureHandler(loginAuthFailHandler())//登录失败处理器

				.and()
				.logout()
				.logoutUrl("/logout")//注销的请求路径
				.logoutSuccessUrl("/logout/page")//注销成功之后的请求路径
				.deleteCookies("JSESSIONID")//删除JSESSIONID
				.invalidateHttpSession(true)//将session是系哦啊

				.and()
				.exceptionHandling()
				.authenticationEntryPoint(loginUrlEntryPoint())//配置登录入口
				.accessDeniedPage("/403")//无权访问之后的页面

		;

		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();//开启同源策略


	}

	/**
	 * 自定义认证策略
	 */

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//				.passwordEncoder(passwordEncoder())//spring security5需要指定passwordEncoder，并对密码进行加密
//				.withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN").and();

		auth.authenticationProvider(authProvider()).eraseCredentials(true);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthProvider authProvider() {
		return new AuthProvider();
	}

	@Bean
	public LoginUrlEntryPoint loginUrlEntryPoint() {
		//参数为默认的登录入口
		return new LoginUrlEntryPoint("/user/login");
	}

	@Bean
	public LoginAuthFailHandler loginAuthFailHandler() {
		return new LoginAuthFailHandler(loginUrlEntryPoint());
	}
}
