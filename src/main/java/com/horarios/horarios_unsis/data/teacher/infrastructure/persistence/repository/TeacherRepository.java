package com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {
    // Métodos de consulta personalizados pueden ser agregados aquí
}