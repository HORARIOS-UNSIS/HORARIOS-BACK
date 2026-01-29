package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.ProfesorExternoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

@Component
public class TeacherConsumeClient {
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    private final RestTemplate restTemplate;
    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;
    
    public TeacherConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Obtiene la lista de todos los profesores del API externo
     * @return Array de profesores externos
     */
    public ProfesorExternoDTO[] obtenerProfesores() {
        if (!integrationEnabled) {
                return new ProfesorExternoDTO[]{
                    new ProfesorExternoDTO(1, "Juan Perez", false),
                    new ProfesorExternoDTO(2, "María López", false)
                };
        }

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
        if (!integrationEnabled) {
            return new ProfesorExternoDTO(idProfesor, "Profesor (fake) #" + idProfesor, false);
        }

        String url = baseUrl + "/api/profesores/" + idProfesor;
        try {
            return restTemplate.getForObject(url, ProfesorExternoDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir API de profesores por ID: " + e.getMessage(), e);
        }
    }
}

