package com.horarios.horarios_unsis.data.classrooms.domain.model;

public class Classrooms {
    private Integer idAula;
    private String nombre;
    private Integer capacidad;

    public Classrooms() {
    }

    public Classrooms(Integer idAula, String nombre, Integer capacidad) {
        this.idAula = idAula;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public Integer getIdAula() {
        return idAula;
    }

    public void setIdAula(Integer idAula) {
        this.idAula = idAula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
}
