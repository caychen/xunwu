package com.caychen.micro.xunwu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Author:       Caychen
 * Date:         2019/9/19
 * Desc:
 */
@Component
public class GeneratorUtil {

	@Autowired
	private RedisTemplate redisTemplate;

	public String generatorOrderNo(){

		LocalDateTime now = LocalDateTime.now();

		String d = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		Long seq = redisTemplate.opsForValue().increment(today, 1L);
		if(seq.compareTo(1L) == 0){
			//第一次,设置key过期时间
			redisTemplate.expire(today, 24, TimeUnit.HOURS);
		}

		String s = d + String.format("%06d", seq);
		return s;
	}
}
