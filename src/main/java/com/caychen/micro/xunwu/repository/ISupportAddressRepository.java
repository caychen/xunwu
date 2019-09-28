package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.entity.SupportAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:           Caychen
 * Date:             2019/9/8
 * Desc:
 */
@Repository
public interface ISupportAddressRepository extends JpaRepository<SupportAddress, Integer> {

	/**
	 * 获取所有对应行政级别的信息
	 *
	 * @return
	 */
	List<SupportAddress> findAllByLevel(String level);

	List<SupportAddress> findAllByLevelAndBelongTo(String level, String cityEnName);

	SupportAddress findByEnNameAndLevel(String cityEnName, String level);

	SupportAddress findByEnNameAndBelongTo(String regionEnName, String enName);
}
