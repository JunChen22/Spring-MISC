package io.github.jun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringS3DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringS3DemoApplication.class, args);
	}
}
