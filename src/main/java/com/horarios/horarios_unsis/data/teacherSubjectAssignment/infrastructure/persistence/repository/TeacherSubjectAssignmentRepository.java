package com.horarios.horarios_unsis.data.teacherSubjectAssignment.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.teacherSubjectAssignment.infrastructure.persistence.entity.TeacherSubjectAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherSubjectAssignmentRepository extends JpaRepository<TeacherSubjectAssignmentEntity, Integer> {

    List<TeacherSubjectAssignmentEntity> findByClavePeriodo(String clavePeriodo);

    List<TeacherSubjectAssignmentEntity> findByIdProfesor(Integer idProfesor);
    
    List<TeacherSubjectAssignmentEntity> findByClaveMateria(String claveMateria);
}
