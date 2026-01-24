package com.horarios.horarios_unsis.shared.validators;

import com.horarios.horarios_unsis.shared.models.ExamScheduleRequest;
import com.horarios.horarios_unsis.shared.ExamConstants;
import org.springframework.stereotype.Component;

/**
 * Validador para solicitudes de creación de horarios de examen
 */
@Component
public class ExamScheduleValidator {

    /**
     * Valida que el profesor aplicador tenga disponibilidad
     * (no tenga clases en esa hora)
     */
    public boolean validarDisponibilidadProfesorAplicador(Integer idProfesor, 
                                                          ExamScheduleRequest request) {
        // TODO: Consultar base de datos de horarios de clases
        // Verificar que el profesor NO tenga clases en fecha y hora del examen
        return true;
    }

    /**
     * Valida que el profesor aplicador sea de la licenciatura
     */
    public boolean validarProfesorDeLicenciatura(Integer idProfesor) {
        // TODO: Verificar que el profesor pertenece a la licenciatura
        return true;
    }

    /**
     * Valida que para exámenes de academia, los sinodales estén en esa academia
     */
    public boolean validarSinodalesEnAcademia(Integer idProfesorSinodal, Integer idAcademia) {
        if (idProfesorSinodal == null || idAcademia == null) {
            return true;
        }
        // TODO: Verificar que el sinodal pertenece a la academia
        return true;
    }

    /**
     * Valida que NO se afecten horas de inglés
     */
    public boolean validarExclusionIngles(ExamScheduleRequest request) {
        // TODO: Verificar que el grupo no tenga inglés en esa hora
        // Consultar todas las horas de inglés del grupo
        return true;
    }

    /**
     * Busca la hora óptima considerando:
     * - Menor número de clases afectadas
     * - Preferentemente en salas (para área de salud)
     * - Evitando horas de inglés
     */
    public ExamScheduleRequest buscarHoraOptima(ExamScheduleRequest request) {
        // TODO: Implementar algoritmo de búsqueda de hora óptima
        return request;
    }

    /**
     * Valida que el aula esté disponible en fecha y hora
     */
    public boolean validarDisponibilidadAula(Integer idAula, ExamScheduleRequest request) {
        // TODO: Consultar disponibilidad de aula
        return true;
    }

    /**
     * Valida que la duración sea correcta según tipo de examen
     * - Parciales: 1 hora
     * - Ordinarios: 2 horas (para TODAS las áreas)
     * - Extraordinarios: 1 hora
     * - Especiales: flexible
     */
    public boolean validarDuracionExamen(ExamScheduleRequest request) {
        switch (request.getTipoExamen()) {
            case ExamConstants.TIPO_PARCIAL:
                return request.getDuracionMinutos() == ExamConstants.DURACION_PARCIAL;
            case ExamConstants.TIPO_ORDINARIO:
                // Todos los ordinarios son 2 horas
                return request.getDuracionMinutos() == ExamConstants.DURACION_ORDINARIO;
            case ExamConstants.TIPO_EXTRAORDINARIO:
                return request.getDuracionMinutos() == ExamConstants.DURACION_EXTRAORDINARIO;
            case ExamConstants.TIPO_ESPECIAL:
                // Los especiales son flexibles
                return true;
            default:
                return true;
        }
    }
}
