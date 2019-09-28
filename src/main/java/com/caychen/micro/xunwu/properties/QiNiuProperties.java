package com.caychen.micro.xunwu.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Author:       Caychen
 * Date:         2019/9/28
 * Desc:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class QiNiuProperties {

	private String accessKey;

	private String secretKey;

	private String bucket;

	private String cdnPrefix;
}
