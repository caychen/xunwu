package com.caychen.micro.xunwu.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:        业务层多结果集的返回类
 */
@Data
@AllArgsConstructor
public class ServiceMultiResult<T> {

	private int total;

	private List<T> result;

	public int getResultSize(){
		return CollectionUtils.isEmpty(result) ? 0 : result.size();
	}

	public static ServiceMultiResult empty(){
		return new ServiceMultiResult(0, null);
	}
}
