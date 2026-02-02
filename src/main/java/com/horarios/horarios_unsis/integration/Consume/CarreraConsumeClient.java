package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.CarreraExternaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de carreras
 * Endpoint: GET /api/carreras/vigentes
 */
@Service
public class CarreraConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(CarreraConsumeClient.class);
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;

    private final RestTemplate restTemplate;

    public CarreraConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene las carreras vigentes desde API externa
     * GET /api/carreras/vigentes
     * 
     * @return Lista de carreras vigentes
     */
    public List<CarreraExternaDTO> obtenerCarreras() {
        String url = baseUrl + "/api/carreras/vigentes";
        logger.info("Obteniendo carreras vigentes desde: {}", url);
        
        try {
            CarreraExternaDTO[] response = restTemplate.getForObject(url, CarreraExternaDTO[].class);
            List<CarreraExternaDTO> result = Arrays.asList(response != null ? response : new CarreraExternaDTO[0]);
            logger.info("Se obtuvieron {} carreras vigentes de la API", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error consumiendo API de carreras: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consumir API de carreras: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene solo las carreras vigentes (ahora es igual a obtenerCarreras)
     * @deprecated Usar obtenerCarreras() directamente
     * @return Lista de carreras vigentes
     */
    @Deprecated
    public List<CarreraExternaDTO> obtenerCarrerasVigentes() {
        return obtenerCarreras();
    }

    /**
     * Busca una carrera por clave
     * 
     * @param clave Clave de la carrera (ej: "07")
     * @return Carrera encontrada o null
     */
    public CarreraExternaDTO obtenerCarreraPorClave(String clave) {
        List<CarreraExternaDTO> todas = obtenerCarreras();
        return todas.stream()
                .filter(c -> clave.equals(c.getClave()))
                .findFirst()
                .orElse(null);
    }
}
