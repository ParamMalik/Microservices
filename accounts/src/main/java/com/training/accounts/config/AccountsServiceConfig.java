package com.training.accounts.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "accounts")
public class AccountsServiceConfig {
	private String msg;
	private String buildVersion;
	private Map<String, String> mailDetails;
	private List<String> activeBranches;

}
