package com.horarios.horarios_unsis.schedule.domain.service;

import com.horarios.horarios_unsis.integration.Consume.HorarioConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.PeriodoConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.DTO.HorarioExternoDTO;
import com.horarios.horarios_unsis.integration.Consume.DTO.PeriodoExternoDTO;
import com.horarios.horarios_unsis.schedule.application.mapper.ScheduleMapper;
import com.horarios.horarios_unsis.schedule.domain.model.Schedule;
import com.horarios.horarios_unsis.schedule.domain.port.in.ScheduleServicePort;
import com.horarios.horarios_unsis.schedule.infrastructure.persistence.entity.ScheduleEntity;
import com.horarios.horarios_unsis.schedule.infrastructure.persistence.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService implements ScheduleServicePort {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    private final ScheduleRepository scheduleRepository;
    private final HorarioConsumeClient horarioConsumeClient;
    private final PeriodoConsumeClient periodoConsumeClient;

    public ScheduleService(
            ScheduleRepository scheduleRepository,
            HorarioConsumeClient horarioConsumeClient,
            PeriodoConsumeClient periodoConsumeClient) {
        this.scheduleRepository = scheduleRepository;
        this.horarioConsumeClient = horarioConsumeClient;
        this.periodoConsumeClient = periodoConsumeClient;
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

    /**
     * Importa horarios desde API externa y los almacena en BD local
     * 
     * Flujo:
     * 1. Obtiene período actual del API
     * 2. Consume todos los horarios del API para ese período
     * 3. Mapea HorarioExternoDTO → Schedule
     * 4. Persiste en BD local
     * 
     * @return Lista de horarios creados en BD
     */
    public List<Schedule> importarHorariosDelAPI() {
        logger.info("=== INICIANDO IMPORTACIÓN DE HORARIOS DESDE API ===");
        
        try {
            // 1. Obtener período actual
            PeriodoExternoDTO periodoActual = periodoConsumeClient.obtenerPeriodoActual();
            logger.info("Período actual: ID={}, Número={}", periodoActual.getIdPeriodo(), periodoActual.getNumero());
            
            // 2. Obtener todos los horarios del período desde API
            List<HorarioExternoDTO> horariosDelAPI = horarioConsumeClient
                .obtenerTodosHorariosPorPeriodo(periodoActual.getIdPeriodo());
            logger.info("Se obtuvieron {} horarios del API", horariosDelAPI.size());
            
            // 3. Mapear y persistir cada horario
            List<Schedule> schedulesCreados = horariosDelAPI.stream()
                    .map(horario -> mapearYPersistirHorario(horario, periodoActual.getIdPeriodo()))
                    .collect(Collectors.toList());
            
            logger.info("✓ Importación completada: {} horarios almacenados en BD", schedulesCreados.size());
            return schedulesCreados;
            
        } catch (Exception e) {
            logger.error("✗ Error durante la importación de horarios: {}", e.getMessage(), e);
            throw new RuntimeException("Error en importación de horarios: " + e.getMessage(), e);
        }
    }

    /**
     * Mapea un HorarioExternoDTO a Schedule y lo persiste
     */
    private Schedule mapearYPersistirHorario(HorarioExternoDTO horarioDTO, Integer idPeriodo) {
        try {
            Schedule schedule = new Schedule(
                null,  // idExamen será generado por la BD
                horarioDTO.getIdMateria(),
                horarioDTO.getIdAula(),
                horarioDTO.getIdBloque() != null ? horarioDTO.getIdBloque() : horarioDTO.getNumeroBloque(),
                null,  // idTipo - será seteado después si es necesario
                idPeriodo,
                horarioDTO.getIdProfesor(),
                horarioDTO.getFecha(),
                horarioDTO.getGrupo(),
                "PROGRAMADO"
            );
            
            return createSchedule(schedule);
            
        } catch (Exception e) {
            logger.error("Error mapeando horario: {}", e.getMessage(), e);
            throw new RuntimeException("Error en mapeo de horario: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene horarios de un profesor desde el API (para consulta)
     */
    public List<HorarioExternoDTO> obtenerHorariosProfesorDelAPI(Integer idPeriodo, Integer idProfesor) {
        logger.info("Consultando horarios del profesor {} en período {}", idProfesor, idPeriodo);
        try {
            return horarioConsumeClient.obtenerHorariosPorProfesor(idPeriodo, idProfesor);
        } catch (Exception e) {
            logger.error("Error obteniendo horarios del profesor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener horarios del profesor: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene horarios de un grupo desde el API (para consulta)
     */
    public List<HorarioExternoDTO> obtenerHorariosGrupoDelAPI(Integer idPeriodo, Integer idGrupo) {
        logger.info("Consultando horarios del grupo {} en período {}", idGrupo, idPeriodo);
        try {
            return horarioConsumeClient.obtenerHorariosPorGrupo(idPeriodo, idGrupo);
        } catch (Exception e) {
            logger.error("Error obteniendo horarios del grupo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener horarios del grupo: " + e.getMessage(), e);
        }
    }
}

