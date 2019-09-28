package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.entity.HouseTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:           Caychen
 * Date:             2019/9/8
 * Desc:
 */
@Repository
public interface IHouseTagRepository extends CrudRepository<HouseTag, Integer> {
    HouseTag findByNameAndHouseId(String name, Integer houseId);

    List<HouseTag> findAllByHouseId(Integer id);

    List<HouseTag> findAllByHouseIdIn(List<Integer> houseIds);
}
