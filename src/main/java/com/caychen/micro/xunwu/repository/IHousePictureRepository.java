package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.entity.HousePicture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:           Caychen
 * Date:             2019/9/8
 * Desc:
 */
@Repository
public interface IHousePictureRepository extends CrudRepository<HousePicture, Integer> {

    List<HousePicture> findAllByHouseId(Integer id);
}
