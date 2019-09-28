package com.caychen.micro.xunwu.global.datatable;

import com.caychen.micro.xunwu.global.BaseResponse;
import lombok.Data;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Data
public class DataTableResponse extends BaseResponse {

	private int draw;

	private long recordsTotal;//总数

	private long recordsFiltered;//

	public DataTableResponse() {
	}

	public DataTableResponse(BaseResponse.Status status){
		this(status.getCode(), status.getStarndardMessage(), null);
	}

	public DataTableResponse(int code, String message, Object data) {
		super(code, message, data);
	}
}
