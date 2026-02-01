package com.horarios.horarios_unsis.data.subjectGroup.domain.model;

import java.time.LocalDateTime;

public class SubjectGroup {
    private Integer idMateriaGrupo;
    private Boolean activo;
    private String claveCarrera;
    private String claveGrupo;
    private String claveMateria;
    private String clavePeriodo;
    private LocalDateTime fechaSincronizacion;
    private Integer horasSemana;
    private Integer idProfesor;
    private String nombreMateria;
    private String nombreProfesor;

    public SubjectGroup() {
    }

    public SubjectGroup(Integer idMateriaGrupo, Boolean activo, String claveCarrera, String claveGrupo, String claveMateria, String clavePeriodo, LocalDateTime fechaSincronizacion, Integer horasSemana, Integer idProfesor, String nombreMateria, String nombreProfesor) {
        this.idMateriaGrupo = idMateriaGrupo;
        this.activo = activo;
        this.claveCarrera = claveCarrera;
        this.claveGrupo = claveGrupo;
        this.claveMateria = claveMateria;
        this.clavePeriodo = clavePeriodo;
        this.fechaSincronizacion = fechaSincronizacion;
        this.horasSemana = horasSemana;
        this.idProfesor = idProfesor;
        this.nombreMateria = nombreMateria;
        this.nombreProfesor = nombreProfesor;
    }

    public Integer getIdMateriaGrupo() {
        return idMateriaGrupo;
    }

    public void setIdMateriaGrupo(Integer idMateriaGrupo) {
        this.idMateriaGrupo = idMateriaGrupo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getClaveCarrera() {
        return claveCarrera;
    }

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public String getClaveGrupo() {
        return claveGrupo;
    }

    public void setClaveGrupo(String claveGrupo) {
        this.claveGrupo = claveGrupo;
    }

    public String getClaveMateria() {
        return claveMateria;
    }

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }

    public String getClavePeriodo() {
        return clavePeriodo;
    }

    public void setClavePeriodo(String clavePeriodo) {
        this.clavePeriodo = clavePeriodo;
    }

    public LocalDateTime getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(LocalDateTime fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }

    public Integer getHorasSemana() {
        return horasSemana;
    }

    public void setHorasSemana(Integer horasSemana) {
        this.horasSemana = horasSemana;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
}
