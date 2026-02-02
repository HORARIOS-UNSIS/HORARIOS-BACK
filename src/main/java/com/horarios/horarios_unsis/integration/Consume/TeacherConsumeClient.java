package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.ProfesorExternoDTO;
import com.horarios.horarios_unsis.integration.Consume.DTO.HorarioExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Cliente para obtener profesores desde la API externa.
 * 
 * NOTA: No existe un endpoint directo para profesores.
 * Los profesores se extraen del endpoint de horarios: /api/horarios/{periodo}/aula/{idAula}
 */
@Component
public class TeacherConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(TeacherConsumeClient.class);
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    private final RestTemplate restTemplate;
    
    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;
    
    // Cache de profesores para evitar múltiples llamadas
    private Map<Integer, ProfesorExternoDTO> profesoresCache = new HashMap<>();
    
    public TeacherConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Obtiene la lista de todos los profesores extrayéndolos de los horarios.
     * 
     * Estrategia:
     * 1. Obtener lista de períodos
     * 2. Obtener lista de aulas
     * 3. Para cada aula, obtener horarios y extraer profesores únicos
     * 
     * @return Array de profesores únicos encontrados
     */
    public ProfesorExternoDTO[] obtenerProfesores() {
        logger.info("Extrayendo profesores desde horarios...");
        profesoresCache.clear();
        
        try {
            // 1. Obtener período actual (el más reciente)
            String urlPeriodos = baseUrl + "/api/periodo/lista";
            logger.debug("Obteniendo períodos desde: {}", urlPeriodos);
            
            // Usar un DTO simple o Map para períodos
            Object[] periodos = restTemplate.getForObject(urlPeriodos, Object[].class);
            if (periodos == null || periodos.length == 0) {
                logger.warn("No se encontraron períodos");
                return new ProfesorExternoDTO[0];
            }
            
            // Tomar el último período (más reciente)
            @SuppressWarnings("unchecked")
            Map<String, Object> ultimoPeriodo = (Map<String, Object>) periodos[periodos.length - 1];
            String clavePeriodo = (String) ultimoPeriodo.get("clave");
            logger.info("Usando período: {}", clavePeriodo);
            
            // 2. Obtener lista de aulas
            String urlAulas = baseUrl + "/api/aulas/";
            logger.debug("Obteniendo aulas desde: {}", urlAulas);
            
            Object[] aulas = restTemplate.getForObject(urlAulas, Object[].class);
            if (aulas == null || aulas.length == 0) {
                logger.warn("No se encontraron aulas");
                return new ProfesorExternoDTO[0];
            }
            
            // 3. Para cada aula, obtener horarios y extraer profesores
            int aulasConsultadas = 0;
            int maxAulas = Math.min(aulas.length, 10); // Limitar a 10 aulas para no sobrecargar
            
            for (int i = 0; i < maxAulas; i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> aula = (Map<String, Object>) aulas[i];
                String claveAula = String.valueOf(aula.get("clave"));
                
                try {
                    extraerProfesoresDeAula(clavePeriodo, claveAula);
                    aulasConsultadas++;
                } catch (Exception e) {
                    logger.debug("Error obteniendo horarios de aula {}: {}", claveAula, e.getMessage());
                }
            }
            
            logger.info("Se extrajeron {} profesores únicos de {} aulas", 
                       profesoresCache.size(), aulasConsultadas);
            
            return profesoresCache.values().toArray(new ProfesorExternoDTO[0]);
            
        } catch (Exception e) {
            logger.error("Error extrayendo profesores: {}", e.getMessage(), e);
            throw new RuntimeException("Error al extraer profesores de horarios: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extrae profesores de los horarios de un aula específica
     */
    private void extraerProfesoresDeAula(String clavePeriodo, String claveAula) {
        String url = baseUrl + "/api/horarios/" + clavePeriodo + "/aula/" + claveAula;
        logger.debug("Consultando horarios: {}", url);
        
        try {
            HorarioExternoDTO[] horarios = restTemplate.getForObject(url, HorarioExternoDTO[].class);
            
            if (horarios != null) {
                for (HorarioExternoDTO horario : horarios) {
                    // Los datos de profesor vienen directamente en el DTO (estructura plana)
                    // idprofesor: "1134", nombreCompleto: "DR. MAURICIO SOSA MONTES"
                    Integer idProfesor = horario.getIdProfesor();
                    String nombreProfesor = horario.getNombreProfesor();
                    
                    if (idProfesor != null && !profesoresCache.containsKey(idProfesor)) {
                        ProfesorExternoDTO prof = new ProfesorExternoDTO(
                            idProfesor, 
                            nombreProfesor != null ? nombreProfesor : "Profesor #" + idProfesor, 
                            false
                        );
                        profesoresCache.put(idProfesor, prof);
                        logger.debug("Profesor encontrado: {} - {}", idProfesor, nombreProfesor);
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("No se pudieron obtener horarios de aula {}: {}", claveAula, e.getMessage());
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
     * Obtiene un profesor específico por su ID (desde cache)
     * @param idProfesor ID del profesor
     * @return Profesor externo o null
     */
    public ProfesorExternoDTO obtenerProfesorPorId(Integer idProfesor) {
        // Si no hay cache, cargar profesores
        if (profesoresCache.isEmpty()) {
            obtenerProfesores();
        }
        
        return profesoresCache.get(idProfesor);
    }
    
    /**
     * Limpia el cache de profesores
     */
    public void limpiarCache() {
        profesoresCache.clear();
    }
}

