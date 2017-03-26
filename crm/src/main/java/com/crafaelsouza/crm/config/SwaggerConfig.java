package com.crafaelsouza.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class SwaggerConfig, responsible to create a documentation of the rest services.
 * 
 * @author Carlos Souza
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.crafaelsouza.crm.controller"))
				.paths(PathSelectors.any())
				.build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		final ApiInfo apiInfo = new ApiInfo("Basic - CRM",
				"Basic CRM API", "v1", "Terms of service", "carlos.rafael31@hotmail.com", "License of API", "URL");
		return apiInfo;
	}
}