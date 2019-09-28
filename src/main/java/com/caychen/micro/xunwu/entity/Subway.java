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
@Table(name = "subway")
@Data
public class Subway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "city_en_name")
    private String cityEnName;

}
