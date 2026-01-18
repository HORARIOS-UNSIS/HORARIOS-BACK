package com.horarios.horarios_unsis.data.schoolHours.domain.model;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Modelo de dominio para horarios escolares
 */
public class SchoollHors {
    
    private Long id;
    private Integer periodNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isBreak;
    private String description;

    public SchoollHors() {
    }

    public SchoollHors(Long id, Integer periodNumber, LocalTime startTime, LocalTime endTime, Boolean isBreak, String description) {
        this.id = id;
        this.periodNumber = Objects.requireNonNull(periodNumber, "periodNumber must not be null");
        this.startTime = Objects.requireNonNull(startTime, "startTime must not be null");
        this.endTime = Objects.requireNonNull(endTime, "endTime must not be null");
        this.isBreak = isBreak != null ? isBreak : false;
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

    @Override
    public String toString() {
        return "SchoollHors{" +
                "id=" + id +
                ", periodNumber=" + periodNumber +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isBreak=" + isBreak +
                ", description='" + description + '\'' +
                '}';
    }
}
