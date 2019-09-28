package com.caychen.micro.xunwu.repository;

import com.caychen.micro.xunwu.entity.HouseSubscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Author:           Caychen
 * Date:             2019/9/8
 * Desc:
 */
@Repository
public interface IHouseSubscribeRespository extends PagingAndSortingRepository<HouseSubscribe, Integer>{

    HouseSubscribe findByHouseIdAndUserId(Integer houseId, Integer loginUserId);

    Page<HouseSubscribe> findAllByUserIdAndStatus(Integer userId, int status, Pageable pageable);

    Page<HouseSubscribe> findAllByAdminIdAndStatus(Integer adminId, int status, Pageable pageable);

    HouseSubscribe findByHouseIdAndAdminId(Integer houseId, Integer adminId);

    @Modifying
    @Query("update HouseSubscribe as subscribe set subscribe.status = :status where subscribe.id = :id")
    void updateStatus(@Param(value = "id") Integer id, @Param(value = "status") int status);
}
