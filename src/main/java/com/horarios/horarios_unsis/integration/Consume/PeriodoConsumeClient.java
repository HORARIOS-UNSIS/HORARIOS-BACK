package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.PeriodoExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de periodos académicos
 * Endpoints:
 * - GET /api/periodos (todos los periodos)
 * - GET /api/periodo/actual (periodo activo)
 */
@Service
public class PeriodoConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(PeriodoConsumeClient.class);
    private static final String BASE_API_URL = "http://serv-horarios.unsis.lan";
    private static final String ENDPOINT_TODOS = "/api/periodos";
    private static final String ENDPOINT_ACTUAL = "/api/periodo/actual";

    private final RestTemplate restTemplate;

    public PeriodoConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene todos los periodos disponibles
     * GET /api/periodos
     * 
     * @return Lista de todos los periodos
     */
    public List<PeriodoExternoDTO> obtenerTodosPeriodos() {
        String url = BASE_API_URL + ENDPOINT_TODOS;
        logger.info("Iniciando consumo de API: {}", url);
        
        try {
            PeriodoExternoDTO[] response = restTemplate.getForObject(url, PeriodoExternoDTO[].class);
            
            List<PeriodoExternoDTO> result = Arrays.asList(
                response != null ? response : new PeriodoExternoDTO[0]
            );
            
            logger.info("Se obtuvieron {} periodos de la API", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo API de periodos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de periodos: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el periodo académico actual
     * GET /api/periodo/actual
     * 
     * @return DTO del periodo activo
     */
    public PeriodoExternoDTO obtenerPeriodoActual() {
        String url = BASE_API_URL + ENDPOINT_ACTUAL;
        logger.info("Obteniendo periodo actual desde: {}", url);
        
        try {
            PeriodoExternoDTO periodo = restTemplate.getForObject(url, PeriodoExternoDTO.class);
            logger.info("Periodo actual obtenido: {}", periodo);
            return periodo;
            
        } catch (Exception e) {
            logger.error("Error obteniendo periodo actual: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener periodo actual: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene un periodo específico por ID
     * GET /api/periodos/{id}
     * 
     * @param idPeriodo ID del periodo
     * @return DTO del periodo solicitado
     */
    public PeriodoExternoDTO obtenerPeriodoPorId(Integer idPeriodo) {
        String url = BASE_API_URL + ENDPOINT_TODOS + "/" + idPeriodo;
        logger.info("Obteniendo periodo con ID: {} desde {}", idPeriodo, url);
        
        try {
            PeriodoExternoDTO periodo = restTemplate.getForObject(url, PeriodoExternoDTO.class);
            logger.info("Periodo obtenido: {}", periodo);
            return periodo;
            
        } catch (Exception e) {
            logger.error("Error obteniendo periodo con ID {}: {}", idPeriodo, e.getMessage(), e);
            throw new RuntimeException("Error al obtener periodo: " + e.getMessage(), e);
        }
    }
}
