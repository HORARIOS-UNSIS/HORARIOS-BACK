package com.horarios.horarios_unsis.data.grupo.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

/**
 * Entidad para almacenar grupos
 * 
 * Estructura de la API externa:
 * {
 *   "clave": "107A",
 *   "nombre": "107A",
 *   "carrera": "07",
 *   "semestre": 1,
 *   "alumnos": 30,
 *   "periodo": "1516A"
 * }
 */
@Entity
@Table(name = "grupos")
public class GrupoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Integer idGrupo;
    
    @Column(name = "clave", length = 50, nullable = false)
    private String clave;
    
    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;
    
    @Column(name = "clave_carrera", length = 50)
    private String claveCarrera;
    
    @Column(name = "semestre")
    private Integer semestre;
    
    @Column(name = "alumnos")
    private Integer alumnos;
    
    @Column(name = "clave_periodo", length = 50)
    private String clavePeriodo;

    public GrupoEntity() {
    }

    public GrupoEntity(String clave, String nombre, String claveCarrera, Integer semestre, Integer alumnos, String clavePeriodo) {
        this.clave = clave;
        this.nombre = nombre;
        this.claveCarrera = claveCarrera;
        this.semestre = semestre;
        this.alumnos = alumnos;
        this.clavePeriodo = clavePeriodo;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
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

    public String getClaveCarrera() {
        return claveCarrera;
    }

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Integer getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Integer alumnos) {
        this.alumnos = alumnos;
    }

    public String getClavePeriodo() {
        return clavePeriodo;
    }

    public void setClavePeriodo(String clavePeriodo) {
        this.clavePeriodo = clavePeriodo;
    }
}
