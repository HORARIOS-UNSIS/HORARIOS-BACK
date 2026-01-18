package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.MateriaExternaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de materias
 */
@Service
public class SubjectConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(SubjectConsumeClient.class);
    private static final String API_URL = "http://serv-horarios.unsis.lan/api/materias";
    private static final int TIMEOUT = 5000;

    private final RestTemplate restTemplate;

    public SubjectConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene la lista de materias desde API externa
     */
    public List<MateriaExternaDTO> obtenerMateriasDelAPI() {
        logger.info("Iniciando consumo de API: {}", API_URL);
        
        try {
            MateriaExternaDTO[] response = restTemplate.getForObject(
                API_URL,
                MateriaExternaDTO[].class
            );
            
            List<MateriaExternaDTO> result = Arrays.asList(
                response != null ? response : new MateriaExternaDTO[0]
            );
            
            logger.info("Se obtuvieron {} materias de la API", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo API de materias: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de materias: " + e.getMessage(), e);
        }
    }
}
