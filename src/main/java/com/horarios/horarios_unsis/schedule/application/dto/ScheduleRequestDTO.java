package com.horarios.horarios_unsis.schedule.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * DTO para crear o actualizar un examen (Schedule)
 */
public class ScheduleRequestDTO {

    @NotNull(message = "El ID de materia es obligatorio")
    private Integer idMateria;
    
    @NotNull(message = "El ID de aula es obligatorio")
    private Integer idAula;
    
    @NotNull(message = "El ID de horario es obligatorio")
    private Integer idHorario;
    
    @NotNull(message = "El ID de tipo es obligatorio")
    private Integer idTipo;
    
    @NotNull(message = "El ID de periodo es obligatorio")
    private Integer idPeriodo;
    
    @NotNull(message = "El ID del profesor es obligatorio")
    private Integer profesorId;
    
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    
    @Size(max = 50, message = "El grupo no puede exceder 50 caracteres")
    private String grupo;
    
    @Size(max = 50, message = "El status no puede exceder 50 caracteres")
    private String status;

    public ScheduleRequestDTO() {
    }

    public ScheduleRequestDTO(Integer idMateria, Integer idAula, Integer idHorario, 
                             Integer idTipo, Integer idPeriodo, Integer profesorId, 
                             LocalDate fecha, String grupo, String status) {
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
