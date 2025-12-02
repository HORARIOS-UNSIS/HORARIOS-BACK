package com.horarios.horarios_unsis.data.subject.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta de una materia")
public class SubjectResponseDTO {
    
    @Schema(description = "ID único de la materia", example = "1")
    private Integer idMateria;
    
    @Schema(description = "Nombre de la materia", example = "Programación Orientada a Objetos")
    private String nombre;

    public SubjectResponseDTO() {
    }

    public SubjectResponseDTO(Integer idMateria, String nombre) {
        this.idMateria = idMateria;
        this.nombre = nombre;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}