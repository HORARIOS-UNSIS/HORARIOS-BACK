package com.horarios.horarios_unsis.schedule.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.schedule.infrastructure.persistence.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {
    
    // Buscar exámenes por materia
    List<ScheduleEntity> findByIdMateria(Integer idMateria);
    
    // Buscar exámenes por aula
    List<ScheduleEntity> findByIdAula(Integer idAula);
    
    // Buscar exámenes por profesor
    List<ScheduleEntity> findByProfesorId(Integer profesorId);
    
    // Buscar exámenes por fecha
    List<ScheduleEntity> findByFecha(LocalDate fecha);
    
    // Buscar exámenes por periodo
    List<ScheduleEntity> findByIdPeriodo(Integer idPeriodo);
    
    // Buscar exámenes por status
    List<ScheduleEntity> findByStatus(String status);
    
    // Buscar exámenes por grupo
    List<ScheduleEntity> findByGrupo(String grupo);
    
    // Buscar exámenes entre fechas
    List<ScheduleEntity> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    @org.springframework.data.jpa.repository.Query(value = "SELECT DISTINCT e.* FROM examen e " +
           "INNER JOIN asignacion_profesor_materia a ON e.clave_materia = a.clave_materia " +
           "AND e.grupo = a.clave_grupo " +
           "WHERE a.clave_carrera LIKE CONCAT(:claveCarrera, '%') " +
           "AND a.clave_periodo = :clavePeriodo", nativeQuery = true)
    List<ScheduleEntity> findByCareerAndPeriod(@org.springframework.data.repository.query.Param("claveCarrera") String claveCarrera, @org.springframework.data.repository.query.Param("clavePeriodo") String clavePeriodo);
}

