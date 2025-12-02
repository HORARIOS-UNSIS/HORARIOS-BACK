package com.horarios.horarios_unsis.data.teacher.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear/actualizar un profesor")
public class TeacherRequestDTO {
    
    @Schema(description = "Nombre completo del profesor", example = "Dr. María González", required = true)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;
    
    @Schema(description = "Indica si el profesor está en año sabático", example = "false")
    private Boolean sabatico;

    public TeacherRequestDTO() {
    }

    public TeacherRequestDTO(String nombre, Boolean sabatico) {
        this.nombre = nombre;
        this.sabatico = sabatico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getSabatico() {
        return sabatico;
    }

    public void setSabatico(Boolean sabatico) {
        this.sabatico = sabatico;
    }
}