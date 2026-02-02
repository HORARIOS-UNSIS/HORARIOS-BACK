package com.horarios.horarios_unsis.shared.services;

import com.horarios.horarios_unsis.shared.models.ExamScheduleRequest;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para búsqueda de disponibilidad y asignación de horarios
 * Considera restricciones, disponibilidad de recursos y optimización
 */
@Service
public class BuscadorDisponibilidadService {
    
    private static final Logger logger = LoggerFactory.getLogger(BuscadorDisponibilidadService.class);

    /**
     * Busca la hora óptima considerando:
     * 1. Menor número de clases afectadas
     * 2. Aulas disponibles (salas para salud)
     * 3. Exclusión de horas de inglés
     * 4. Disponibilidad de profesor aplicador
     */
    public ExamScheduleRequest encontrarHoraOptima(Integer idGrupo, 
                                                    Integer idProfesorAplicador,
                                                    ExamScheduleRequest solicitud) {
        logger.info("Buscando hora óptima para grupo {} en fecha {}", 
                   idGrupo, solicitud.getFechaExamen());
        
        // TODO: Implementar algoritmo de búsqueda:
        // 1. Obtener todos los bloques de horario disponibles
        // 2. Para cada bloque, calcular:
        //    - Clases afectadas
        //    - Si hay inglés
        //    - Disponibilidad de profesor
        //    - Disponibilidad de aula/sala
        // 3. Seleccionar el bloque con menor impacto
        
        return solicitud;
    }

    /**
     * Verifica disponibilidad simultánea de recursos:
     * - Profesor aplicador
     * - Profesor titular (no debe tener clase)
     * - Aula/Sala
     * - No conflicto con inglés
     */
    public boolean verificarDisponibilidadCompleta(ExamScheduleRequest request) {
        logger.info("Verificando disponibilidad completa para examen de grupo: {}", 
                   request.getIdGrupo());
        
        // TODO: Verificaciones en orden de importancia
        
        return true;
    }

    /**
     * Para área de Salud: Busca preferentemente salas disponibles
     */
    public Integer seleccionarSalaOptima(ExamScheduleRequest request) {
        logger.info("Seleccionando sala óptima para área: {}", request.getArea());
        
        if (!"SALUD".equals(request.getArea())) {
            // Si no es salud, usar cualquier aula
            return request.getIdAula();
        }
        
        // TODO: Para salud, preferir salas especializadas
        // Salas de cómputo (ORDIS) si es examen en computadora
        
        return request.getIdAula();
    }

    /**
     * Calcula el impacto de un horario propuesto
     * Retorna un score de 0-100 donde 0 es óptimo
     */
    public Integer calcularImpactoHorario(ExamScheduleRequest request) {
        logger.info("Calculando impacto del horario propuesto");
        
        int score = 0;
        
        // Penalizar por clases afectadas
        // TODO: score += clasesAfectadas * ExamConstants.PESO_CLASE_AFECTADA
        
        // Penalizar si afecta inglés
        // TODO: if (afectaIngles) score += ExamConstants.PENALIDAD_INGLES
        
        // Premiar si es sala (para salud)
        // TODO: if (esSala) score -= ExamConstants.BONUS_SALA
        
        return score;
    }
}
