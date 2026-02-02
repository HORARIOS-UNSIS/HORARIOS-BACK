package com.horarios.horarios_unsis.data.carrera.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.carrera.infrastructure.persistence.entity.CarreraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CarreraRepository extends JpaRepository<CarreraEntity, Integer> {
    
    Optional<CarreraEntity> findByClave(String clave);
    
    List<CarreraEntity> findByVigente(Boolean vigente);
    
    boolean existsByClave(String clave);
}
