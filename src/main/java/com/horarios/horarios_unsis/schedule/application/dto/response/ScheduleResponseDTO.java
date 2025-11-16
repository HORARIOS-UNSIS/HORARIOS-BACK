package com.horarios.horarios_unsis.schedule.application.dto.response;

import java.time.LocalDate;

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
}
