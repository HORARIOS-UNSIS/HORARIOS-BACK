package com.horarios.horarios_unsis.data.subject.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.projection.SubjectDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Integer> {
    
    @Modifying
    @Query(value = "INSERT INTO materia (id_materia, nombre, activo) VALUES (:id, :nombre, :activo) " +
                   "ON CONFLICT (id_materia) DO UPDATE SET nombre = :nombre", nativeQuery = true)
    void upsertMateria(@Param("id") Integer id, @Param("nombre") String nombre, @Param("activo") boolean activo);

    @Query(value = "SELECT DISTINCT a.nombre_materia as nombre, COALESCE(m.es_academia, false) as esAcademia, a.clave_grupo as claveGrupo, a.nombre_profesor as nombreProfesor " +
                   "FROM asignacion_profesor_materia a " +
                   "LEFT JOIN materia m ON TRIM(UPPER(m.nombre)) = TRIM(UPPER(a.nombre_materia)) " +
                   "WHERE a.clave_carrera LIKE CONCAT('%', :carrera, '%') AND a.clave_periodo = :periodo", nativeQuery = true)
    List<SubjectDetailsProjection> findSubjectsByCareerAndPeriod(@Param("carrera") String carrera, @Param("periodo") String periodo);
}