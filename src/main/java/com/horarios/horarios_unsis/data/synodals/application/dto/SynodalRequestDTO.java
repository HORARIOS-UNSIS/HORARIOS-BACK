package com.horarios.horarios_unsis.data.synodals.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para crear/actualizar un sinodal")
public class SynodalRequestDTO {
    
    @Schema(description = "ID del profesor que será sinodal", example = "1")
    @NotNull(message = "El ID del profesor sinodal es obligatorio")
    private Integer idProfesorSinodal;

    @Schema(description = "ID del profesor titular de la materia", example = "2")
    @NotNull(message = "El ID del profesor titular es obligatorio")
    private Integer idProfesorTitular;
    @Schema(description = "ID de la materia", example = "1")
    @NotNull(message = "El ID de la materia es obligatorio")
    private Integer idMateria;

    public SynodalRequestDTO() {
    }

    public SynodalRequestDTO(Integer idProfesorSinodal, Integer idProfesorTitular, Integer idMateria) {
        this.idProfesorSinodal = idProfesorSinodal;
        this.idProfesorTitular = idProfesorTitular;
        this.idMateria = idMateria;
    }

    public Integer getIdProfesorSinodal() {
        return idProfesorSinodal;
    }

    public void setIdProfesorSinodal(Integer idProfesorSinodal) {
        this.idProfesorSinodal = idProfesorSinodal;
    }

    public Integer getIdProfesorTitular() {
        return idProfesorTitular;
    }

    public void setIdProfesorTitular(Integer idProfesorTitular) {
        this.idProfesorTitular = idProfesorTitular;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }
}
