package com.horarios.horarios_unsis.data.group.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "grupos")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Integer idGrupo;

    @Column(name = "alumnos")
    private Integer alumnos;

    @Column(name = "clave", nullable = false, length = 50)
    private String clave;

    @Column(name = "clave_carrera", length = 50)
    private String claveCarrera;

    @Column(name = "clave_periodo", length = 50)
    private String clavePeriodo;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "semestre")
    private Integer semestre;

    public GroupEntity() {
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Integer alumnos) {
        this.alumnos = alumnos;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClaveCarrera() {
        return claveCarrera;
    }

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public String getClavePeriodo() {
        return clavePeriodo;
    }

    public void setClavePeriodo(String clavePeriodo) {
        this.clavePeriodo = clavePeriodo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
}
