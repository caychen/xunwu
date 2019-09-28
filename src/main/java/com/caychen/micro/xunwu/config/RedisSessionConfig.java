package com.caychen.micro.xunwu.config;

import org.springframework.context.annotation.Configuration;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Configuration
//加入spring-session-data-redis依赖
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)//配置session失效时间为一天
public class RedisSessionConfig {

}
