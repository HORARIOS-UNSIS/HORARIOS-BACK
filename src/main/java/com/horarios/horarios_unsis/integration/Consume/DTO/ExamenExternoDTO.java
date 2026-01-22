package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class ExamenExternoDTO {
    
    @JsonProperty("idExamen")
    private Integer idExamen;
    
    @JsonProperty("nombreExamen")
    private String nombreExamen;
    
    @JsonProperty("descripcion")
    private String descripcion;
    
    @JsonProperty("periodo")
    private String periodo;
    
    @JsonProperty("idMateria")
    private Integer idMateria;
    
    @JsonProperty("idAula")
    private Integer idAula;
    
    @JsonProperty("idHorario")
    private Integer idHorario;
    
    @JsonProperty("idTipo")
    private Integer idTipo;
    
    @JsonProperty("idPeriodo")
    private Integer idPeriodo;
    
    @JsonProperty("idProfesor")
    private Integer profesorId;
    
    @JsonProperty("fecha")
    private LocalDate fecha;
    
    @JsonProperty("grupo")
    private String grupo;
    
    @JsonProperty("status")
    private String status;

    public ExamenExternoDTO() {
    }

    public ExamenExternoDTO(Integer idExamen, String nombreExamen, String descripcion, String periodo) {
        this.idExamen = idExamen;
        this.nombreExamen = nombreExamen;
        this.descripcion = descripcion;
        this.periodo = periodo;
    }

    public Integer getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(Integer idExamen) {
        this.idExamen = idExamen;
    }

    public String getNombreExamen() {
        return nombreExamen;
    }

    public void setNombreExamen(String nombreExamen) {
        this.nombreExamen = nombreExamen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

