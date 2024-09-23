package com.underthekey.sentenceapi.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rate-limiting")
public class RateLimitingProperties {
	public List<String> allowedIps;
}
