package com.caychen.micro.xunwu.global;

import lombok.Data;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:        业务层多结果集的返回类
 */
@Data
public class ServiceResult<T> {

	private boolean success;

	private String message;

	private T result;

	public ServiceResult() {
	}

	public ServiceResult(boolean success) {
		this.success = success;
	}

	public ServiceResult(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public ServiceResult(boolean success, String message, T result) {
		this.success = success;
		this.message = message;
		this.result = result;
	}

	public static ServiceResult notFound() {
		return new ServiceResult(true, "查无数据");
	}

	public static <T> ServiceResult success() {
		return success(null);
	}


	public static <T> ServiceResult success(T result) {
		ServiceResult<Object> serviceResult = new ServiceResult<>(true);
		serviceResult.setResult(result);
		return serviceResult;
	}
}
