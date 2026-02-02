package com.horarios.horarios_unsis.data.group.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta de un grupo")
public class GroupResponseDTO {

    @Schema(description = "Identificador único del grupo", example = "1")
    private Integer idGrupo;

    @Schema(description = "Número de alumnos", example = "25")
    private Integer alumnos;

    @Schema(description = "Clave del grupo", example = "G306")
    private String clave;

    @Schema(description = "Clave de la carrera", example = "LISI")
    private String claveCarrera;

    @Schema(description = "Clave del periodo", example = "2024-2025-A")
    private String clavePeriodo;

    @Schema(description = "Nombre del grupo", example = "Grupo 306")
    private String nombre;

    @Schema(description = "Semestre", example = "3")
    private Integer semestre;

    public GroupResponseDTO() {
    }

    public GroupResponseDTO(Integer idGrupo, Integer alumnos, String clave, String claveCarrera, String clavePeriodo, String nombre, Integer semestre) {
        this.idGrupo = idGrupo;
        this.alumnos = alumnos;
        this.clave = clave;
        this.claveCarrera = claveCarrera;
        this.clavePeriodo = clavePeriodo;
        this.nombre = nombre;
        this.semestre = semestre;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Integer alumnos) {
        this.alumnos = alumnos;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClaveCarrera() {
        return claveCarrera;
    }

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public String getClavePeriodo() {
        return clavePeriodo;
    }

    public void setClavePeriodo(String clavePeriodo) {
        this.clavePeriodo = clavePeriodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
}
