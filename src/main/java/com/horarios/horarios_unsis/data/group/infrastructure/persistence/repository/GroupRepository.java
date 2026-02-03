package com.horarios.horarios_unsis.data.group.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.group.infrastructure.persistence.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {
    List<GroupEntity> findByClaveCarrera(String claveCarrera);
    List<GroupEntity> findByClaveCarreraContaining(String claveCarrera);
    List<GroupEntity> findByClavePeriodo(String clavePeriodo);
    List<GroupEntity> findByClaveCarreraAndClavePeriodo(String claveCarrera, String clavePeriodo);
    List<GroupEntity> findByClaveCarreraContainingAndClavePeriodo(String claveCarrera, String clavePeriodo);
    Optional<GroupEntity> findByClaveAndClavePeriodo(String clave, String clavePeriodo);
}
