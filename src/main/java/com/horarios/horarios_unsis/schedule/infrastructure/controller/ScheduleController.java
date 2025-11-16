package com.horarios.horarios_unsis.schedule.infrastructure.controller;

import com.horarios.horarios_unsis.schedule.application.dto.request.ScheduleRequestDTO;
import com.horarios.horarios_unsis.schedule.application.dto.response.ScheduleResponseDTO;
import com.horarios.horarios_unsis.schedule.domain.model.Schedule;
import com.horarios.horarios_unsis.schedule.domain.port.in.ScheduleServicePort;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {
    
    private final ScheduleServicePort scheduleService;

    public ScheduleController(ScheduleServicePort scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@Valid @RequestBody ScheduleRequestDTO requestDTO) {
        Schedule schedule = toModel(requestDTO);
        Schedule createdSchedule = scheduleService.createSchedule(schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(createdSchedule));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> getScheduleById(@PathVariable Integer id) {
        return scheduleService.getScheduleById(id)
                .map(schedule -> ResponseEntity.ok(toResponseDTO(schedule)))
                .orElse(ResponseEntity.notFound().build());
    }

    //Obtener todos
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDTO>> getAllSchedules() {
        List<ScheduleResponseDTO> schedules = scheduleService.getAllSchedules()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }

    //Agregar horario
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(
            @PathVariable Integer id,
            @Valid @RequestBody ScheduleRequestDTO requestDTO) {
        try {
            Schedule schedule = toModel(requestDTO);
            Schedule updatedSchedule = scheduleService.updateSchedule(id, schedule);
            return ResponseEntity.ok(toResponseDTO(updatedSchedule));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Eliminar uno en específico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Integer id) {
        try {
            scheduleService.deleteSchedule(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Por materia
    @GetMapping("/materia/{idMateria}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByMateria(@PathVariable Integer idMateria) {
        List<ScheduleResponseDTO> schedules = scheduleService.getSchedulesByMateria(idMateria)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }

    //Examenes por profesor 
    @GetMapping("/profesor/{profesorId}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByProfesor(@PathVariable Integer profesorId) {
        List<ScheduleResponseDTO> schedules = scheduleService.getSchedulesByProfesor(profesorId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }

    //Obtener exámenes por periodo
    @GetMapping("/periodo/{idPeriodo}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByPeriodo(@PathVariable Integer idPeriodo) {
        List<ScheduleResponseDTO> schedules = scheduleService.getSchedulesByPeriodo(idPeriodo)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }

    //Exámenes por Status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByStatus(@PathVariable String status) {
        List<ScheduleResponseDTO> schedules = scheduleService.getSchedulesByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }

    //Obtener por rango de fechas
    @GetMapping("/rango")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByFechaRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<ScheduleResponseDTO> schedules = scheduleService.getSchedulesByFechaRange(inicio, fin)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schedules);
    }

    // Métodos auxiliares para conversión entre DTOs y modelo de dominio
    private Schedule toModel(ScheduleRequestDTO dto) {
        return new Schedule(
                null, // id se genera automáticamente
                dto.getIdMateria(),
                dto.getIdAula(),
                dto.getIdHorario(),
                dto.getIdTipo(),
                dto.getIdPeriodo(),
                dto.getProfesorId(),
                dto.getFecha(),
                dto.getGrupo(),
                dto.getStatus()
        );
    }

    private ScheduleResponseDTO toResponseDTO(Schedule schedule) {
        return new ScheduleResponseDTO(
                schedule.getIdExamen(),
                schedule.getIdMateria(),
                schedule.getIdAula(),
                schedule.getIdHorario(),
                schedule.getIdTipo(),
                schedule.getIdPeriodo(),
                schedule.getProfesorId(),
                schedule.getFecha(),
                schedule.getGrupo(),
                schedule.getStatus()
        );
    }
}

