package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.PeriodoExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de periodos académicos
 * Endpoint: GET /api/periodo/lista
 */
@Service
public class PeriodoConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(PeriodoConsumeClient.class);
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;

    private final RestTemplate restTemplate;

    public PeriodoConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene todos los periodos disponibles
     * GET /api/periodo/lista
     * 
     * @return Lista de todos los periodos
     */
    public List<PeriodoExternoDTO> obtenerTodosPeriodos() {
        String url = baseUrl + "/api/periodo/lista";
        logger.info("Obteniendo periodos desde: {}", url);
        
        try {
            PeriodoExternoDTO[] response = restTemplate.getForObject(url, PeriodoExternoDTO[].class);
            List<PeriodoExternoDTO> result = Arrays.asList(response != null ? response : new PeriodoExternoDTO[0]);
            logger.info("Se obtuvieron {} periodos de la API", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error consumiendo API de periodos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de periodos: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el periodo más reciente (último en la lista)
     * 
     * @return DTO del periodo más reciente
     */
    public PeriodoExternoDTO obtenerPeriodoActual() {
        List<PeriodoExternoDTO> periodos = obtenerTodosPeriodos();
        if (periodos.isEmpty()) {
            return null;
        }
        // Retorna el último periodo de la lista (más reciente)
        return periodos.get(periodos.size() - 1);
    }

    /**
     * Busca un periodo por clave
     * 
     * @param clave Clave del periodo (ej: "1516A")
     * @return DTO del periodo o null
     */
    public PeriodoExternoDTO obtenerPeriodoPorClave(String clave) {
        List<PeriodoExternoDTO> periodos = obtenerTodosPeriodos();
        return periodos.stream()
                .filter(p -> clave.equals(p.getClave()))
                .findFirst()
                .orElse(null);
    }
}
