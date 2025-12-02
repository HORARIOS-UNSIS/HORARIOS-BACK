package com.horarios.horarios_unsis.data.subject.domain.model;

public class Subject {
    private Integer idMateria;
    private String nombre;

    public Subject() {
    }

    public Subject(Integer idMateria, String nombre) {
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