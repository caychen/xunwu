package com.caychen.micro.xunwu.dto;

import lombok.Data;

import java.util.Date;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Data
public class HouseSubscribeDto {
    private Integer id;

    private Integer houseId;

    private Integer userId;

    private Integer adminId;

    // 预约状态 1-加入待看清单 2-已预约看房时间 3-看房完成
    private int status;

    private Date createTime;

    private Date lastUpdateTime;

    private Date orderTime;

    private String telephone;

    private String desc;

}
