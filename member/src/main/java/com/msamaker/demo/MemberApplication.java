package com.msamaker.demo;

import com.msamaker.demo.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
public class MemberApplication {

	private static final Logger log = LoggerFactory.getLogger(MemberApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			log.info("--------------------------------------------------------------------------------");
			log.info("--------------------------------------------------------------------------------");
			log.info("--------------------------------------------------------------------------------");
			log.info("Start ExampleApplication ENV Setting Display...");
			log.info("spring.profiles.active : {}" , ctx.getEnvironment().getProperty("spring.profiles.active"));
			log.info("application.service.name : {}" , ctx.getEnvironment().getProperty("application.service.name"));
			log.info("application.routes[0].id : {}" , ctx.getEnvironment().getProperty("application.routes[0].id"));
			log.info("application.routes[0].index : {}" , ctx.getEnvironment().getProperty("application.routes[0].index"));
			log.info("spring.kafka.bootstrap-servers : {}" , ctx.getEnvironment().getProperty("spring.kafka.bootstrap-servers"));
			log.info("--------------------------------------------------------------------------------");
			log.info("--------------------------------------------------------------------------------");
			log.info("--------------------------------------------------------------------------------");

		};
	}


}
