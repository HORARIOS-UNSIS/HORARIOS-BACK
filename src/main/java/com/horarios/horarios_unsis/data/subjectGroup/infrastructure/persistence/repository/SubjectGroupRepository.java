package com.horarios.horarios_unsis.data.subjectGroup.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.subjectGroup.infrastructure.persistence.entity.SubjectGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectGroupRepository extends JpaRepository<SubjectGroupEntity, Integer> {
    List<SubjectGroupEntity> findByClaveCarrera(String claveCarrera);
    List<SubjectGroupEntity> findByClavePeriodo(String clavePeriodo);
    List<SubjectGroupEntity> findByClaveGrupo(String claveGrupo);
    List<SubjectGroupEntity> findByIdProfesor(Integer idProfesor);
}
