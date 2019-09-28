package com.caychen.micro.xunwu.service;

import com.caychen.micro.xunwu.dto.UserDto;
import com.caychen.micro.xunwu.entity.User;
import com.caychen.micro.xunwu.global.ServiceResult;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
public interface IUserService {

	User findUserByName(String name);

	ServiceResult<UserDto> findById(Integer adminId);
}
