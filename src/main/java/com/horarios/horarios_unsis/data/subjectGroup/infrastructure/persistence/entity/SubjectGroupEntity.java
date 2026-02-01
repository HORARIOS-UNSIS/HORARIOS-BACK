package com.horarios.horarios_unsis.data.subjectGroup.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "materia_grupo")
public class SubjectGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia_grupo")
    private Integer idMateriaGrupo;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "clave_carrera", length = 10)
    private String claveCarrera;

    @Column(name = "clave_grupo", nullable = false, length = 20)
    private String claveGrupo;

    @Column(name = "clave_materia", nullable = false, length = 50)
    private String claveMateria;

    @Column(name = "clave_periodo", nullable = false, length = 20)
    private String clavePeriodo;

    @Column(name = "fecha_sincronizacion")
    private LocalDateTime fechaSincronizacion;

    @Column(name = "horas_semana")
    private Integer horasSemana;

    @Column(name = "id_profesor")
    private Integer idProfesor;

    @Column(name = "nombre_materia", length = 200)
    private String nombreMateria;

    @Column(name = "nombre_profesor", length = 200)
    private String nombreProfesor;

    public SubjectGroupEntity() {
    }

    public Integer getIdMateriaGrupo() {
        return idMateriaGrupo;
    }

    public void setIdMateriaGrupo(Integer idMateriaGrupo) {
        this.idMateriaGrupo = idMateriaGrupo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getClaveCarrera() {
        return claveCarrera;
    }

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public String getClaveGrupo() {
        return claveGrupo;
    }

    public void setClaveGrupo(String claveGrupo) {
        this.claveGrupo = claveGrupo;
    }

    public String getClaveMateria() {
        return claveMateria;
    }

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }

    public String getClavePeriodo() {
        return clavePeriodo;
    }

    public void setClavePeriodo(String clavePeriodo) {
        this.clavePeriodo = clavePeriodo;
    }

    public LocalDateTime getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(LocalDateTime fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }

    public Integer getHorasSemana() {
        return horasSemana;
    }

    public void setHorasSemana(Integer horasSemana) {
        this.horasSemana = horasSemana;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
}
