package com.horarios.horarios_unsis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HorariosUnsisApplication {

	public static void main(String[] args) {
		SpringApplication.run(HorariosUnsisApplication.class, args);
	}

	/**
	 * Bean de RestTemplate para consumir APIs externas
	 * Se inyectará automáticamente en servicios que lo necesiten
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
