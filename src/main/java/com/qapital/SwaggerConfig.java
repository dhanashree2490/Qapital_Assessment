package com.qapital;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	
	@Value("${api.doc.title}")
	private String apiDocTitle;
	@Value("${api.doc.description}")
	private String apiDocDescription;
	@Value("${api.doc.version}")
	private String apiDocVersion;
	
	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI().info(new Info().title(apiDocTitle).description(apiDocDescription).version(apiDocVersion));
	}


}
