package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.entity.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:           Caychen
 * Date:             2019/9/8
 * Desc:
 */
@Repository
public interface ISubwayStationRepository extends JpaRepository<SubwayStation, Integer> {

	List<SubwayStation> findAllBySubwayId(Integer subwayId);
}
