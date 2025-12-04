package com.horarios.horarios_unsis.data.teacher.domain.model;

public class Teacher {
    private Integer idProfesor;
    private String nombre;
    private Boolean sabatico;

    public Teacher() {
    }

    public Teacher(Integer idProfesor, String nombre, Boolean sabatico) {
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