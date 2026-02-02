package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.HorarioExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de horarios
 * 
 * Endpoints disponibles:
 * - GET /api/horarios/{periodo}/{idprofesor} (horarios de un profesor en periodo)
 * - GET /api/horarios/{periodo}/grupo/{idGrupo} (horarios de un grupo)
 * - GET /api/horarios/{periodo}/aula/{idaula} (horarios de un aula)
 */
@Service
public class HorarioConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(HorarioConsumeClient.class);
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;
    
    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;

    private final RestTemplate restTemplate;

    public HorarioConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene los horarios de un aula en un período específico
     * GET /api/horarios/{periodo}/aula/{idaula}
     * 
     * @param clavePeriodo Clave del período (ej: "1516A")
     * @param claveAula Clave del aula (ej: "1", "95")
     * @return Lista de horarios del aula
     */
    public List<HorarioExternoDTO> obtenerHorariosPorAula(String clavePeriodo, String claveAula) {
        String url = baseUrl + "/api/horarios/" + clavePeriodo + "/aula/" + claveAula;
        logger.info("Obteniendo horarios para aula {} en periodo {}", claveAula, clavePeriodo);
        
        try {
            HorarioExternoDTO[] response = restTemplate.getForObject(url, HorarioExternoDTO[].class);
            List<HorarioExternoDTO> result = Arrays.asList(response != null ? response : new HorarioExternoDTO[0]);
            logger.info("Se obtuvieron {} horarios del aula", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error consumiendo horarios del aula: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener horarios del aula: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los horarios de un profesor en un período específico
     * GET /api/horarios/{periodo}/{idprofesor}
     * 
     * @param clavePeriodo Clave del período (ej: "1516A")
     * @param idProfesor ID del profesor
     * @return Lista de horarios del profesor
     */
    public List<HorarioExternoDTO> obtenerHorariosPorProfesor(String clavePeriodo, Integer idProfesor) {
        String url = baseUrl + "/api/horarios/" + clavePeriodo + "/" + idProfesor;
        logger.info("Obteniendo horarios para profesor {} en periodo {}", idProfesor, clavePeriodo);
        
        try {
            HorarioExternoDTO[] response = restTemplate.getForObject(url, HorarioExternoDTO[].class);
            List<HorarioExternoDTO> result = Arrays.asList(response != null ? response : new HorarioExternoDTO[0]);
            logger.info("Se obtuvieron {} horarios del profesor", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error consumiendo horarios del profesor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener horarios del profesor: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene TODOS los horarios de un período (iterando por aulas o método masivo si existe)
     * Por ahora simula iterando aulas conocidas
     * 
     * @param clavePeriodo Clave del período
     * @return Lista de todos los horarios
     */
    public List<HorarioExternoDTO> obtenerTodosHorariosPorPeriodo(String clavePeriodo) {
        // NOTA: Si no existe un endpoint "dame todo", necesitamos iterar aulas.
        // Asumiendo que existe un endpoint similar o iterando aulas 1..20
        // Para simplificar, asumimos que iteramos aulas claves 1 a 20
        java.util.ArrayList<HorarioExternoDTO> todos = new java.util.ArrayList<>();
        
        // Lista temporal de aulas comunes para probar integración
        String[] aulasComunes = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "95", "96"};
        
        for (String aula : aulasComunes) {
            try {
                todos.addAll(obtenerHorariosPorAula(clavePeriodo, aula));
            } catch (Exception e) {
                // Ignorar error de aulas específicas
                logger.warn("No se pudo obtener horario para aula {}: {}", aula, e.getMessage());
            }
        }
        
        return todos;
    }

    /**
     * Obtiene los horarios de un grupo en un período específico
     * GET /api/horarios/{periodo}/grupo/{claveGrupo}
     * 
     * @param clavePeriodo Clave del período (ej: "1516A")
     * @param claveGrupo Clave del grupo (ej: "107A")
     * @return Lista de horarios del grupo
     */
    public List<HorarioExternoDTO> obtenerHorariosPorGrupo(String clavePeriodo, String claveGrupo) {
        String url = baseUrl + "/api/horarios/" + clavePeriodo + "/grupo/" + claveGrupo;
        logger.info("Obteniendo horarios para grupo {} en periodo {}", claveGrupo, clavePeriodo);
        
        try {
            HorarioExternoDTO[] response = restTemplate.getForObject(url, HorarioExternoDTO[].class);
            List<HorarioExternoDTO> result = Arrays.asList(response != null ? response : new HorarioExternoDTO[0]);
            logger.info("Se obtuvieron {} horarios del grupo", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error consumiendo horarios del grupo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener horarios del grupo: " + e.getMessage(), e);
        }
    }
}
