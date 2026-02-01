package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.MateriaExternaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

@Component
public class SubjectConsumeClient {
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    private final RestTemplate restTemplate;
    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;
    
    public SubjectConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Obtiene la lista de todas las materias del API externo
     * @return Array de materias externas
     */
    public MateriaExternaDTO[] obtenerMaterias() {
        if (!integrationEnabled) {
            // Datos ficticios para pruebas
            return new MateriaExternaDTO[]{
                    new MateriaExternaDTO(1, "Matemáticas (fake)"),
                    new MateriaExternaDTO(2, "Física (fake)"),
                    new MateriaExternaDTO(3, "Programación (fake)")
            };
        }

        String url = baseUrl + "/api/materias";
        try {
            return restTemplate.getForObject(url, MateriaExternaDTO[].class);
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir API de materias: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene la lista de todas las materias del API externo (alias)
     * @return Array de materias externas
     */
    public MateriaExternaDTO[] obtenerMateriasDelAPI() {
        return obtenerMaterias();
    }
    
    /**
     * Obtiene una materia específica del API externo por su ID
     * @param idMateria ID de la materia
     * @return Materia externa
     */
    public MateriaExternaDTO obtenerMateriaPorId(Integer idMateria) {
        if (!integrationEnabled) {
            return new MateriaExternaDTO(idMateria, "Materia (fake) #" + idMateria);
        }

        String url = baseUrl + "/api/materias/" + idMateria;
        try {
            return restTemplate.getForObject(url, MateriaExternaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir API de materias por ID: " + e.getMessage(), e);
        }
    }
}

