package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.MateriaExternaDTO;
import com.horarios.horarios_unsis.integration.Consume.DTO.HorarioExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Cliente para obtener materias desde la API externa.
 * 
 * NOTA: No existe un endpoint directo para materias.
 * Las materias se extraen del endpoint de horarios: /api/horarios/{periodo}/aula/{idAula}
 */
@Component
public class SubjectConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(SubjectConsumeClient.class);
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    private final RestTemplate restTemplate;
    
    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;
    
    // Cache de materias para evitar múltiples llamadas
    private Map<Integer, MateriaExternaDTO> materiasCache = new HashMap<>();
    
    public SubjectConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * Obtiene la lista de todas las materias extrayéndolas de los horarios.
     * 
     * Estrategia:
     * 1. Obtener lista de períodos
     * 2. Obtener lista de aulas
     * 3. Para cada aula, obtener horarios y extraer materias únicas
     * 
     * @return Array de materias únicas encontradas
     */
    public MateriaExternaDTO[] obtenerMaterias() {
        logger.info("Extrayendo materias desde horarios...");
        materiasCache.clear();
        
        try {
            // 1. Obtener período actual (el más reciente)
            String urlPeriodos = baseUrl + "/api/periodo/lista";
            logger.debug("Obteniendo períodos desde: {}", urlPeriodos);
            
            Object[] periodos = restTemplate.getForObject(urlPeriodos, Object[].class);
            if (periodos == null || periodos.length == 0) {
                logger.warn("No se encontraron períodos");
                return new MateriaExternaDTO[0];
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
                return new MateriaExternaDTO[0];
            }
            
            // 3. Para cada aula, obtener horarios y extraer materias
            int aulasConsultadas = 0;
            int maxAulas = Math.min(aulas.length, 10); // Limitar para no sobrecargar
            
            for (int i = 0; i < maxAulas; i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> aula = (Map<String, Object>) aulas[i];
                String claveAula = String.valueOf(aula.get("clave"));
                
                try {
                    extraerMateriasDeAula(clavePeriodo, claveAula);
                    aulasConsultadas++;
                } catch (Exception e) {
                    logger.debug("Error obteniendo horarios de aula {}: {}", claveAula, e.getMessage());
                }
            }
            
            logger.info("Se extrajeron {} materias únicas de {} aulas", 
                       materiasCache.size(), aulasConsultadas);
            
            return materiasCache.values().toArray(new MateriaExternaDTO[0]);
            
        } catch (Exception e) {
            logger.error("Error extrayendo materias: {}", e.getMessage(), e);
            throw new RuntimeException("Error al extraer materias de horarios: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extrae materias de los horarios de un aula específica
     */
    private void extraerMateriasDeAula(String clavePeriodo, String claveAula) {
        String url = baseUrl + "/api/horarios/" + clavePeriodo + "/aula/" + claveAula;
        logger.debug("Consultando horarios: {}", url);
        
        try {
            HorarioExternoDTO[] horarios = restTemplate.getForObject(url, HorarioExternoDTO[].class);
            
            if (horarios != null) {
                for (HorarioExternoDTO horario : horarios) {
                    // Los datos de materia vienen directamente en el DTO (estructura plana)
                    // asignatura: "5032_2024", materia: "MICROECONOMÍA"
                    Integer idMateria = horario.getIdMateria(); // extrae de asignatura "5032_2024" -> 5032
                    String nombreMateria = horario.getNombreMateria();
                    
                    if (idMateria != null && !materiasCache.containsKey(idMateria)) {
                        MateriaExternaDTO mat = new MateriaExternaDTO(
                            idMateria, 
                            nombreMateria != null ? nombreMateria : "Materia #" + idMateria
                        );
                        materiasCache.put(idMateria, mat);
                        logger.debug("Materia encontrada: {} - {}", idMateria, nombreMateria);
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("No se pudieron obtener horarios de aula {}: {}", claveAula, e.getMessage());
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
     * Obtiene una materia específica por su ID (desde cache)
     * @param idMateria ID de la materia
     * @return Materia externa o null
     */
    public MateriaExternaDTO obtenerMateriaPorId(Integer idMateria) {
        // Si no hay cache, cargar materias
        if (materiasCache.isEmpty()) {
            obtenerMaterias();
        }
        
        return materiasCache.get(idMateria);
    }
    
    /**
     * Limpia el cache de materias
     */
    public void limpiarCache() {
        materiasCache.clear();
    }
}

