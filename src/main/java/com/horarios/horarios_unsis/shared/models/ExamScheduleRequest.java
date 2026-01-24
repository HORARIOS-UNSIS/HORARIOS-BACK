package com.horarios.horarios_unsis.shared.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Modelo de solicitud para creación de horarios de examen
 * Datos que vienen del FRONTEND con asignaciones realizadas por:
 * - Jefe de carrera
 * - Servicios escolares
 * - Secretaria
 * - Administrador
 */
public class ExamScheduleRequest {
    
    private Integer idMateria;
    private Integer idGrupo;
    private Integer idProfesor;  // Profesor titular de la materia
    private Integer idProfesorAplicador;  // Quien aplica el examen
    private List<Integer> idsProfesorSinodales;  // Lista de IDs de sinodales (DEPRECATED - usar sinodalesRequest)
    private List<SinodalRequest> sinodalesRequest;  // Lista flexible: puede enviar nombre o ID
    private LocalDate fechaExamen;
    private LocalTime horaExamen;
    private Integer idAula;
    private Integer duracionMinutos;  // Por defecto 120 (2 horas)
    private String tipoExamen;  // PARCIAL, ORDINARIO, EXTRAORDINARIO, ESPECIAL
    private String area;  // SALUD, INGENIERIA, DERECHO, etc
    private Integer periodoAcademico;
    private Boolean esAcademia;  // Si es examen de academia
    private Integer idAcademia;  // ID de la academia si aplica
    private String observaciones;  // Notas adicionales

    public ExamScheduleRequest() {
        this.duracionMinutos = 120;  // 2 horas por defecto
        this.esAcademia = false;
    }

    // Getters y Setters
    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public Integer getIdProfesorAplicador() {
        return idProfesorAplicador;
    }

    public void setIdProfesorAplicador(Integer idProfesorAplicador) {
        this.idProfesorAplicador = idProfesorAplicador;
    }

    public List<Integer> getIdsProfesorSinodales() {
        return idsProfesorSinodales;
    }

    public void setIdsProfesorSinodales(List<Integer> idsProfesorSinodales) {
        this.idsProfesorSinodales = idsProfesorSinodales;
    }

    public List<SinodalRequest> getSinodalesRequest() {
        return sinodalesRequest;
    }

    public void setSinodalesRequest(List<SinodalRequest> sinodalesRequest) {
        this.sinodalesRequest = sinodalesRequest;
    }

    public LocalDate getFechaExamen() {
        return fechaExamen;
    }

    public void setFechaExamen(LocalDate fechaExamen) {
        this.fechaExamen = fechaExamen;
    }

    public LocalTime getHoraExamen() {
        return horaExamen;
    }

    public void setHoraExamen(LocalTime horaExamen) {
        this.horaExamen = horaExamen;
    }

    public Integer getIdAula() {
        return idAula;
    }

    public void setIdAula(Integer idAula) {
        this.idAula = idAula;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getTipoExamen() {
        return tipoExamen;
    }

    public void setTipoExamen(String tipoExamen) {
        this.tipoExamen = tipoExamen;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getPeriodoAcademico() {
        return periodoAcademico;
    }

    public void setPeriodoAcademico(Integer periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public Boolean getEsAcademia() {
        return esAcademia;
    }

    public void setEsAcademia(Boolean esAcademia) {
        this.esAcademia = esAcademia;
    }

    public Integer getIdAcademia() {
        return idAcademia;
    }

    public void setIdAcademia(Integer idAcademia) {
        this.idAcademia = idAcademia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
