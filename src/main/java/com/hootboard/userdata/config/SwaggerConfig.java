package com.hootboard.userdata.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket userApi() {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new ParameterBuilder().name("Authorization").modelRef(new ModelRef("header"))
				.parameterType("header").required(true).build());
		return new Docket(DocumentationType.SWAGGER_2).groupName("secured").globalOperationParameters(parameters)
				.select().apis(RequestHandlerSelectors.basePackage("com.hootboard.userdata.controller"))
				.paths(PathSelectors.ant("/secure/**")).build().apiInfo(apiInfo());
	}

	@Bean
	public Docket authApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.hootboard.userdata.controller"))
				.paths(PathSelectors.ant("/client/*")).build().apiInfo(apiInfo());
	}

	@Bean
	ApiInfo apiInfo() {
		final ApiInfoBuilder builder = new ApiInfoBuilder();
		builder.title("UserDataService API").version("1.0")
				.description("List of all endpoints used in UserDataService API");
		return builder.build();
	}
}
