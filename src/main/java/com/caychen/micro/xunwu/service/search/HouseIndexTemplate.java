package com.caychen.micro.xunwu.service.search;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Author:       Caychen
 * Date:         2019/9/28
 * Desc:        索引模版结构，以后统一使用该类与ES交互
 */
@Data
public class HouseIndexTemplate {

	private Integer houseId;

	private String title;

	private Integer price;

	private Integer area;

	private Date createTime;

	private Date lastUpdateTime;

	private String cityEnName;

	private String regionEnName;

	private Integer direction;

	private Integer distanceToSubway;

	private String subwayLineName;

	private String subwayStationName;

	private String street;

	private String district;

	private String description;

	private String layoutDesc;

	private String traffic;

	private String routeService;

	private Integer rentWay;

	private List<String> tags;

}
