package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.SchoolHoursExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de horarios escolares
 */
@Service
public class SchoolHoursConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(SchoolHoursConsumeClient.class);
    private static final String API_URL = "http://serv-horarios.unsis.lan/api/horarios";
    private static final int TIMEOUT = 5000; // 5 segundos

    private final RestTemplate restTemplate;

    public SchoolHoursConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Consume la API externa y obtiene la lista de horarios escolares
     * @return Lista de horarios desde la API externa
     */
    public List<SchoolHoursExternoDTO> obtenerHorariosDelAPI() {
        logger.info("Iniciando consumo de API: {}", API_URL);
        
        try {
            SchoolHoursExternoDTO[] response = restTemplate.getForObject(
                API_URL,
                SchoolHoursExternoDTO[].class
            );
            
            List<SchoolHoursExternoDTO> result = Arrays.asList(
                response != null ? response : new SchoolHoursExternoDTO[0]
            );
            
            logger.info("Se obtuvieron {} horarios de la API", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo API de horarios: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de horarios: " + e.getMessage(), e);
        }
    }
}
