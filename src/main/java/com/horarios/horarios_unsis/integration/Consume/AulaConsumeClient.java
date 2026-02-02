package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.AulaExternaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de aulas
 * Endpoint: GET /api/aulas/
 * 
 * Estructura JSON:
 * {
 *   "clave": "1",
 *   "nombre": "A1",
 *   "capacidad": 18,
 *   "tipo": "AULA",
 *   "statusProyector": "NO_FUNCIONA"
 * }
 */
@Service
public class AulaConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(AulaConsumeClient.class);
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;

    private final RestTemplate restTemplate;

    public AulaConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene la lista de aulas desde API externa
     * GET /api/aulas/
     * 
     * @return Lista de aulas disponibles
     */
    public List<AulaExternaDTO> obtenerAulasDelAPI() {
        String url = baseUrl + "/api/aulas/";
        logger.info("Obteniendo aulas desde: {}", url);
        
        try {
            AulaExternaDTO[] response = restTemplate.getForObject(url, AulaExternaDTO[].class);
            List<AulaExternaDTO> result = Arrays.asList(response != null ? response : new AulaExternaDTO[0]);
            logger.info("Se obtuvieron {} aulas de la API", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error consumiendo API de aulas: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de aulas: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene un aula específica por clave
     * 
     * @param clave Clave del aula (ej: "1", "95", "155")
     * @return DTO del aula solicitada o null
     */
    public AulaExternaDTO obtenerAulaPorClave(String clave) {
        List<AulaExternaDTO> aulas = obtenerAulasDelAPI();
        return aulas.stream()
                .filter(a -> clave.equals(a.getClave()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtiene aulas filtradas por tipo
     * 
     * @param tipo Tipo de aula (AULA, LABORATORIO, AUDITORIO)
     * @return Lista de aulas del tipo especificado
     */
    public List<AulaExternaDTO> obtenerAulasPorTipo(String tipo) {
        List<AulaExternaDTO> aulas = obtenerAulasDelAPI();
        return aulas.stream()
                .filter(a -> tipo.equals(a.getTipo()))
                .toList();
    }
}
