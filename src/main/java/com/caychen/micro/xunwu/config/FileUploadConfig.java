package com.caychen.micro.xunwu.config;

import com.caychen.micro.xunwu.properties.QiNiuProperties;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:        文件上传配置
 */

@Configuration
@EnableConfigurationProperties(QiNiuProperties.class)
//@ConditionalOnClass({Servlet.class, StandardServletMultipartResolver.class, MultipartConfigElement.class})
//@ConditionalOnProperty(value = "spring.http.multipart", name = "enable", matchIfMissing = true)
//@EnableConfigurationProperties(MultipartProperties.class)
public class FileUploadConfig {


	//华南机房, 详见https://developer.qiniu.com/kodo/sdk/1239/java
	@Bean
	public com.qiniu.storage.Configuration qiniuHuaNanConfig(){
		return new com.qiniu.storage.Configuration(Region.region2());
//		return new com.qiniu.storage.Configuration(Region.huanan());
//		return new com.qiniu.storage.Configuration(Zone.zone2());
//		return new com.qiniu.storage.Configuration(Zone.huanan());
	}

	//构建七牛云上传工具实例
	@Bean
	public UploadManager uploadManager(){
		return new UploadManager(qiniuHuaNanConfig());
	}

	@Autowired
	private QiNiuProperties qiNiuProperties;

	//认证信息
	@Bean
	public Auth auth(){
		return Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey());
	}

	//构建七牛云空间管理实例
	@Bean
	public BucketManager bucketManager(){
		return new BucketManager(auth(), qiniuHuaNanConfig());
	}
}
