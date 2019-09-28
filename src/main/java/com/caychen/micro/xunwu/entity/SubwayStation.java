package com.caychen.micro.xunwu.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Entity
@Table(name = "subway_station")
@Data
public class SubwayStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "subway_id")
    private Integer subwayId;

    private String name;

}
