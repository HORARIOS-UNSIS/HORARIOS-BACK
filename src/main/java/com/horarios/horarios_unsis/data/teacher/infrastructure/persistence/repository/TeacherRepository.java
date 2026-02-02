package com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {
    
    @Modifying
    @Query(value = "INSERT INTO profesor (id_profesor, nombre, activo) VALUES (:id, :nombre, :activo) " +
                   "ON CONFLICT (id_profesor) DO UPDATE SET nombre = :nombre", nativeQuery = true)
    void upsertProfesor(@Param("id") Integer id, @Param("nombre") String nombre, @Param("activo") boolean activo);
}