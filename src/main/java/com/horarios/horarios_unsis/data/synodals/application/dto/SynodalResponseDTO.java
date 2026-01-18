package com.horarios.horarios_unsis.data.synodals.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de un sinodal")
public class SynodalResponseDTO {
    
    @Schema(description = "ID del sinodal", example = "1")
    private Integer id;

    @Schema(description = "ID del profesor que será sinodal", example = "1")
    private Integer idProfesorSinodal;

    @Schema(description = "Nombre del profesor sinodal", example = "Juan Pérez")
    private String nombreProfesorSinodal;

    @Schema(description = "ID del profesor titular de la materia", example = "2")
    private Integer idProfesorTitular;

    @Schema(description = "Nombre del profesor titular", example = "María García")
    private String nombreProfesorTitular;

    @Schema(description = "ID de la materia", example = "1")
    private Integer idMateria;

    @Schema(description = "Nombre de la materia", example = "Programación Orientada a Objetos")
    private String nombreMateria;

    public SynodalResponseDTO() {
    }

    public SynodalResponseDTO(Integer id, Integer idProfesorSinodal, String nombreProfesorSinodal, 
                              Integer idProfesorTitular, String nombreProfesorTitular,
                              Integer idMateria, String nombreMateria) {
        this.id = id;
        this.idProfesorSinodal = idProfesorSinodal;
        this.nombreProfesorSinodal = nombreProfesorSinodal;
        this.idProfesorTitular = idProfesorTitular;
        this.nombreProfesorTitular = nombreProfesorTitular;
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProfesorSinodal() {
        return idProfesorSinodal;
    }

    public void setIdProfesorSinodal(Integer idProfesorSinodal) {
        this.idProfesorSinodal = idProfesorSinodal;
    }

    public String getNombreProfesorSinodal() {
        return nombreProfesorSinodal;
    }

    public void setNombreProfesorSinodal(String nombreProfesorSinodal) {
        this.nombreProfesorSinodal = nombreProfesorSinodal;
    }

    public Integer getIdProfesorTitular() {
        return idProfesorTitular;
    }

    public void setIdProfesorTitular(Integer idProfesorTitular) {
        this.idProfesorTitular = idProfesorTitular;
    }

    public String getNombreProfesorTitular() {
        return nombreProfesorTitular;
    }

    public void setNombreProfesorTitular(String nombreProfesorTitular) {
        this.nombreProfesorTitular = nombreProfesorTitular;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }
}
