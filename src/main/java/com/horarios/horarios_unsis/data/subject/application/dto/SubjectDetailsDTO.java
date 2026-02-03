package com.horarios.horarios_unsis.data.subject.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalles de materia, grupo y profesor")
public class SubjectDetailsDTO {

    @Schema(description = "Nombre de la materia", example = "Programación I")
    private String nombre;

    @Schema(description = "Indica si es materia de academia", example = "true")
    private Boolean esAcademia;

    @Schema(description = "Clave del grupo", example = "106")
    private String claveGrupo;

    @Schema(description = "Nombre del profesor", example = "Juan Perez")
    private String nombreProfesor;

    public SubjectDetailsDTO(String nombre, Boolean esAcademia, String claveGrupo, String nombreProfesor) {
        this.nombre = nombre;
        this.esAcademia = esAcademia;
        this.claveGrupo = claveGrupo;
        this.nombreProfesor = nombreProfesor;
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
        this.esAcademia = esAcademia;
    }

    public String getClaveGrupo() {
        return claveGrupo;
    }

    public void setClaveGrupo(String claveGrupo) {
        this.claveGrupo = claveGrupo;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
}
