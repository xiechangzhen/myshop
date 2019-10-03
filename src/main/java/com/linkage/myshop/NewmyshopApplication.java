package com.linkage.myshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.linkage.myshop.mapper")
public class NewmyshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewmyshopApplication.class, args);
	}

}
