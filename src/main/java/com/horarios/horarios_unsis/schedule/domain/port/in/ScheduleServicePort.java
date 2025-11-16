package com.horarios.horarios_unsis.schedule.domain.port.in;

import com.horarios.horarios_unsis.schedule.domain.model.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para operaciones de Schedule (Hexagonal Architecture)
 */
public interface ScheduleServicePort {

    // Crear un nuevo examen
    Schedule createSchedule(Schedule schedule);
    
    // Obtener examen por ID
    Optional<Schedule> getScheduleById(Integer id);
    
    // Obtener todos los exámenes
    List<Schedule> getAllSchedules();
    
    // Actualizar un examen existente
    Schedule updateSchedule(Integer id, Schedule schedule);
    
    // Eliminar un examen
    void deleteSchedule(Integer id);
    
    // Buscar exámenes por materia
    List<Schedule> getSchedulesByMateria(Integer idMateria);
    
    // Buscar exámenes por profesor
    List<Schedule> getSchedulesByProfesor(Integer profesorId);
    
    // Buscar exámenes por fecha
    List<Schedule> getSchedulesByFecha(LocalDate fecha);
    
    // Buscar exámenes por periodo
    List<Schedule> getSchedulesByPeriodo(Integer idPeriodo);
    
    // Buscar exámenes por status
    List<Schedule> getSchedulesByStatus(String status);
    
    // Buscar exámenes entre fechas
    List<Schedule> getSchedulesByFechaRange(LocalDate fechaInicio, LocalDate fechaFin);
}
