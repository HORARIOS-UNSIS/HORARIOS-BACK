package com.horarios.horarios_unsis.data.periodo.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.time.LocalDate;

/**
 * Entidad para almacenar períodos académicos
 * 
 * Estructura de la API externa:
 * {
 *   "clave": "1516A",
 *   "nombre": "SEMESTREOCT/15-FEB/16",
 *   "tipo": "A",
 *   "fInicio": "2015-10-05",
 *   "fFin": "2016-02-14"
 * }
 */
@Entity
@Table(name = "periodos")
public class PeriodoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo")
    private Integer idPeriodo;
    
    @Column(name = "clave", length = 50, unique = true, nullable = false)
    private String clave;
    
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;
    
    @Column(name = "tipo", length = 10)
    private String tipo;
    
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    
    @Column(name = "activo")
    private Boolean activo = false;

    public PeriodoEntity() {
    }

    public PeriodoEntity(String clave, String nombre, String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.clave = clave;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
