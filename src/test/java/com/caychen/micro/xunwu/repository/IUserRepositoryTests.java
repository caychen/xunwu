package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.MicroXunwuApplicationTests;
import com.caychen.micro.xunwu.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Author:       Caychen
 * Date:         2019/9/7
 * Desc:
 */
public class IUserRepositoryTests extends MicroXunwuApplicationTests {

	@Autowired
	private IUserRepository IUserRepository;

	@Test
	public void testFind() {
		Optional<User> user = IUserRepository.findById(3);
		if (user.isPresent()) {
			Assert.assertEquals("waliwali", user.get().getName());
		} else {
			System.out.println("未找到数据");
		}
	}
}
