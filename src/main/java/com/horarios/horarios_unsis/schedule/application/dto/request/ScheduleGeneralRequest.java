package com.horarios.horarios_unsis.schedule.application.dto.request;

import java.time.LocalDate;
import java.util.List;

public class ScheduleGeneralRequest {
    private String licenciatura;
    private List<String> grupos;
    private String periodo;
    private String Tipo;
    private LocalDate start;
    private LocalDate end;
    
    public LocalDate getStart() {
        return start;
    }
    public void setStart(LocalDate start) {
        this.start = start;
    }
    public LocalDate getEnd() {
        return end;
    }
    public void setEnd(LocalDate end) {
        this.end = end;
    }
    public String getLicenciatura() {
        return licenciatura;
    }
    public void setLicenciatura(String licenciatura) {
        this.licenciatura = licenciatura;
    }
    public List<String> getGrupos() {
        return grupos;
    }
    public void setGrupos(List<String> grupos) {
        this.grupos = grupos;
    }
    public String getPeriodo() {
        return periodo;
    }
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
    public String getTipo() {
        return Tipo;
    }
    public void setTipo(String tipo) {
        Tipo = tipo;
    }



}
