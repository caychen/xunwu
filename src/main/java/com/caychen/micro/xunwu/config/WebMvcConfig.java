package com.caychen.micro.xunwu.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author:       Caychen
 * Date:         2019/9/7
 * Desc:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//资源映射，将/static/**的文件映射到classpath下多个文件夹下的文件，这样就可以直接返回
		//如可以直接访问http://localhost:8080/static/xxx/yyy/zz.js等
		registry.addResourceHandler("/static/**")
				.addResourceLocations(
						"classpath:/META-INF/resources/",
						"classpath:/resources/",
						"classpath:/static/",
						"classpath:/public/",
						"/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/500").setViewName("/500");
		registry.addViewController("/404").setViewName("/404");
		registry.addViewController("/403").setViewName("/403");
		registry.addViewController("/logout").setViewName("/logout");
	}


	/**
	 * Bean Util
	 * @return
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
