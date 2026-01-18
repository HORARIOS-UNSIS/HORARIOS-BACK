package com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.entity.SynodalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SynodalRepository extends JpaRepository<SynodalEntity, Long> {
    List<SynodalEntity> findByMateria_Id(Integer idMateria);
    List<SynodalEntity> findByProfesorSinodal_IdOrProfesorTitular_Id(Long idSinodal, Long idTitular);
    List<SynodalEntity> findByProfesorTitular_Id(Long idProfesor);
    List<SynodalEntity> findByProfesorSinodal_Id(Long idProfesor);
}
