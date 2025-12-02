package com.horarios.horarios_unsis.data.teacher.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta de un profesor")
public class TeacherResponseDTO {
    
    @Schema(description = "ID único del profesor", example = "1")
    private Integer idProfesor;
    
    @Schema(description = "Nombre completo del profesor", example = "Dr. María González")
    private String nombre;
    
    @Schema(description = "Estado sabático del profesor", example = "false")
    private Boolean sabatico;

    public TeacherResponseDTO() {
    }

    public TeacherResponseDTO(Integer idProfesor, String nombre, Boolean sabatico) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.sabatico = sabatico;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
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