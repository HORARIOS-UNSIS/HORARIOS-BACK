package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.ProfesorExternoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TeacherConsumeClient {
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    private final RestTemplate restTemplate;
    
    public TeacherConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Obtiene la lista de todos los profesores del API externo
     * @return Array de profesores externos
     */
    public ProfesorExternoDTO[] obtenerProfesores() {
        String url = baseUrl + "/api/profesores";
        try {
            return restTemplate.getForObject(url, ProfesorExternoDTO[].class);
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir API de profesores: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene la lista de todos los profesores del API externo (alias)
     * @return Array de profesores externos
     */
    public ProfesorExternoDTO[] obtenerProfesoresDelAPI() {
        return obtenerProfesores();
    }
    
    /**
     * Obtiene un profesor específico del API externo por su ID
     * @param idProfesor ID del profesor
     * @return Profesor externo
     */
    public ProfesorExternoDTO obtenerProfesorPorId(Integer idProfesor) {
        String url = baseUrl + "/api/profesores/" + idProfesor;
        try {
            return restTemplate.getForObject(url, ProfesorExternoDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir API de profesores por ID: " + e.getMessage(), e);
        }
    }
}

