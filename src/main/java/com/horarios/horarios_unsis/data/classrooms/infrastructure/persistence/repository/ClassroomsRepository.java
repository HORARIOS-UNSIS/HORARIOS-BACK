package com.horarios.horarios_unsis.data.classrooms.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.classrooms.infrastructure.persistence.entity.ClassroomsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClassroomsRepository extends JpaRepository<ClassroomsEntity,Integer>{
    Optional<ClassroomsEntity> findByClave(String clave);
} 
