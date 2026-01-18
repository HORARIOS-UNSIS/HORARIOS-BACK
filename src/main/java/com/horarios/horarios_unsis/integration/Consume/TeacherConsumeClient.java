package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.ProfesorExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de profesores
 */
@Service
public class TeacherConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(TeacherConsumeClient.class);
    private static final String API_URL = "http://serv-horarios.unsis.lan/api/profesores";
    private static final int TIMEOUT = 5000;

    private final RestTemplate restTemplate;

    public TeacherConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene la lista de profesores desde API externa
     */
    public List<ProfesorExternoDTO> obtenerProfesoresDelAPI() {
        logger.info("Iniciando consumo de API: {}", API_URL);
        
        try {
            ProfesorExternoDTO[] response = restTemplate.getForObject(
                API_URL,
                ProfesorExternoDTO[].class
            );
            
            List<ProfesorExternoDTO> result = Arrays.asList(
                response != null ? response : new ProfesorExternoDTO[0]
            );
            
            logger.info("Se obtuvieron {} profesores de la API", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo API de profesores: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de profesores: " + e.getMessage(), e);
        }
    }
}
