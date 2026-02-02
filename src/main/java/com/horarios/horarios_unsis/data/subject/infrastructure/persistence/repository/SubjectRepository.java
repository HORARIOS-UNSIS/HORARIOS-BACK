package com.horarios.horarios_unsis.data.subject.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Integer> {
    
    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query(value = "INSERT INTO materia (id_materia, nombre, es_academia) VALUES (:id, :nombre, :esAcademia) " +
                   "ON CONFLICT (id_materia) DO UPDATE SET nombre = :nombre", nativeQuery = true)
    void upsertMateria(@Param("id") Integer id, @Param("nombre") String nombre, @Param("esAcademia") boolean esAcademia);
}