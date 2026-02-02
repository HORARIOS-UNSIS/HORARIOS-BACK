package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para datos de periodos desde API externa
 * Endpoint: GET /api/periodo/lista
 * 
 * Estructura JSON:
 * {
 *   "clave": "1516A",
 *   "nombre": "SEMESTREOCT/15-FEB/16",
 *   "tipo": "A",
 *   "fInicio": "2015-10-05",
 *   "fFin": "2016-02-14"
 * }
 */
public class PeriodoExternoDTO {
    
    @JsonProperty("clave")
    private String clave;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("tipo")
    private String tipo;
    
    @JsonProperty("fInicio")
    private String fechaInicio;
    
    @JsonProperty("fFin")
    private String fechaFin;

    public PeriodoExternoDTO() {
    }

    public PeriodoExternoDTO(String clave, String nombre, String tipo, String fechaInicio, String fechaFin) {
        this.clave = clave;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public String toString() {
        return "PeriodoExternoDTO{" +
                "clave='" + clave + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaFin='" + fechaFin + '\'' +
                '}';
    }
}
