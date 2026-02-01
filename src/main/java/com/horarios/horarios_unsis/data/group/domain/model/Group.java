package com.horarios.horarios_unsis.data.group.domain.model;

public class Group {
    private Integer idGrupo;
    private Integer alumnos;
    private String clave;
    private String claveCarrera;
    private String clavePeriodo;
    private String nombre;
    private Integer semestre;

    public Group() {
    }

    public Group(Integer idGrupo, Integer alumnos, String clave, String claveCarrera, String clavePeriodo, String nombre, Integer semestre) {
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
