package com.horarios.horarios_unsis.data.period.domain.model;

import java.time.LocalDate;

public class Period {
    private Integer idPeriodo;
    private Boolean activo;
    private String clave;
    private LocalDate fechaFin;
    private LocalDate fechaInicio;
    private String nombre;
    private String tipo;

    public Period() {
    }

    public Period(Integer idPeriodo, Boolean activo, String clave, LocalDate fechaFin, LocalDate fechaInicio, String nombre, String tipo) {
        this.idPeriodo = idPeriodo;
        this.activo = activo;
        this.clave = clave;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
