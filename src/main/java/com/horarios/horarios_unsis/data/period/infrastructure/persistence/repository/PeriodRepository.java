package com.horarios.horarios_unsis.data.period.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.period.infrastructure.persistence.entity.PeriodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<PeriodEntity, Integer> {
    Optional<PeriodEntity> findByClave(String clave);
}
