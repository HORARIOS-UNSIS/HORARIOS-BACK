package com.horarios.horarios_unsis.data.carrera.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

/**
 * Entidad para almacenar carreras
 * 
 * Estructura de la API externa:
 * {
 *   "clave": "01B",
 *   "nombre": "LICENCIATURA EN ADMINISTRACIÓN MUNICIPAL 2015",
 *   "vigente": true
 * }
 */
@Entity
@Table(name = "carreras")
public class CarreraEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrera")
    private Integer idCarrera;
    
    @Column(name = "clave", length = 50, unique = true, nullable = false)
    private String clave;
    
    @Column(name = "nombre", length = 300, nullable = false)
    private String nombre;
    
    @Column(name = "vigente")
    private Boolean vigente = true;

    public CarreraEntity() {
    }

    public CarreraEntity(String clave, String nombre, Boolean vigente) {
        this.clave = clave;
        this.nombre = nombre;
        this.vigente = vigente;
    }

    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
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
}
