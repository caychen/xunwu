package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {

	List<Role> findByUserId(Integer id);
}
