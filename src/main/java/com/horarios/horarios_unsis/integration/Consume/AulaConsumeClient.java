package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.AulaExternaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de aulas
 * Endpoint: GET /api/aulas
 * 
 * Mapeo:
 * - id (API) → idAula (BD Local)
 * - nombre → nombre
 * - capacidad → capacidad
 */
@Service
public class AulaConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(AulaConsumeClient.class);
    private static final String BASE_API_URL = "http://serv-horarios.unsis.lan";
    private static final String ENDPOINT = "/api/aulas";

    private final RestTemplate restTemplate;

    public AulaConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene la lista de aulas desde API externa
     * GET /api/aulas
     * 
     * @return Lista de aulas disponibles
     */
    public List<AulaExternaDTO> obtenerAulasDelAPI() {
        String url = BASE_API_URL + ENDPOINT;
        logger.info("Iniciando consumo de API: {}", url);
        
        try {
            AulaExternaDTO[] response = restTemplate.getForObject(url, AulaExternaDTO[].class);
            
            List<AulaExternaDTO> result = Arrays.asList(
                response != null ? response : new AulaExternaDTO[0]
            );
            
            logger.info("Se obtuvieron {} aulas de la API", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo API de aulas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de aulas: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene un aula específica por ID
     * GET /api/aulas/{id}
     * 
     * @param idAula ID del aula a obtener
     * @return DTO del aula solicitada
     */
    public AulaExternaDTO obtenerAulaPorId(Integer idAula) {
        String url = BASE_API_URL + ENDPOINT + "/" + idAula;
        logger.info("Obteniendo aula con ID: {} desde {}", idAula, url);
        
        try {
            AulaExternaDTO aula = restTemplate.getForObject(url, AulaExternaDTO.class);
            logger.info("Aula obtenida: {}", aula);
            return aula;
            
        } catch (Exception e) {
            logger.error("Error obteniendo aula con ID {}: {}", idAula, e.getMessage(), e);
            throw new RuntimeException("Error al obtener aula: " + e.getMessage(), e);
        }
    }
}
