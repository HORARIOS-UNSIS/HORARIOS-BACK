package com.horarios.horarios_unsis.data.synodals.application.dto;

import java.util.List;

public class SinodalAssignmentDTO {
    private String nombreMateria;
    private String nombreProfesorTitular;
    private Integer idMateria;
    private Integer idProfesorTitular;
    private Integer semestre; // Ojo: La tabla grupos tiene semestre, asignacion no. Habría que hacer join con tabla grupos.
    private List<SinodalDTO> sinodales;

    public SinodalAssignmentDTO(String nombreMateria, String nombreProfesorTitular, Integer idMateria, Integer idProfesorTitular, List<SinodalDTO> sinodales) {
        this.nombreMateria = nombreMateria;
        this.nombreProfesorTitular = nombreProfesorTitular;
        this.idMateria = idMateria;
        this.idProfesorTitular = idProfesorTitular;
        this.sinodales = sinodales;
    }

    // Getters and Setters
    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }
    public String getNombreProfesorTitular() { return nombreProfesorTitular; }
    public void setNombreProfesorTitular(String nombreProfesorTitular) { this.nombreProfesorTitular = nombreProfesorTitular; }
    public Integer getIdMateria() { return idMateria; }
    public void setIdMateria(Integer idMateria) { this.idMateria = idMateria; }
    public Integer getIdProfesorTitular() { return idProfesorTitular; }
    public void setIdProfesorTitular(Integer idProfesorTitular) { this.idProfesorTitular = idProfesorTitular; }
    public List<SinodalDTO> getSinodales() { return sinodales; }
    public void setSinodales(List<SinodalDTO> sinodales) { this.sinodales = sinodales; }

    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
}
