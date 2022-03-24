package com.ggoreb.practice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ggoreb.practice.interceptor.SignInCheckInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	@Autowired
	private SignInCheckInterceptor signInCheckInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
		.addInterceptor(signInCheckInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/signin", "/signup", "/css/**", "/js/**", "/user/check");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
