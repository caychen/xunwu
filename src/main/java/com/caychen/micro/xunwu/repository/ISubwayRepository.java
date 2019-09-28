package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.entity.Subway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:           Caychen
 * Date:             2019/9/8
 * Desc:
 */
@Repository
public interface ISubwayRepository extends JpaRepository<Subway, Integer> {

	List<Subway> findAllByCityEnName(String cityEnName);
}
