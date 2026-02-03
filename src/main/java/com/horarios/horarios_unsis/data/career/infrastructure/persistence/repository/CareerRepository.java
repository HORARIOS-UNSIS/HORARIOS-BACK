package com.horarios.horarios_unsis.data.career.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.career.infrastructure.persistence.entity.CareerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CareerRepository extends JpaRepository<CareerEntity, Integer> {
    Optional<CareerEntity> findByClave(String clave);
}
