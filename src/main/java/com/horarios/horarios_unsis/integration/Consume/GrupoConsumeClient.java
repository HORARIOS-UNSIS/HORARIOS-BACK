package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.GrupoExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de grupos
 * Endpoint: GET /api/grupos/
 */
@Service
public class GrupoConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(GrupoConsumeClient.class);
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;

    private final RestTemplate restTemplate;

    public GrupoConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene todos los grupos desde API externa
     * GET /api/grupos/
     * 
     * @return Lista de grupos
     */
    public List<GrupoExternoDTO> obtenerGrupos() {
        String url = baseUrl + "/api/grupos/";
        logger.info("Obteniendo grupos desde: {}", url);
        
        try {
            GrupoExternoDTO[] response = restTemplate.getForObject(url, GrupoExternoDTO[].class);
            List<GrupoExternoDTO> result = Arrays.asList(response != null ? response : new GrupoExternoDTO[0]);
            logger.info("Se obtuvieron {} grupos de la API", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error consumiendo API de grupos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de grupos: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene grupos filtrados por periodo directamente desde la API externa
     * GET /api/grupos/periodo={clavePeriodo}
     * 
     * @param periodo Clave del periodo (ej: "2526A")
     * @return Lista de grupos del periodo
     */
    public List<GrupoExternoDTO> obtenerGruposPorPeriodo(String periodo) {
        String url = baseUrl + "/api/grupos/periodo=" + periodo;
        logger.info("Obteniendo grupos del período {} desde: {}", periodo, url);
        
        try {
            GrupoExternoDTO[] response = restTemplate.getForObject(url, GrupoExternoDTO[].class);
            List<GrupoExternoDTO> result = Arrays.asList(response != null ? response : new GrupoExternoDTO[0]);
            logger.info("Se obtuvieron {} grupos del período {} de la API", result.size(), periodo);
            return result;
        } catch (Exception e) {
            logger.error("Error consumiendo API de grupos por período {}: {}", periodo, e.getMessage(), e);
            // Fallback: filtrar localmente si el endpoint específico falla
            logger.warn("Intentando fallback: obteniendo todos los grupos y filtrando...");
            List<GrupoExternoDTO> todos = obtenerGrupos();
            return todos.stream()
                    .filter(g -> periodo.equals(g.getPeriodo()))
                    .toList();
        }
    }

    /**
     * Obtiene grupos filtrados por carrera
     * 
     * @param claveCarrera Clave de la carrera (ej: "07")
     * @return Lista de grupos de la carrera
     */
    public List<GrupoExternoDTO> obtenerGruposPorCarrera(String claveCarrera) {
        List<GrupoExternoDTO> todos = obtenerGrupos();
        return todos.stream()
                .filter(g -> claveCarrera.equals(g.getCarrera()))
                .toList();
    }
}
