package com.horarios.horarios_unsis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.horarios.horarios_unsis.integration.external.ConsumeAPI;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class HorariosUnsisApplication implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(HorariosUnsisApplication.class);
	
	@Autowired
	private ConsumeAPI consumeAPI;

	public static void main(String[] args) {
		SpringApplication.run(HorariosUnsisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("========================================");
		logger.info("Iniciando consumo de API de aulas...");
		logger.info("========================================");
		
		try {
			var aulas = consumeAPI.consumirApiAulas();
			
			logger.info("========================================");
			logger.info("DATOS OBTENIDOS DE LA API:");
			logger.info("========================================");
			
			if (aulas.isEmpty()) {
				logger.warn("No se obtuvieron datos de la API");
			} else {
				ObjectMapper mapper = new ObjectMapper();
				for (int i = 0; i < aulas.size(); i++) {
					String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(aulas.get(i));
					logger.info("Aula {}: {}", (i + 1), json);
				}
			}
			
			logger.info("========================================");
			logger.info("Total de aulas: {}", aulas.size());
			logger.info("========================================");
		} catch (Exception e) {
			logger.error("Error al ejecutar el consumo de API", e);
		}
	}

}
