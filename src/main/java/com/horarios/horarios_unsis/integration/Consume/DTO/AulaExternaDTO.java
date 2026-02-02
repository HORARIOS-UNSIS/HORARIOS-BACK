package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para datos de aulas desde API externa
 * Endpoint: GET /api/aulas/
 * 
 * Estructura JSON:
 * {
 *   "clave": "1",
 *   "nombre": "A1",
 *   "capacidad": 18,
 *   "tipo": "AULA",
 *   "statusProyector": "NO_FUNCIONA"
 * }
 */
public class AulaExternaDTO {
    
    @JsonProperty("clave")
    private String clave;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("capacidad")
    private Integer capacidad;
    
    @JsonProperty("tipo")
    private String tipo;
    
    @JsonProperty("statusProyector")
    private String statusProyector;

    public AulaExternaDTO() {
    }

    public AulaExternaDTO(String clave, String nombre, Integer capacidad, String tipo, String statusProyector) {
        this.clave = clave;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.tipo = tipo;
        this.statusProyector = statusProyector;
    }

    // Constructor simplificado para compatibilidad
    public AulaExternaDTO(Integer id, String nombre, Integer capacidad) {
        this.clave = String.valueOf(id);
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.tipo = "AULA";
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    
    // Para compatibilidad con código existente
    public Integer getIdAula() {
        try {
            return Integer.parseInt(clave);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void setIdAula(Integer idAula) {
        this.clave = String.valueOf(idAula);
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatusProyector() {
        return statusProyector;
    }

    public void setStatusProyector(String statusProyector) {
        this.statusProyector = statusProyector;
    }

    @Override
    public String toString() {
        return "AulaExternaDTO{" +
                "clave='" + clave + '\'' +
                ", nombre='" + nombre + '\'' +
                ", capacidad=" + capacidad +
                ", tipo='" + tipo + '\'' +
                ", statusProyector='" + statusProyector + '\'' +
                '}';
    }
}
