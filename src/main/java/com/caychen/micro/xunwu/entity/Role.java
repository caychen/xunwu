package com.caychen.micro.xunwu.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Table
@Entity(name = "role")
@Data
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_id")
	private Integer userId;

	private String name;
}
