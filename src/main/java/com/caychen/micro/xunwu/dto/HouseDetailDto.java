package com.caychen.micro.xunwu.dto;

import lombok.Data;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Data
public class HouseDetailDto {
    private String description;

    private String layoutDesc;

    private String traffic;

    private String roundService;

    private int rentWay;

    private Integer adminId;

    private String address;

    private Integer subwayLineId;

    private Integer subwayStationId;

    private String subwayLineName;

    private String subwayStationName;
}
