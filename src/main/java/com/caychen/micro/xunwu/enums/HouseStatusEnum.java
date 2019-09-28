package com.caychen.micro.xunwu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Author:       Caychen
 * Date:         2019/9/28
 * Desc:
 */
@Getter
@AllArgsConstructor
public enum HouseStatusEnum {

	NOT_AUDITED(0),//未审核
	PASSED(1),//审核通过
	RENTED(2),//已出租
	DELETED(3),//已删除（逻辑删除）

	;

	private int value;
}
