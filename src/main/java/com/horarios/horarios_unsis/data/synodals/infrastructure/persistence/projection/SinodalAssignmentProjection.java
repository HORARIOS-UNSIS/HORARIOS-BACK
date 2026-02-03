package com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.projection;

public interface SinodalAssignmentProjection {
    String getNombreMateria();
    Integer getIdMateria();
    String getNombreProfesorTitular();
    Integer getIdProfesorTitular();
    Integer getSemestre();
}
