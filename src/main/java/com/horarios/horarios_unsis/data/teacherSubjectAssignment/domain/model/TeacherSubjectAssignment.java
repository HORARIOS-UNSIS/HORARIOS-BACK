package com.horarios.horarios_unsis.data.teacherSubjectAssignment.domain.model;

import java.time.LocalDateTime;

public class TeacherSubjectAssignment {
    
    private Integer idAsignacion;
    private String claveAula;
    private String claveCarrera;
    private String claveGrupo;
    private String claveMateria;
    private String clavePeriodo;
    private Integer dia;
    private Integer hora;
    private Integer idProfesor;
    private String nombreMateria;
    private String nombreProfesor;
    private Boolean activo;
    private LocalDateTime fechaSincronizacion;

    public TeacherSubjectAssignment() {
    }

    public TeacherSubjectAssignment(Integer idAsignacion, String claveAula, String claveCarrera, 
                                   String claveGrupo, String claveMateria, String clavePeriodo, 
                                   Integer dia, Integer hora, Integer idProfesor, 
                                   String nombreMateria, String nombreProfesor, 
                                   Boolean activo, LocalDateTime fechaSincronizacion) {
        this.idAsignacion = idAsignacion;
        this.claveAula = claveAula;
        this.claveCarrera = claveCarrera;
        this.claveGrupo = claveGrupo;
        this.claveMateria = claveMateria;
        this.clavePeriodo = clavePeriodo;
        this.dia = dia;
        this.hora = hora;
        this.idProfesor = idProfesor;
        this.nombreMateria = nombreMateria;
        this.nombreProfesor = nombreProfesor;
        this.activo = activo;
        this.fechaSincronizacion = fechaSincronizacion;
    }

    public Integer getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(Integer idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public String getClaveAula() {
        return claveAula;
    }

    public void setClaveAula(String claveAula) {
        this.claveAula = claveAula;
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

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(LocalDateTime fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }
}
