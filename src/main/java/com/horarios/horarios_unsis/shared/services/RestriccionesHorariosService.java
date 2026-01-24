package com.horarios.horarios_unsis.shared.services;

import com.horarios.horarios_unsis.shared.models.ExamScheduleRequest;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para gestión de restricciones de horarios
 * Maneja la lógica de exclusión de horas de inglés y otras restricciones
 */
@Service
public class RestriccionesHorariosService {
    
    private static final Logger logger = LoggerFactory.getLogger(RestriccionesHorariosService.class);

    /**
     * Obtiene todas las horas de inglés del grupo
     * Ya que pueden haber múltiples niveles y horarios diferentes
     */
    public boolean tieneClaseDeIngles(Integer idGrupo, ExamScheduleRequest request) {
        logger.info("Verificando horas de inglés para grupo: {}", idGrupo);
        // TODO: Consultar BD de horarios de clases
        // Buscar todas las asignaciones de inglés para este grupo
        return false;
    }

    /**
     * Verifica si el horario propuesto afecta alguna clase
     * Retorna el número de clases que se verían afectadas
     */
    public Integer calcularClasesAfectadas(Integer idGrupo, ExamScheduleRequest request) {
        logger.info("Calculando clases afectadas para grupo: {}", idGrupo);
        // TODO: Consultar horario de clases del grupo en esa fecha/hora
        return 0;
    }

    /**
     * Obtiene disponibilidad de salas para área de salud
     */
    public java.util.List<Integer> obtenerSalasDisponibles(ExamScheduleRequest request) {
        logger.info("Obteniendo salas disponibles para área: {}", request.getArea());
        
        if (!"SALUD".equals(request.getArea())) {
            logger.info("Área {} no es SALUD, no se requieren salas especializadas", request.getArea());
            return java.util.List.of();
        }
        
        // TODO: Consultar todas las salas libres en esa fecha/hora
        return java.util.List.of();
    }
}
