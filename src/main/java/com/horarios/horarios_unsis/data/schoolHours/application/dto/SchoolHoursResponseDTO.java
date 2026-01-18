package com.horarios.horarios_unsis.data.schoolHours.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@Schema(description = "Datos de respuesta de horarios escolares")
public class SchoolHoursResponseDTO {
    
    @Schema(description = "ID único del horario escolar", example = "1")
    private Long id;
    
    @Schema(description = "Número del período", example = "1")
    private Integer periodNumber;
    
    @Schema(description = "Hora de inicio", example = "08:00")
    private LocalTime startTime;
    
    @Schema(description = "Hora de finalización", example = "09:00")
    private LocalTime endTime;
    
    @Schema(description = "Indica si es receso o descanso", example = "false")
    private Boolean isBreak;
    
    @Schema(description = "Descripción del período", example = "Período 1")
    private String description;

    public SchoolHoursResponseDTO() {
    }

    public SchoolHoursResponseDTO(Long id, Integer periodNumber, LocalTime startTime, LocalTime endTime, Boolean isBreak, String description) {
        this.id = id;
        this.periodNumber = periodNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isBreak = isBreak;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(Integer periodNumber) {
        this.periodNumber = periodNumber;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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
