package com.caychen.micro.xunwu.service.impl;

import com.caychen.micro.xunwu.properties.QiNiuProperties;
import com.caychen.micro.xunwu.service.IQiNiuService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

/**
 * Author:       Caychen
 * Date:         2019/9/28
 * Desc:
 */
@Service
public class QiNiuServiceImpl implements IQiNiuService, InitializingBean {

	@Autowired
	private UploadManager uploadManager;

	@Autowired
	private QiNiuProperties qiNiuProperties;

	@Autowired
	private Auth auth;

	@Autowired
	private BucketManager bucketManager;

	private StringMap policyMap;

	@Override
	public Response uploadFile(File file) throws QiniuException {
		Response response = this.uploadManager.put(file, null, getUploadToken());

		int retry = 0;
		while(response.needRetry() && retry < 3){
			response = this.uploadManager.put(file, null, getUploadToken());
			retry++;
		}

		return response;
	}

	private String getUploadToken() {
		return this.auth.uploadToken(qiNiuProperties.getBucket(), null);
	}

	@Override
	public Response uploadFile(InputStream inputStream) throws QiniuException {
		Response response = this.uploadManager.put(inputStream, null, getUploadToken(), null, null);

		int retry = 0;
		while(response.needRetry() && retry < 3){
			response = this.uploadManager.put(inputStream, null, getUploadToken(), null, null);
			retry++;
		}

		return response;
	}

	@Override
	public Response delete(String key) throws QiniuException {
		Response response = bucketManager.delete(qiNiuProperties.getBucket(), key);
		int retry = 0;
		while(response.needRetry() && retry < 3){
			response = bucketManager.delete(qiNiuProperties.getBucket(), key);
			retry++;
		}
		return response;
	}

	//上传之后的回调设置，详见https://developer.qiniu.com/kodo/manual/1235/vars
	@Override
	public void afterPropertiesSet() throws Exception {
		this.policyMap = new StringMap();
		policyMap.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width),\"height\":${imageInfo.height}}" );
	}
}
