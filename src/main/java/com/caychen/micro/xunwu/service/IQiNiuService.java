package com.caychen.micro.xunwu.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

import java.io.File;
import java.io.InputStream;

/**
 * Author:           Caychen
 * Date:             2019/9/28
 * Desc:
 */
public interface IQiNiuService {

	Response uploadFile(File file) throws QiniuException;

	Response uploadFile(InputStream inputStream) throws QiniuException;

	Response delete(String key) throws QiniuException;
}
