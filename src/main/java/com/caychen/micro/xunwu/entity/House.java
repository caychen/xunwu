package com.caychen.micro.xunwu.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Entity
@Table(name = "house")
@Data
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(name = "admin_id")
    private Integer adminId;

    private int price;

    private int area;

    private int room;

    private int parlour;

    private int bathroom;

    private int floor;

    @Column(name = "total_floor")
    private int totalFloor;

    @Column(name = "watch_times")
    private int watchTimes;

    @Column(name = "build_year")
    private int buildYear;

    private int status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    @Column(name = "city_en_name")
    private String cityEnName;

    @Column(name = "region_en_name")
    private String regionEnName;

    private String street;

    private String district;

    private int direction;

    private String cover;

    @Column(name = "distance_to_subway")
    private int distanceToSubway;
}
