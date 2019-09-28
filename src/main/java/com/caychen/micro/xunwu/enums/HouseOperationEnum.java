package com.caychen.micro.xunwu.enums;

/**
 * Author:       Caychen
 * Date:         2019/9/28
 * Desc:
 */
public class HouseOperationEnum {

	public static final int PASSED = 1; // 通过审核
	public static final int PULL_OUT = 2; //下架，需要重新审核
	public static final int DELETE = 3; //逻辑删除
	public static final int RENT = 4;//出租
}
