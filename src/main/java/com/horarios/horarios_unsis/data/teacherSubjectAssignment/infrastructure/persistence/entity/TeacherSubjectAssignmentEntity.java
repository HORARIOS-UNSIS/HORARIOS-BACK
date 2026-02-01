package com.horarios.horarios_unsis.data.teacherSubjectAssignment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asignacion_profesor_materia")
public class TeacherSubjectAssignmentEntity {

    @Id
    @Column(name = "id_asignacion")
    private Integer idAsignacion;

    @Column(name = "clave_aula")
    private String claveAula;

    @Column(name = "clave_carrera")
    private String claveCarrera;

    @Column(name = "clave_grupo", nullable = false)
    private String claveGrupo;

    @Column(name = "clave_materia", nullable = false)
    private String claveMateria;

    @Column(name = "clave_periodo", nullable = false)
    private String clavePeriodo;

    @Column(name = "dia")
    private Integer dia;

    @Column(name = "hora")
    private Integer hora;

    @Column(name = "id_profesor", nullable = false)
    private Integer idProfesor;

    @Column(name = "nombre_materia")
    private String nombreMateria;

    @Column(name = "nombre_profesor")
    private String nombreProfesor;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_sincronizacion")
    private LocalDateTime fechaSincronizacion;

    public TeacherSubjectAssignmentEntity() {
    }

    public Integer getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(Integer idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public String getClaveAula() {
        return claveAula;
    }

    public void setClaveAula(String claveAula) {
        this.claveAula = claveAula;
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

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(LocalDateTime fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }
}
