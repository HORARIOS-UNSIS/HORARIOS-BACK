package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para datos de carreras desde API externa
 * Endpoint: GET /api/carreras
 * 
 * Estructura JSON:
 * {
 *   "clave": "01B",
 *   "nombre": "LICENCIATURA EN ADMINISTRACIÓN MUNICIPAL 2015",
 *   "vigente": true
 * }
 */
public class CarreraExternaDTO {
    
    @JsonProperty("clave")
    private String clave;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("vigente")
    private Boolean vigente;

    public CarreraExternaDTO() {
    }

    public CarreraExternaDTO(String clave, String nombre, Boolean vigente) {
        this.clave = clave;
        this.nombre = nombre;
        this.vigente = vigente;
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

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    @Override
    public String toString() {
        return "CarreraExternaDTO{" +
                "clave='" + clave + '\'' +
                ", nombre='" + nombre + '\'' +
                ", vigente=" + vigente +
                '}';
    }
}
