package com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "materia")
public class SubjectEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia")
    private Integer idMateria;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "es_academia", nullable = false)
    private Boolean esAcademia = false;  // true si es materia de academia, false si es regular

    public SubjectEntity() {
    }

    public SubjectEntity(Integer idMateria, String nombre) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.esAcademia = false;  // Por defecto, no es academia
    }

    public SubjectEntity(Integer idMateria, String nombre, Boolean esAcademia) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.esAcademia = esAcademia != null ? esAcademia : false;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEsAcademia() {
        return esAcademia;
    }

    public void setEsAcademia(Boolean esAcademia) {
        this.esAcademia = esAcademia != null ? esAcademia : false;
    }
}