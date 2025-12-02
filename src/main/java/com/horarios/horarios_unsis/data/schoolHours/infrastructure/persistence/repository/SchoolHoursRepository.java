package com.horarios.horarios_unsis.data.schoolHours.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.horarios.horarios_unsis.data.schoolHours.infrastructure.persistence.entity.SchoolHoursEntity;

@Repository
public interface SchoolHoursRepository extends JpaRepository<SchoolHoursEntity, Integer>{
    
}
