package com.caychen.micro.xunwu.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Data
@Entity
@Table(name = "house_tag")
public class HouseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "house_id")
    private Integer houseId;

    private String name;

    public HouseTag() {
    }

    public HouseTag(Integer houseId, String name) {
        this.houseId = houseId;
        this.name = name;
    }

}
