package com.underthekey.sentenceapi.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
	private final RateLimitFilter rateLimitFilter;
	private static final String[] INCLUDE_PATHS = {
		"/*"
	};

	@Bean
	public FilterRegistrationBean<RateLimitFilter> filterBean() {

		FilterRegistrationBean<RateLimitFilter> registrationBean
			= new FilterRegistrationBean<>();

		registrationBean.setFilter(rateLimitFilter);
		registrationBean.setOrder(1);
		registrationBean.setUrlPatterns(Arrays.asList(INCLUDE_PATHS));

		return registrationBean;
	}
}
