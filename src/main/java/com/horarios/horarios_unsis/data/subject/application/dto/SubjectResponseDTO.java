package com.horarios.horarios_unsis.data.subject.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta de una materia")
public class SubjectResponseDTO {
    
    @Schema(description = "ID único de la materia", example = "1")
    private Integer idMateria;
    
    @Schema(description = "Nombre de la materia", example = "Programación Orientada a Objetos")
    private String nombre;

    @Schema(description = "Si es materia de academia (true) o regular (false)", example = "false")
    private Boolean esAcademia;

    public SubjectResponseDTO() {
        this.esAcademia = false;
    }

    public SubjectResponseDTO(Integer idMateria, String nombre) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.esAcademia = false;
    }

    public SubjectResponseDTO(Integer idMateria, String nombre, Boolean esAcademia) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.esAcademia = esAcademia != null ? esAcademia : false;
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

    public Boolean getEsAcademia() {
        return esAcademia;
    }

    public void setEsAcademia(Boolean esAcademia) {
        this.esAcademia = esAcademia != null ? esAcademia : false;
    }

    @Override
    public String toString() {
        return "SubjectResponseDTO{" +
                "idMateria=" + idMateria +
                ", nombre='" + nombre + '\'' +
                ", esAcademia=" + esAcademia +
                '}';
    }
}
