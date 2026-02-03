package com.horarios.horarios_unsis.data.synodals.application.dto;

public class SinodalDTO {
    private Integer idSinodal; // ID de la tabla sinodales
    private Integer idProfesor; // ID del profesor
    private String nombre;

    public SinodalDTO(Integer idSinodal, Integer idProfesor, String nombre) {
        this.idSinodal = idSinodal;
        this.idProfesor = idProfesor;
        this.nombre = nombre;
    }

    public Integer getIdSinodal() { return idSinodal; }
    public void setIdSinodal(Integer idSinodal) { this.idSinodal = idSinodal; }
    public Integer getIdProfesor() { return idProfesor; }
    public void setIdProfesor(Integer idProfesor) { this.idProfesor = idProfesor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
