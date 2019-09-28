package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.entity.HouseDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:           Caychen
 * Date:             2019/9/8
 * Desc:
 */
@Repository
public interface IHouseDetailRepository extends CrudRepository<HouseDetail, Integer>{

    HouseDetail findByHouseId(Integer houseId);

    List<HouseDetail> findAllByHouseIdIn(List<Integer> houseIds);
}
