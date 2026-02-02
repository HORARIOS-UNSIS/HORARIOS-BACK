package com.horarios.horarios_unsis.data.subject.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Integer> {
    
    @Modifying
    @Query(value = "INSERT INTO materia (id_materia, nombre, activo) VALUES (:id, :nombre, :activo) " +
                   "ON CONFLICT (id_materia) DO UPDATE SET nombre = :nombre", nativeQuery = true)
    void upsertMateria(@Param("id") Integer id, @Param("nombre") String nombre, @Param("activo") boolean activo);
}