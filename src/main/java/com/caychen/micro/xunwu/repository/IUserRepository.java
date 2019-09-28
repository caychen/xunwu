package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author:       Caychen
 * Date:         2019/9/7
 * Desc:
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

	User findUserByName(String name);
}
