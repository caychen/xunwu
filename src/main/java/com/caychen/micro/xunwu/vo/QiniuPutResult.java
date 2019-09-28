package com.caychen.micro.xunwu.vo;

import lombok.Data;
import lombok.ToString;

/**
 * Author:       Caychen
 * Date:         2019/9/28
 * Desc:
 */
@Data
@ToString
public final class QiniuPutResult {

	public String key;

	public String hash;

	public String bucket;

	public int width;

	public int height;

}
