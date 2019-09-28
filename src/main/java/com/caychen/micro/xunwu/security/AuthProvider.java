package com.caychen.micro.xunwu.security;

import com.caychen.micro.xunwu.entity.User;
import com.caychen.micro.xunwu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
public class AuthProvider implements AuthenticationProvider {

	@Autowired
	private IUserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String credentials = (String) authentication.getCredentials();

		User user = userService.findUserByName(name);
		if (user == null) {
			throw new AuthenticationCredentialsNotFoundException("Auth Error");
		}

		boolean bMatch = passwordEncoder.matches(credentials, user.getPassword());
		if (bMatch) {
			return new UsernamePasswordAuthenticationToken(user, credentials, user.getAuthorities());
		}
		throw new BadCredentialsException("Auth Error");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}
