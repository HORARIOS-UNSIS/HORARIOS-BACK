package com.horarios.horarios_unsis.data.materiagrupo.infrastructure.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa la relación entre una materia y un grupo en un período específico.
 * Esta información se sincroniza desde el endpoint de horarios de la API externa.
 * 
 * Permite saber qué materias se imparten en cada grupo.
 */
@Entity
@Table(name = "materia_grupo", 
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"clave_materia", "clave_grupo", "clave_periodo"}
       ))
public class MateriaGrupoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia_grupo")
    private Integer idMateriaGrupo;

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

    @Column(name = "id_profesor")
    private Integer idProfesor;

    @Column(name = "nombre_profesor", length = 200)
    private String nombreProfesor;

    @Column(name = "horas_semana")
    private Integer horasSemana;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_sincronizacion")
    private java.time.LocalDateTime fechaSincronizacion;

    public MateriaGrupoEntity() {
    }

    public MateriaGrupoEntity(String claveMateria, String nombreMateria, String claveGrupo, 
                               String claveCarrera, String clavePeriodo) {
        this.claveMateria = claveMateria;
        this.nombreMateria = nombreMateria;
        this.claveGrupo = claveGrupo;
        this.claveCarrera = claveCarrera;
        this.clavePeriodo = clavePeriodo;
        this.activo = true;
        this.fechaSincronizacion = java.time.LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdMateriaGrupo() {
        return idMateriaGrupo;
    }

    public void setIdMateriaGrupo(Integer idMateriaGrupo) {
        this.idMateriaGrupo = idMateriaGrupo;
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

    public Integer getHorasSemana() {
        return horasSemana;
    }

    public void setHorasSemana(Integer horasSemana) {
        this.horasSemana = horasSemana;
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
        return "MateriaGrupo{" +
                "materia=" + claveMateria + " (" + nombreMateria + ")" +
                ", grupo=" + claveGrupo +
                ", profesor=" + nombreProfesor +
                ", periodo=" + clavePeriodo +
                '}';
    }
}
