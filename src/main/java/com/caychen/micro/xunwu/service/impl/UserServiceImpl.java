package com.caychen.micro.xunwu.service.impl;

import com.caychen.micro.xunwu.dto.UserDto;
import com.caychen.micro.xunwu.entity.Role;
import com.caychen.micro.xunwu.entity.User;
import com.caychen.micro.xunwu.global.ServiceResult;
import com.caychen.micro.xunwu.repository.IRoleRepository;
import com.caychen.micro.xunwu.repository.IUserRepository;
import com.caychen.micro.xunwu.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public User findUserByName(String name) {
		User user = userRepository.findUserByName(name);

		if (user == null) {
			return null;
		}

		List<Role> roleList = roleRepository.findByUserId(user.getId());
		if (CollectionUtils.isEmpty(roleList)) {
			throw new DisabledException("非法权限");
		}

		List<GrantedAuthority> authorities =
				roleList.stream().map(role ->
						new SimpleGrantedAuthority("ROLE_" + role.getName())
				).collect(Collectors.toList());
		user.setAuthorityList(authorities);
		return user;
	}

	@Override
	public ServiceResult<UserDto> findById(Integer userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser == null || !optionalUser.isPresent()) {
			return ServiceResult.notFound();
		}
		UserDto userDTO = modelMapper.map(optionalUser.get(), UserDto.class);
		return ServiceResult.success(userDTO);
	}
}
