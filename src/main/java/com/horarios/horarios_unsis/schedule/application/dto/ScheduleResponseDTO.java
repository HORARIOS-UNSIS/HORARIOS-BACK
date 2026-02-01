package com.horarios.horarios_unsis.schedule.application.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO para responder con la información de un examen (Schedule)
 */
public class ScheduleResponseDTO {

    private Integer idExamen;
    private Integer idMateria;
    private Integer idAula;
    private Integer idHorario;
    private Integer idTipo;
    private Integer idPeriodo;
    private Integer profesorId;
    private LocalDate fecha;
    private String grupo;
    private String status;
    private String claveMateria;
    private String nombreAula;
    private String nombreMateria;
    private String nombreProfesor;
    private Boolean enHorarioOficial;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean isLocked;

    public ScheduleResponseDTO() {
    }

    public ScheduleResponseDTO(Integer idExamen, Integer idMateria, Integer idAula, 
                              Integer idHorario, Integer idTipo, Integer idPeriodo, 
                              Integer profesorId, LocalDate fecha, String grupo, String status) {
        this.idExamen = idExamen;
        this.idMateria = idMateria;
        this.idAula = idAula;
        this.idHorario = idHorario;
        this.idTipo = idTipo;
        this.idPeriodo = idPeriodo;
        this.profesorId = profesorId;
        this.fecha = fecha;
        this.grupo = grupo;
        this.status = status;
    }

    public ScheduleResponseDTO(Integer idExamen, Integer idMateria, Integer idAula, 
                              Integer idHorario, Integer idTipo, Integer idPeriodo, 
                              Integer profesorId, LocalDate fecha, String grupo, String status,
                              String claveMateria, String nombreAula, String nombreMateria, 
                              String nombreProfesor, Boolean enHorarioOficial, LocalTime horaInicio, 
                              LocalTime horaFin, Boolean isLocked) {
        this.idExamen = idExamen;
        this.idMateria = idMateria;
        this.idAula = idAula;
        this.idHorario = idHorario;
        this.idTipo = idTipo;
        this.idPeriodo = idPeriodo;
        this.profesorId = profesorId;
        this.fecha = fecha;
        this.grupo = grupo;
        this.status = status;
        this.claveMateria = claveMateria;
        this.nombreAula = nombreAula;
        this.nombreMateria = nombreMateria;
        this.nombreProfesor = nombreProfesor;
        this.enHorarioOficial = enHorarioOficial;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.isLocked = isLocked;
    }

    public Integer getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(Integer idExamen) {
        this.idExamen = idExamen;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public Integer getIdAula() {
        return idAula;
    }

    public void setIdAula(Integer idAula) {
        this.idAula = idAula;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Integer getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(Integer profesorId) {
        this.profesorId = profesorId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClaveMateria() {
        return claveMateria;
    }

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }

    public String getNombreAula() {
        return nombreAula;
    }

    public void setNombreAula(String nombreAula) {
        this.nombreAula = nombreAula;
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

    public Boolean getEnHorarioOficial() {
        return enHorarioOficial;
    }

    public void setEnHorarioOficial(Boolean enHorarioOficial) {
        this.enHorarioOficial = enHorarioOficial;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }
}
