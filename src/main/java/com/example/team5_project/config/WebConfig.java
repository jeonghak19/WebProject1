package com.example.team5_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dir = System.getProperty("user.dir");
		registry.addResourceHandler("/files/**")  // view에서 접근할 경로
				.addResourceLocations("file:///" + dir + "/src/main/resources/static/files/"); // 실제 파일 저장 경로
	}
}
