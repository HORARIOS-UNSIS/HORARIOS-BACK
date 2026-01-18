package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.ExamenExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de exámenes
 */
@Service
public class ScheduleConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(ScheduleConsumeClient.class);
    private static final String API_URL = "http://serv-horarios.unsis.lan/api/examenes";
    private static final int TIMEOUT = 5000;

    private final RestTemplate restTemplate;

    public ScheduleConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene la lista de exámenes desde API externa
     */
    public List<ExamenExternoDTO> obtenerExamenesDelAPI() {
        logger.info("Iniciando consumo de API: {}", API_URL);
        
        try {
            ExamenExternoDTO[] response = restTemplate.getForObject(
                API_URL,
                ExamenExternoDTO[].class
            );
            
            List<ExamenExternoDTO> result = Arrays.asList(
                response != null ? response : new ExamenExternoDTO[0]
            );
            
            logger.info("Se obtuvieron {} exámenes de la API", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo API de exámenes: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de exámenes: " + e.getMessage(), e);
        }
    }
}
