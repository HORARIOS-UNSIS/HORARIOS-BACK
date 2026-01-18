package com.horarios.horarios_unsis.integration.Consume.DTO;

import java.time.LocalTime;

/**
 * DTO para datos de horarios escolares desde API externa
 */
public class SchoolHoursExternoDTO {
    
    private Integer periodNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isBreak;
    private String description;

    public SchoolHoursExternoDTO() {
    }

    public SchoolHoursExternoDTO(Integer periodNumber, LocalTime startTime, LocalTime endTime, Boolean isBreak, String description) {
        this.periodNumber = periodNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isBreak = isBreak;
        this.description = description;
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
