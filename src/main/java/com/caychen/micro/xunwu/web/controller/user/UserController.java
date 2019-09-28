package com.caychen.micro.xunwu.web.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Controller
@RequestMapping("/user")
@Api(tags = "普通用户页面")
public class UserController {

	@GetMapping("/center")
	public String center(){
		return "/user/center";
	}

	@GetMapping("/login")
	@ApiOperation("普通用户登录入口")
	public String login(){
		return "/user/login";
	}
}
