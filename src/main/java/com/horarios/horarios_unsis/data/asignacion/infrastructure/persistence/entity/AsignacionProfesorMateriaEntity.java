package com.horarios.horarios_unsis.data.asignacion.infrastructure.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa la asignación de un profesor a una materia en un grupo específico.
 * Esta información se sincroniza desde el endpoint de horarios de la API externa.
 * 
 * Relación: Profesor -> Materia -> Grupo -> Período
 */
@Entity
@Table(name = "asignacion_profesor_materia", 
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"id_profesor", "clave_materia", "clave_grupo", "clave_periodo"}
       ))
public class AsignacionProfesorMateriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Integer idAsignacion;

    @Column(name = "id_profesor", nullable = false)
    private Integer idProfesor;

    @Column(name = "nombre_profesor", length = 200)
    private String nombreProfesor;

    @Column(name = "clave_materia", length = 50, nullable = false)
    private String claveMateria;

    @Column(name = "nombre_materia", length = 200)
    private String nombreMateria;

    @Column(name = "clave_grupo", length = 20, nullable = false)
    private String claveGrupo;

    @Column(name = "clave_carrera", length = 10)
    private String claveCarrera;

    @Column(name = "clave_periodo", length = 20, nullable = false)
    private String clavePeriodo;

    @Column(name = "dia")
    private Integer dia;

    @Column(name = "hora")
    private Integer hora;

    @Column(name = "clave_aula", length = 20)
    private String claveAula;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_sincronizacion")
    private java.time.LocalDateTime fechaSincronizacion;

    public AsignacionProfesorMateriaEntity() {
    }

    public AsignacionProfesorMateriaEntity(Integer idProfesor, String nombreProfesor, 
                                           String claveMateria, String nombreMateria,
                                           String claveGrupo, String claveCarrera, 
                                           String clavePeriodo) {
        this.idProfesor = idProfesor;
        this.nombreProfesor = nombreProfesor;
        this.claveMateria = claveMateria;
        this.nombreMateria = nombreMateria;
        this.claveGrupo = claveGrupo;
        this.claveCarrera = claveCarrera;
        this.clavePeriodo = clavePeriodo;
    }

    // Getters y Setters
    public Integer getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(Integer idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public String getClaveMateria() {
        return claveMateria;
    }

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public String getClaveGrupo() {
        return claveGrupo;
    }

    public void setClaveGrupo(String claveGrupo) {
        this.claveGrupo = claveGrupo;
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

    public String getClaveAula() {
        return claveAula;
    }

    public void setClaveAula(String claveAula) {
        this.claveAula = claveAula;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public java.time.LocalDateTime getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(java.time.LocalDateTime fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }

    @Override
    public String toString() {
        return "AsignacionProfesorMateria{" +
                "profesor=" + idProfesor + " (" + nombreProfesor + ")" +
                ", materia=" + claveMateria + " (" + nombreMateria + ")" +
                ", grupo=" + claveGrupo +
                ", periodo=" + clavePeriodo +
                '}';
    }
}
