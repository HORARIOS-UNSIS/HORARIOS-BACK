package com.horarios.horarios_unsis.data.group.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.group.infrastructure.persistence.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Integer> {
    List<GroupEntity> findByClaveCarrera(String claveCarrera);
    List<GroupEntity> findByClavePeriodo(String clavePeriodo);
    List<GroupEntity> findByClaveCarreraAndClavePeriodo(String claveCarrera, String clavePeriodo);
}
