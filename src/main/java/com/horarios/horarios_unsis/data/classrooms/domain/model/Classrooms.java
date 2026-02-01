package com.horarios.horarios_unsis.data.classrooms.domain.model;

public class Classrooms {
    private Integer idAula;
    private String nombre;
    private Integer capacidad;
    private String clave;
    private String statusProyector;
    private String tipo;

    public Classrooms() {
    }

    public Classrooms(Integer idAula, String nombre, Integer capacidad) {
        this.idAula = idAula;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public Classrooms(Integer idAula, String nombre, Integer capacidad, String clave, String statusProyector, String tipo) {
        this.idAula = idAula;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.clave = clave;
        this.statusProyector = statusProyector;
        this.tipo = tipo;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getStatusProyector() {
        return statusProyector;
    }

    public void setStatusProyector(String statusProyector) {
        this.statusProyector = statusProyector;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
