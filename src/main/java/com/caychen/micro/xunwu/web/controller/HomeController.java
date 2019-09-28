package com.caychen.micro.xunwu.web.controller;

import com.caychen.micro.xunwu.util.GeneratorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author:       Caychen
 * Date:         2019/9/7
 * Desc:
 */
@Controller
@RequestMapping("/")
@Api("首页")
public class HomeController {

	@RequestMapping({"/", "/index"})
	@ApiOperation("首页")
	public String index() {
		return "index";
	}

	@GetMapping("/logout/page")
	public String logout() {
		return "logout";
	}

	@Autowired
	private GeneratorUtil generatorUtil;

	@GetMapping("/get")
	@ResponseBody
	public String getorderno() {
		return generatorUtil.generatorOrderNo();
	}

}
