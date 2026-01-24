package com.horarios.horarios_unsis.data.subject.domain.model;

public class Subject {
    private Integer idMateria;
    private String nombre;
    private Boolean esAcademia;  // true si es materia de academia

    public Subject() {
        this.esAcademia = false;
    }

    public Subject(Integer idMateria, String nombre) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.esAcademia = false;
    }

    public Subject(Integer idMateria, String nombre, Boolean esAcademia) {
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
}