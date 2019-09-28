package com.caychen.micro.xunwu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Author:       Caychen
 * Date:         2019/9/8
 * Desc:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private static String BASE_PACKAGE = "com.caychen.micro.xunwu.web.controller";

	@Bean
	public Docket buildDocket(){
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(buildApiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo buildApiInfo(){
		return new ApiInfoBuilder()
				.title("使用Swagger2构建RESTful API文档")
				.description("构建简单优雅的Restfun风格的api")
				.termsOfServiceUrl("https://github.com/caychen")
				.contact(new Contact("Caychen", "", "412425870@qq.com"))
				.version("1.0")
				.build();
	}

}
