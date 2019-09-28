package com.caychen.micro.xunwu.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Data
public class HouseDto {

    private Integer id;

    private String title;

    private int price;

    private int area;

    private int direction;

    private int room;

    private int parlour;

    private int bathroom;

    private int floor;

    private Integer adminId;

    private String district;

    private int totalFloor;

    private int watchTimes;

    private int buildYear;

    private int status;

    private Date createTime;

    private Date lastUpdateTime;

    private String cityEnName;

    private String regionEnName;

    private String street;

    private String cover;

    private int distanceToSubway;

    private HouseDetailDto houseDetail;

    private List<String> tags;

    private List<HousePictureDto> pictures;

    private int subscribeStatus;
}
