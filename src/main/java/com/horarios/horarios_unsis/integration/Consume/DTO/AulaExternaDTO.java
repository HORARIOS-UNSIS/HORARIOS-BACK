package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para datos de aulas desde API externa
 * Estructura esperada: GET /api/aulas
 */
public class AulaExternaDTO {
    
    @JsonProperty("id")
    private Integer idAula;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("capacidad")
    private Integer capacidad;

    public AulaExternaDTO() {
    }

    public AulaExternaDTO(Integer idAula, String nombre, Integer capacidad) {
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

    @Override
    public String toString() {
        return "AulaExternaDTO{" +
                "idAula=" + idAula +
                ", nombre='" + nombre + '\'' +
                ", capacidad=" + capacidad +
                '}';
    }
}
