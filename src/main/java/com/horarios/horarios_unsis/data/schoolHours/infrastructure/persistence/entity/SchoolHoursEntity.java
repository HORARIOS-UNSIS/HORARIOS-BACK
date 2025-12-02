package com.horarios.horarios_unsis.data.schoolHours.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Table(name = "school_hours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolHoursEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "period_number", nullable = false)
    private Integer periodNumber;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "is_break", nullable = false)
    private Boolean isBreak = false;
    
    @Column(name = "description", length = 100)
    private String description;
}
