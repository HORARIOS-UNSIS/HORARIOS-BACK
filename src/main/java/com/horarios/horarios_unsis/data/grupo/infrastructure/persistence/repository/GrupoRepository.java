package com.horarios.horarios_unsis.data.grupo.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.grupo.infrastructure.persistence.entity.GrupoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<GrupoEntity, Integer> {
    
    Optional<GrupoEntity> findByClaveAndClavePeriodo(String clave, String clavePeriodo);
    
    List<GrupoEntity> findByClavePeriodo(String clavePeriodo);
    
    List<GrupoEntity> findByClaveCarrera(String claveCarrera);
    
    List<GrupoEntity> findBySemestre(Integer semestre);
    
    List<GrupoEntity> findByClavePeriodoAndClaveCarrera(String clavePeriodo, String claveCarrera);
    
    boolean existsByClaveAndClavePeriodo(String clave, String clavePeriodo);
}
