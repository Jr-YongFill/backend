package com.yongfill.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import jakarta.annotation.PostConstruct;

import java.util.TimeZone;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableScheduling
public class ServerApplication {

	@PostConstruct
	public void started() {
        	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
