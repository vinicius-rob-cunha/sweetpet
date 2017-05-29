package br.com.sweetpet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SweetpetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SweetpetApplication.class, args);
	}
}
