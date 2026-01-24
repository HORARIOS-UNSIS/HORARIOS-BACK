package com.horarios.horarios_unsis.data.subject.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear/actualizar una materia")
public class SubjectRequestDTO {
    
    @Schema(description = "Nombre de la materia", example = "Programación Orientada a Objetos", required = true)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String nombre;

    @Schema(description = "Si es materia de academia (true) o regular (false)", example = "false", required = false)
    private Boolean esAcademia = false;

    public SubjectRequestDTO() {
        this.esAcademia = false;
    }

    public SubjectRequestDTO(String nombre) {
        this.nombre = nombre;
        this.esAcademia = false;
    }

    public SubjectRequestDTO(String nombre, Boolean esAcademia) {
        this.nombre = nombre;
        this.esAcademia = esAcademia != null ? esAcademia : false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEsAcademia() {
        return esAcademia;
    }

    public void setEsAcademia(Boolean esAcademia) {
        this.esAcademia = esAcademia != null ? esAcademia : false;
    }
}