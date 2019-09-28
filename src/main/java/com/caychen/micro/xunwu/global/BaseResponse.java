package com.caychen.micro.xunwu.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Author:       Caychen
 * Date:         2019/9/7
 * Desc:
 */
@Data
@NoArgsConstructor
public class BaseResponse {

	private int code;

	private String message;

	private Object data;

	private boolean more;//数据是否有更多

	public BaseResponse(int code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static BaseResponse error(){
		return error(Status.INTERNAL_SERVER_ERROR.code, Status.INTERNAL_SERVER_ERROR.starndardMessage);
	}

	public static BaseResponse error(int code, String messag){
		return new BaseResponse(code, messag, null);
	}

	public static BaseResponse success(){
		return success(null);
	}

	public static BaseResponse success(Object data){
		return new BaseResponse(Status.SUCCESS.code, Status.SUCCESS.starndardMessage, data);
	}

	public static BaseResponse ofStatus(Status status){
		return new BaseResponse(status.code, status.starndardMessage, null);
	}


	@Getter
	@AllArgsConstructor
	public enum Status {
		SUCCESS(200, "OK"),
		BAD_REQUEST(400, "Bad Request"),
		NOT_FOUND(404, "Page Not Found"),
		INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

		PARAMETER_NOT_VALID(10000, "Parameter Not Valid"),
		OPERATION_NOT_SUPPORTED(10001, "Operation Not Supported"),
		NOT_LOGIN(10002, "Not Login"),
		NOT_FOUND_DATA(10003, "Don't have Data!"),
		FILE_UPLOAD_ERROR(10004, "File Upload Failed"),

		;

		private int code;
		private String starndardMessage;

	}
}
