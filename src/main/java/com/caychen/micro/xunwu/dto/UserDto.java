package com.caychen.micro.xunwu.dto;

import lombok.Data;

/**
 * Author:       Caychen
 * Date:         2019/9/28
 * Desc:
 */
@Data
public class UserDto {

	private Integer id;

	private String name;

	private String avatar;

	private String phoneNumber;

	private String lastLoginTime;
}
