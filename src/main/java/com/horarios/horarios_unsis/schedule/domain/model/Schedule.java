package com.horarios.horarios_unsis.schedule.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Modelo de dominio para Schedule (Examen). Independiente de la capa de persistencia.
 */
public final class Schedule {

    private final Integer idExamen;
    private final Integer idMateria;
    private final Integer idAula;
    private final Integer idHorario;
    private final Integer idTipo;
    private final Integer idPeriodo;
    private final Integer profesorId;
    private final LocalDate fecha;
    private final String grupo;
    private final String status;
    private final String claveMateria;
    private final String nombreAula;
    private final String nombreMateria;
    private final String nombreProfesor;
    private final Boolean enHorarioOficial;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private final Boolean isLocked;

    public Schedule(Integer idExamen, Integer idMateria, Integer idAula, Integer idHorario,
                    Integer idTipo, Integer idPeriodo, Integer profesorId, LocalDate fecha,
                    String grupo, String status) {
        this(idExamen, idMateria, idAula, idHorario, idTipo, idPeriodo, profesorId, fecha, grupo, status, null, null, null, null, false, null, null, false);
    }

    public Schedule(Integer idExamen, Integer idMateria, Integer idAula, Integer idHorario,
                    Integer idTipo, Integer idPeriodo, Integer profesorId, LocalDate fecha,
                    String grupo, String status, String claveMateria, String nombreAula,
                    String nombreMateria, String nombreProfesor, Boolean enHorarioOficial,
                    LocalTime horaInicio, LocalTime horaFin, Boolean isLocked) {
        this.idExamen = idExamen;
        this.idMateria = Objects.requireNonNull(idMateria, "idMateria must not be null");
        this.idAula = Objects.requireNonNull(idAula, "idAula must not be null");
        this.idHorario = Objects.requireNonNull(idHorario, "idHorario must not be null");
        this.idTipo = Objects.requireNonNull(idTipo, "idTipo must not be null");
        this.idPeriodo = Objects.requireNonNull(idPeriodo, "idPeriodo must not be null");
        this.profesorId = Objects.requireNonNull(profesorId, "profesorId must not be null");
        this.fecha = Objects.requireNonNull(fecha, "fecha must not be null");
        this.grupo = grupo;
        this.status = status;
        this.claveMateria = claveMateria;
        this.nombreAula = nombreAula;
        this.nombreMateria = nombreMateria;
        this.nombreProfesor = nombreProfesor;
        this.enHorarioOficial = enHorarioOficial != null ? enHorarioOficial : false;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.isLocked = isLocked != null ? isLocked : false;
    }


    public Integer getIdExamen() {
        return idExamen;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public Integer getIdAula() {
        return idAula;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public Integer getProfesorId() {
        return profesorId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getStatus() {
        return status;
    }

    public String getClaveMateria() {
        return claveMateria;
    }

    public String getNombreAula() {
        return nombreAula;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public Boolean getEnHorarioOficial() {
        return enHorarioOficial;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "idExamen=" + idExamen +
                ", idMateria=" + idMateria +
                ", idAula=" + idAula +
                ", idHorario=" + idHorario +
                ", idTipo=" + idTipo +
                ", idPeriodo=" + idPeriodo +
                ", profesorId=" + profesorId +
                ", fecha=" + fecha +
                ", grupo='" + grupo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
