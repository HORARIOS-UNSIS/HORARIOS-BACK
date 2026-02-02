package com.horarios.horarios_unsis.data.periodo.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.periodo.infrastructure.persistence.entity.PeriodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface PeriodoRepository extends JpaRepository<PeriodoEntity, Integer> {
    
    Optional<PeriodoEntity> findByClave(String clave);
    
    List<PeriodoEntity> findByActivo(Boolean activo);
    
    List<PeriodoEntity> findByTipo(String tipo);
    
    boolean existsByClave(String clave);
    
    /**
     * Encuentra el período actual basado en la fecha
     */
    @Query("SELECT p FROM PeriodoEntity p WHERE p.fechaInicio <= :fecha AND p.fechaFin >= :fecha ORDER BY p.fechaInicio DESC")
    List<PeriodoEntity> findByFechaActual(@Param("fecha") LocalDate fecha);
    
    /**
     * Encuentra el período más reciente (último por fecha de inicio)
     */
    @Query("SELECT p FROM PeriodoEntity p ORDER BY p.fechaInicio DESC")
    List<PeriodoEntity> findAllOrderByFechaInicioDesc();
}
