package com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.entity.SynodalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SynodalRepository extends JpaRepository<SynodalEntity, Integer> {
    List<SynodalEntity> findByMateria_IdMateria(Integer idMateria);
    List<SynodalEntity> findByProfesorSinodal_IdProfesorOrProfesorTitular_IdProfesor(Integer idSinodal, Integer idTitular);
    List<SynodalEntity> findByProfesorTitular_IdProfesor(Integer idProfesor);
    List<SynodalEntity> findByProfesorSinodal_IdProfesor(Integer idProfesor);
}
