package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchoolHoursExternoDTO {
    
    @JsonProperty("idHorario")
    private Integer idHorario;
    
    @JsonProperty("numeroBloque")
    private Integer numeroBloque;
    
    @JsonProperty("horaInicio")
    private String horaInicio;
    
    @JsonProperty("horaFin")
    private String horaFin;
    
    @JsonProperty("esDescanso")
    private Boolean isBreak;
    
    @JsonProperty("descripcion")
    private String description;

    public SchoolHoursExternoDTO() {
    }

    public SchoolHoursExternoDTO(Integer idHorario, Integer numeroBloque, String horaInicio, String horaFin) {
        this.idHorario = idHorario;
        this.numeroBloque = numeroBloque;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getNumeroBloque() {
        return numeroBloque;
    }

    public void setNumeroBloque(Integer numeroBloque) {
        this.numeroBloque = numeroBloque;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getIsBreak() {
        return isBreak;
    }

    public void setIsBreak(Boolean isBreak) {
        this.isBreak = isBreak;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

