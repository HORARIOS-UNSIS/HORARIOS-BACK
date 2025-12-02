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

    public SubjectRequestDTO() {
    }

    public SubjectRequestDTO(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}