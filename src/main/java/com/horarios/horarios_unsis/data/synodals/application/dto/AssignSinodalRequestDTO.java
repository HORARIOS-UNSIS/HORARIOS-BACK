package com.horarios.horarios_unsis.data.synodals.application.dto;

public class AssignSinodalRequestDTO {
    private Integer idMateria;
    private Integer idProfesorTitular;
    private Integer idProfesorSinodal;

    // Getters and Setters
    public Integer getIdMateria() { return idMateria; }
    public void setIdMateria(Integer idMateria) { this.idMateria = idMateria; }
    public Integer getIdProfesorTitular() { return idProfesorTitular; }
    public void setIdProfesorTitular(Integer idProfesorTitular) { this.idProfesorTitular = idProfesorTitular; }
    public Integer getIdProfesorSinodal() { return idProfesorSinodal; }
    public void setIdProfesorSinodal(Integer idProfesorSinodal) { this.idProfesorSinodal = idProfesorSinodal; }
}
