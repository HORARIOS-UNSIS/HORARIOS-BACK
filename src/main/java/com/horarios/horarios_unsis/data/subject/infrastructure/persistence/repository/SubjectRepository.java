package com.horarios.horarios_unsis.data.subject.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Integer> {
    // Métodos de consulta personalizados pueden ser agregados aquí
}