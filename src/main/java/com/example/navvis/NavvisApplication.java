package com.example.navvis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class NavvisApplication {

	public static void main(String[] args) {
		SpringApplication.run(NavvisApplication.class, args);
	}

}
