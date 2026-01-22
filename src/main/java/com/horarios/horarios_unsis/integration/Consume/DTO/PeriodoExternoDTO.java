package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para datos de periodos desde API externa
 * Estructura esperada: GET /api/periodo/actual o GET /api/periodos
 */
public class PeriodoExternoDTO {
    
    @JsonProperty("id")
    private Integer idPeriodo;
    
    @JsonProperty("numero")
    private Integer numero;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("activo")
    private Boolean activo;

    public PeriodoExternoDTO() {
    }

    public PeriodoExternoDTO(Integer idPeriodo, Integer numero, String nombre, Boolean activo) {
        this.idPeriodo = idPeriodo;
        this.numero = numero;
        this.nombre = nombre;
        this.activo = activo;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "PeriodoExternoDTO{" +
                "idPeriodo=" + idPeriodo +
                ", numero=" + numero +
                ", nombre='" + nombre + '\'' +
                ", activo=" + activo +
                '}';
    }
}
