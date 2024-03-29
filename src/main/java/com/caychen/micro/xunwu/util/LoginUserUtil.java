package com.caychen.micro.xunwu.util;

import com.caychen.micro.xunwu.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
public class LoginUserUtil {

	public static User load(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal != null && principal instanceof User){
			return (User)principal;
		}
		return null;
	}

	public static Integer getUserId(){
		User user = load();
		if(user == null){
			return  -1;
		}

		return user.getId();
	}
}
