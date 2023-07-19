package com.luxlog;

import com.luxlog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
//@Import(QueryDslConfig.class)
public class LuxlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuxlogApplication.class, args);
	}

}
