package com.horarios.horarios_unsis.schedule.domain.service;

import com.horarios.horarios_unsis.schedule.application.mapper.ScheduleMapper;
import com.horarios.horarios_unsis.schedule.domain.model.Schedule;
import com.horarios.horarios_unsis.schedule.domain.port.in.ScheduleServicePort;
import com.horarios.horarios_unsis.schedule.infrastructure.persistence.entity.ScheduleEntity;
import com.horarios.horarios_unsis.schedule.infrastructure.persistence.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService implements ScheduleServicePort {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {
        ScheduleEntity entity = ScheduleMapper.toEntity(schedule);
        ScheduleEntity savedEntity = scheduleRepository.save(entity);
        return ScheduleMapper.toModel(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Schedule> getScheduleById(Integer id) {
        return scheduleRepository.findById(id)
                .map(ScheduleMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(ScheduleMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Schedule updateSchedule(Integer id, Schedule schedule) {
        if (!scheduleRepository.existsById(id)) {
            throw new IllegalArgumentException("Schedule with id " + id + " not found");
        }
        ScheduleEntity entity = ScheduleMapper.toEntity(schedule);
        entity.setIdExamen(id);
        ScheduleEntity updatedEntity = scheduleRepository.save(entity);
        return ScheduleMapper.toModel(updatedEntity);
    }

    @Override
    public void deleteSchedule(Integer id) {
        if (!scheduleRepository.existsById(id)) {
            throw new IllegalArgumentException("Schedule with id " + id + " not found");
        }
        scheduleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByMateria(Integer idMateria) {
        return scheduleRepository.findByIdMateria(idMateria)
                .stream()
                .map(ScheduleMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByProfesor(Integer profesorId) {
        return scheduleRepository.findByProfesorId(profesorId)
                .stream()
                .map(ScheduleMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByFecha(LocalDate fecha) {
        return scheduleRepository.findByFecha(fecha)
                .stream()
                .map(ScheduleMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByPeriodo(Integer idPeriodo) {
        return scheduleRepository.findByIdPeriodo(idPeriodo)
                .stream()
                .map(ScheduleMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByStatus(String status) {
        return scheduleRepository.findByStatus(status)
                .stream()
                .map(ScheduleMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByFechaRange(LocalDate fechaInicio, LocalDate fechaFin) {
        return scheduleRepository.findByFechaBetween(fechaInicio, fechaFin)
                .stream()
                .map(ScheduleMapper::toModel)
                .collect(Collectors.toList());
    }
}

