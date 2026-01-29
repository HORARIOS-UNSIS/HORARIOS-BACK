package com.horarios.horarios_unsis.integration.Consume;

import com.horarios.horarios_unsis.integration.Consume.DTO.HorarioExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Cliente para consumir API externa de horarios
 * 
 * Endpoints:
 * - GET /api/horarios/{periodo}/{idprofesor} (horarios de un profesor en periodo)
 * - GET /api/horarios/{periodo}/grupo/{idGrupo} (horarios de un grupo)
 * - GET /api/horarios/{periodo}/grupo/{idGrupo}/materias (materias de un grupo)
 * 
 * Mapeo de Campos:
 * - id_materia → idMateria
 * - id_profesor → idProfesor
 * - id_aula → idAula
 * - id_bloque/numero_bloque → numeroBloque (para SchoolHours)
 * - hora_inicio/hora_fin → startTime/endTime
 * - es_descanso → isBreak
 * - fecha → fecha
 * - grupo → grupo
 */
@Service
public class HorarioConsumeClient {
    
    private static final Logger logger = LoggerFactory.getLogger(HorarioConsumeClient.class);
    private static final String BASE_API_URL = "http://serv-horarios.unsis.lan";
    private static final String ENDPOINT_BASE = "/api/horarios";

    private final RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;

    public HorarioConsumeClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene los horarios de un profesor en un período específico
     * GET /api/horarios/{periodo}/{idprofesor}
     * 
     * @param idPeriodo ID del período académico
     * @param idProfesor ID del profesor
     * @return Lista de horarios del profesor
     */
    public List<HorarioExternoDTO> obtenerHorariosPorProfesor(Integer idPeriodo, Integer idProfesor) {
        if (!integrationEnabled) {
            // Datos ficticios básicos
            HorarioExternoDTO h = new HorarioExternoDTO();
            h.setIdHorario(1);
            h.setIdMateria(1);
            h.setIdProfesor(idProfesor);
            h.setIdAula(1);
            h.setNumeroBloque(1);
            h.setHoraInicio(java.time.LocalTime.of(8,0));
            h.setHoraFin(java.time.LocalTime.of(9,30));
            h.setFecha(java.time.LocalDate.now().plusDays(1));
            return Arrays.asList(h);
        }

        String url = BASE_API_URL + ENDPOINT_BASE + "/" + idPeriodo + "/" + idProfesor;
        logger.info("Obteniendo horarios para profesor {} en periodo {}", idProfesor, idPeriodo);
        
        try {
            HorarioExternoDTO[] response = restTemplate.getForObject(url, HorarioExternoDTO[].class);
            
            List<HorarioExternoDTO> result = Arrays.asList(
                response != null ? response : new HorarioExternoDTO[0]
            );
            
            logger.info("Se obtuvieron {} horarios del profesor", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo horarios del profesor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener horarios del profesor: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los horarios de un grupo en un período específico
     * GET /api/horarios/{periodo}/grupo/{idGrupo}
     * 
     * @param idPeriodo ID del período académico
     * @param idGrupo ID del grupo
     * @return Lista de horarios del grupo
     */
    public List<HorarioExternoDTO> obtenerHorariosPorGrupo(Integer idPeriodo, Integer idGrupo) {
        if (!integrationEnabled) {
            HorarioExternoDTO h = new HorarioExternoDTO();
            h.setIdHorario(10);
            h.setIdMateria(2);
            h.setIdProfesor(5);
            h.setIdAula(2);
            h.setNumeroBloque(2);
            h.setHoraInicio(java.time.LocalTime.of(10,0));
            h.setHoraFin(java.time.LocalTime.of(11,30));
            h.setFecha(java.time.LocalDate.now().plusDays(2));
            return Arrays.asList(h);
        }

        String url = BASE_API_URL + ENDPOINT_BASE + "/" + idPeriodo + "/grupo/" + idGrupo;
        logger.info("Obteniendo horarios para grupo {} en periodo {}", idGrupo, idPeriodo);
        
        try {
            HorarioExternoDTO[] response = restTemplate.getForObject(url, HorarioExternoDTO[].class);
            
            List<HorarioExternoDTO> result = Arrays.asList(
                response != null ? response : new HorarioExternoDTO[0]
            );
            
            logger.info("Se obtuvieron {} horarios del grupo", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo horarios del grupo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener horarios del grupo: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todas las materias asignadas a un grupo en un período
     * GET /api/horarios/{periodo}/grupo/{idGrupo}/materias
     * 
     * @param idPeriodo ID del período académico
     * @param idGrupo ID del grupo
     * @return Lista de materias del grupo
     */
    public List<HorarioExternoDTO> obtenerMateriasPorGrupo(Integer idPeriodo, Integer idGrupo) {
        if (!integrationEnabled) {
            HorarioExternoDTO h = new HorarioExternoDTO();
            h.setIdHorario(11);
            h.setIdMateria(3);
            h.setIdProfesor(6);
            h.setIdAula(2);
            h.setNumeroBloque(3);
            h.setHoraInicio(java.time.LocalTime.of(13,0));
            h.setHoraFin(java.time.LocalTime.of(14,30));
            h.setFecha(java.time.LocalDate.now().plusDays(3));
            return Arrays.asList(h);
        }

        String url = BASE_API_URL + ENDPOINT_BASE + "/" + idPeriodo + "/grupo/" + idGrupo + "/materias";
        logger.info("Obteniendo materias para grupo {} en periodo {}", idGrupo, idPeriodo);
        
        try {
            HorarioExternoDTO[] response = restTemplate.getForObject(url, HorarioExternoDTO[].class);
            
            List<HorarioExternoDTO> result = Arrays.asList(
                response != null ? response : new HorarioExternoDTO[0]
            );
            
            logger.info("Se obtuvieron {} materias del grupo", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo materias del grupo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener materias del grupo: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los horarios de un período
     * GET /api/horarios/{periodo}
     * 
     * @param idPeriodo ID del período académico
     * @return Lista de todos los horarios del período
     */
    public List<HorarioExternoDTO> obtenerTodosHorariosPorPeriodo(Integer idPeriodo) {
        if (!integrationEnabled) {
            HorarioExternoDTO h = new HorarioExternoDTO();
            h.setIdHorario(20);
            h.setIdMateria(4);
            h.setIdProfesor(7);
            h.setIdAula(3);
            h.setNumeroBloque(4);
            h.setHoraInicio(java.time.LocalTime.of(15,0));
            h.setHoraFin(java.time.LocalTime.of(16,30));
            h.setFecha(java.time.LocalDate.now().plusDays(4));
            return Arrays.asList(h);
        }

        String url = BASE_API_URL + ENDPOINT_BASE + "/" + idPeriodo;
        logger.info("Obteniendo todos los horarios para periodo {}", idPeriodo);
        
        try {
            HorarioExternoDTO[] response = restTemplate.getForObject(url, HorarioExternoDTO[].class);
            
            List<HorarioExternoDTO> result = Arrays.asList(
                response != null ? response : new HorarioExternoDTO[0]
            );
            
            logger.info("Se obtuvieron {} horarios totales del periodo", result.size());
            return result;
            
        } catch (Exception e) {
            logger.error("Error consumiendo horarios del periodo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener horarios del período: " + e.getMessage(), e);
        }
    }
}
