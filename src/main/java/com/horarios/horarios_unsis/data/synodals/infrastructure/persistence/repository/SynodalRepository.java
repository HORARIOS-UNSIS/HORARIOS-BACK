package com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.entity.SynodalEntity;
import com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.projection.SinodalAssignmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SynodalRepository extends JpaRepository<SynodalEntity, Integer> {
    
    // Consulta para obtener la "Tabla Base" de asignaciones (Profesor Titular - Materia)
    // Se une con 'materia' para obtener el id_materia (requerido para la tabla sinodales)
    // Se une con 'grupos' para obtener el semestre
    @Query(value = "SELECT DISTINCT " +
           "m.nombre as nombreMateria, " +
           "m.id_materia as idMateria, " +
           "a.nombre_profesor as nombreProfesorTitular, " +
           "a.id_profesor as idProfesorTitular, " +
           "g.semestre as semestre " +
           "FROM asignacion_profesor_materia a " +
           "JOIN materia m ON TRIM(UPPER(m.nombre)) = TRIM(UPPER(a.nombre_materia)) " +
           "JOIN grupos g ON g.clave = a.clave_grupo AND g.clave_periodo = a.clave_periodo " +
           "WHERE UPPER(a.clave_carrera) LIKE UPPER(CONCAT('%', :carrera, '%')) AND a.clave_periodo = :periodo " +
           "ORDER BY m.nombre", nativeQuery = true)
    List<SinodalAssignmentProjection> findAssignmentsByCareerAndPeriod(@Param("carrera") String carrera, @Param("periodo") String periodo);

    List<SynodalEntity> findByMateria_IdMateriaAndProfesorTitular_IdProfesor(Integer idMateria, Integer idTitular);
}
