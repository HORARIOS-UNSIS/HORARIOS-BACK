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
import com.horarios.horarios_unsis.shared.models.ExamScheduleRequest;
import com.horarios.horarios_unsis.shared.ExamConstants;
import com.horarios.horarios_unsis.shared.validators.ExamScheduleValidator;
import com.horarios.horarios_unsis.shared.services.RestriccionesHorariosService;
import com.horarios.horarios_unsis.shared.services.AsignacionSinodalesService;
import com.horarios.horarios_unsis.shared.services.BuscadorDisponibilidadService;
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
    private final ExamScheduleValidator examValidator;
    private final RestriccionesHorariosService restricciones;
    private final AsignacionSinodalesService sinodales;
    private final BuscadorDisponibilidadService buscador;

    public ScheduleService(
            ScheduleRepository scheduleRepository,
            HorarioConsumeClient horarioConsumeClient,
            PeriodoConsumeClient periodoConsumeClient,
            ExamScheduleValidator examValidator,
            RestriccionesHorariosService restricciones,
            AsignacionSinodalesService sinodales,
            BuscadorDisponibilidadService buscador) {
        this.scheduleRepository = scheduleRepository;
        this.horarioConsumeClient = horarioConsumeClient;
        this.periodoConsumeClient = periodoConsumeClient;
        this.examValidator = examValidator;
        this.restricciones = restricciones;
        this.sinodales = sinodales;
        this.buscador = buscador;
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

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByCareerAndPeriod(String claveCarrera, String clavePeriodo) {
        return scheduleRepository.findByCareerAndPeriod(claveCarrera, clavePeriodo)
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
            logger.info("Período actual: Clave={}, Nombre={}", periodoActual.getClave(), periodoActual.getNombre());
            
            // 2. Obtener todos los horarios del período desde API (usando clave)
            // Se usa String.valueOf porque el método espera un String
            List<HorarioExternoDTO> horariosDelAPI = horarioConsumeClient
                .obtenerTodosHorariosPorPeriodo(periodoActual.getClave()); // TODO: Revisar si ID o clave
            logger.info("Se obtuvieron {} horarios del API", horariosDelAPI.size());
            
            // 3. Mapear y persistir cada horario
            List<Schedule> schedulesCreados = horariosDelAPI.stream()
                    .map(horario -> mapearYPersistirHorario(horario, periodoActual.getClave()))
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
    private Schedule mapearYPersistirHorario(HorarioExternoDTO horarioDTO, String clavePeriodo) {
        try {
            // Convertir período CLAVE a ID si es necesario
            // Por ahora asumo que idPeriodo en BD es String o se mapeará diferente. 
            // Como Schedule.idPeriodo es int, necesito una forma de obtener el ID numérico
            // Este es un punto de integración complejo. Usaré un hash o 0 temporalmente
            // Lo ideal es tener un servicio que busque el ID dado la clave
            Integer idPeriodo = 0; 
            
            Schedule schedule = new Schedule(
                null,  // idExamen será generado por la BD
                horarioDTO.getIdMateria(), // Puede dar error si es null
                horarioDTO.getIdAula(), 
                horarioDTO.getIdBloque() != null ? horarioDTO.getIdBloque() : horarioDTO.getNumeroBloque(),
                null,  // idTipo - será seteado después si es necesario
                idPeriodo,
                horarioDTO.getIdProfesor(),
                null, // Fecha no viene en HorarioExternoDTO (es día/hora/semana)
                String.valueOf(horarioDTO.getGrupo()), // Convirtiendo a String si es necesario
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
        // Convertir idPeriodo a String (clave) si es necesario. 
        // Asumiendo por ahora que idPeriodo es la clave numérica o string
        String clavePeriodo = String.valueOf(idPeriodo);
        logger.info("Consultando horarios del profesor {} en período {}", idProfesor, clavePeriodo);
        try {
            return horarioConsumeClient.obtenerHorariosPorProfesor(clavePeriodo, idProfesor);
        } catch (Exception e) {
            logger.error("Error obteniendo horarios del profesor: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener horarios del profesor: " + e.getMessage(), e);
        }
    }

    /**
     * CREACIÓN DE HORARIOS DE EXAMEN
     * 
     * Implementa la lógica compleja de creación considerando:
     * - Tipo de examen (Parcial, Ordinario, Extraordinario, Especial)
     * - Restricciones de inglés
     * - Disponibilidad de profesor aplicador
     * - Asignación de sinodales según tipo
     * - Búsqueda de hora óptima
     * - Preferencia de salas para área de salud
     */

    /**
     * Método principal para crear un horario de examen
     * 
     * Diferencia dos flujos:
     * 1. EXAMEN NO ACADEMIA: Se aplica en hora de clase regular
     *    - Horario: Hora de clase existente
     *    - Profesor Aplicador: Quien imparte la materia
     *    - Sinodales: De la misma carrera
     *    - Duración: 1 hora (parcial) o 2 horas (ordinario)
     * 
     * 2. EXAMEN ACADEMIA: Se busca hora óptima
     *    - Horario: Se busca con impacto mínimo
     *    - Profesor Aplicador: Especialista
     *    - Sinodales: Pertenecen a academia
     *    - Aula: Preferencia "sala" para salud
     *    - Duración: 1 hora (parcial) o 2 horas (ordinario)
     */
    public Schedule crearHorarioExamen(ExamScheduleRequest request) {
        logger.info("=== INICIANDO CREACIÓN DE HORARIO DE EXAMEN ===");
        logger.info("Materia: {}, Grupo: {}, Tipo: {}", 
                   request.getIdMateria(), request.getIdGrupo(), 
                   request.getTipoExamen());
        
        try {
            // PASO 0: Obtener información de la materia desde BD
            // para validar si es academia o no
            // TODO: Consultar materia desde BD usando idMateria
            // Boolean esAcademiaDesdeMateria = materiaService.obtenerMateria(request.getIdMateria()).getEsAcademia();
            
            // Determinar flujo según si es Academia o no
            // Prioridad: Usa valor de la materia desde BD si está disponible, sino usa el del request
            Boolean esAcademia = request.getEsAcademia() != null ? request.getEsAcademia() : false;
            
            if (Boolean.TRUE.equals(esAcademia)) {
                logger.info("→ FLUJO: EXAMEN DE ACADEMIA (buscar hora óptima)");
                return crearHorarioExamenAcademia(request);
            } else {
                logger.info("→ FLUJO: EXAMEN NO ACADEMIA (usar hora de clase regular)");
                return crearHorarioExamenNoAcademia(request);
            }
            
        } catch (Exception e) {
            logger.error("✗ Error creando horario de examen: {}", e.getMessage(), e);
            throw new RuntimeException("Error en creación de horario: " + e.getMessage(), e);
        }
    }

    /**
     * FLUJO: Examen NO Academia
     * Se aplica en la hora de clase regular con el profesor que imparte la materia
     */
    private Schedule crearHorarioExamenNoAcademia(ExamScheduleRequest request) {
        logger.info("--- Procesando EXAMEN NO ACADEMIA ---");
        
        // PASO 1: Validaciones básicas
        validarSolicitud(request);
        
        // PASO 2: El profesor aplicador DEBE SER quien imparte la materia
        // TODO: Consultar BD para obtener profesor titular de la materia
        logger.info("Verificando que profesor aplicador imparte la materia");
        
        // PASO 3: Validar sinodales sean de la misma carrera (ya validado en AsignacionSinodalesService)
        List<Integer> sinodalesAsignados = sinodales.validarYObtenerSinodales(request);
        logger.info("Sinodales de carrera validados: {}", sinodalesAsignados.size());
        
        // PASO 4: Obtener hora y aula de la clase regular
        // TODO: Consultar BD de horarios para obtener hora de clase regular
        logger.info("Obteniendo hora y aula de clase regular para grupo {}", request.getIdGrupo());
        
        // PASO 5: Validar disponibilidad en esa hora
        if (!examValidator.validarDisponibilidadProfesorAplicador(
                request.getIdProfesorAplicador(), request)) {
            throw new IllegalArgumentException(
                "Profesor aplicador no disponible en hora de clase regular");
        }
        
        // PASO 6: Validar exclusión de inglés
        if (!examValidator.validarExclusionIngles(request)) {
            throw new IllegalArgumentException(
                "Hora de clase regular afecta clases de inglés");
        }
        
        // PASO 7: Establecer duración según tipo de examen
        Integer duracion = establecerDuracionExamen(request.getTipoExamen(), request.getArea());
        request.setDuracionMinutos(duracion);
        logger.info("Duración establecida: {} minutos", duracion);
        
        // PASO 8: Validar duración
        if (!examValidator.validarDuracionExamen(request)) {
            throw new IllegalArgumentException("Duración inválida para tipo de examen " + request.getTipoExamen());
        }
        
        // PASO 9: Crear Schedule con hora de clase regular
        Schedule schedule = new Schedule(
            null,  // ID generado por BD
            request.getIdMateria(),
            request.getIdAula(),  // Aula de clase regular
            null,  // idHorario - puede venir del bloque
            null,  // idTipo
            request.getPeriodoAcademico(),
            request.getIdProfesor(),  // Profesor titular
            request.getFechaExamen(),  // Fecha de clase regular
            request.getIdGrupo().toString(),
            ExamConstants.STATUS_PROGRAMADO
        );
        
        Schedule scheduleCreado = createSchedule(schedule);
        logger.info("✓ Examen NO Academia creado exitosamente. ID: {}", scheduleCreado.getIdExamen());
        
        return scheduleCreado;
    }

    /**
     * FLUJO: Examen Academia
     * Se busca hora óptima con impacto mínimo en otras clases
     */
    private Schedule crearHorarioExamenAcademia(ExamScheduleRequest request) {
        logger.info("--- Procesando EXAMEN ACADEMIA (idAcademia: {}) ---", request.getIdAcademia());
        
        // PASO 1: Validaciones básicas
        validarSolicitud(request);
        
        // PASO 2: Validar disponibilidad de profesor aplicador
        if (!examValidator.validarDisponibilidadProfesorAplicador(
                request.getIdProfesorAplicador(), request)) {
            throw new IllegalArgumentException(
                "Profesor aplicador no disponible");
        }
        
        // PASO 3: Validar sinodales pertenezcan a academia
        List<Integer> sinodalesAsignados = sinodales.validarYObtenerSinodales(request);
        logger.info("Sinodales de academia validados: {}", sinodalesAsignados.size());
        
        // PASO 4: Validar exclusión de inglés
        if (!examValidator.validarExclusionIngles(request)) {
            throw new IllegalArgumentException(
                "El horario propuesto afecta clases de inglés");
        }
        
        // PASO 5: BUSCAR HORA ÓPTIMA (solo para Academia)
        logger.info("Buscando hora óptima para minimizar impacto...");
        request = buscador.encontrarHoraOptima(
            request.getIdGrupo(),
            request.getIdProfesorAplicador(),
            request);
        logger.info("Hora óptima encontrada: {} {}", 
                   request.getFechaExamen(), request.getHoraExamen());
        
        // PASO 6: Validar disponibilidad de aula
        if (!examValidator.validarDisponibilidadAula(request.getIdAula(), request)) {
            throw new IllegalArgumentException("Aula no disponible");
        }
        
        // PASO 7: Para área de Salud, preferir salas
        if ("SALUD".equals(request.getArea())) {
            Integer salaOptima = buscador.seleccionarSalaOptima(request);
            request.setIdAula(salaOptima);
            logger.info("Sala seleccionada para área Salud: {}", salaOptima);
        }
        
        // PASO 8: Establecer duración según tipo y área
        // Para ORDINARIOS en SALUD: 2 horas obligatorio
        Integer duracion = establecerDuracionExamen(request.getTipoExamen(), request.getArea());
        request.setDuracionMinutos(duracion);
        logger.info("Duración establecida: {} minutos", duracion);
        
        // PASO 9: Validar duración
        if (!examValidator.validarDuracionExamen(request)) {
            throw new IllegalArgumentException("Duración inválida para tipo de examen " + request.getTipoExamen());
        }
        
        // PASO 10: Sinodales ya fueron validados en PASO 3 para academia
        logger.info("✓ Sinodales validados: {} sinodales para examen {}", 
                   sinodalesAsignados.size(), request.getTipoExamen());
        
        // PASO 11: Crear el Schedule en BD
        Schedule schedule = new Schedule(
            null,  // ID generado por BD
            request.getIdMateria(),
            request.getIdAula(),  // Aula seleccionada/sala
            null,  // idHorario
            null,  // idTipo
            request.getPeriodoAcademico(),
            request.getIdProfesor(),
            request.getFechaExamen(),  // Fecha óptima
            request.getIdGrupo().toString(),
            ExamConstants.STATUS_PROGRAMADO
        );        Schedule scheduleCreado = createSchedule(schedule);
        logger.info("✓ Examen Academia creado exitosamente. ID: {}", scheduleCreado.getIdExamen());
        
        return scheduleCreado;
    }

    /**
     * Helper: Establece la duración según tipo de examen
     * - Parciales: 1 hora (60 minutos)
     * - Ordinarios: 2 horas (120 minutos) para TODAS las áreas
     * - Extraordinarios: 1 hora
     * - Especiales: flexible (por defecto 2 horas)
     */
    private Integer establecerDuracionExamen(String tipoExamen, String area) {
        switch (tipoExamen) {
            case ExamConstants.TIPO_PARCIAL:
                logger.debug("Parcial: duración 1 hora");
                return ExamConstants.DURACION_PARCIAL;
            case ExamConstants.TIPO_ORDINARIO:
                logger.debug("Ordinario: duración 2 horas (para todas las áreas)");
                return ExamConstants.DURACION_ORDINARIO;
            case ExamConstants.TIPO_EXTRAORDINARIO:
                logger.debug("Extraordinario: duración 1 hora");
                return ExamConstants.DURACION_EXTRAORDINARIO;
            case ExamConstants.TIPO_ESPECIAL:
                logger.debug("Especial: duración flexible, por defecto 2 horas");
                return ExamConstants.DURACION_ORDINARIO;
            default:
                logger.debug("Tipo desconocido: duración por defecto 1 hora");
                return ExamConstants.DURACION_PARCIAL;
        }
    }

    /**
     * Validaciones básicas de la solicitud
     */
    private void validarSolicitud(ExamScheduleRequest request) {
        if (request.getIdMateria() == null) {
            throw new IllegalArgumentException("Materia es requerida");
        }
        if (request.getIdGrupo() == null) {
            throw new IllegalArgumentException("Grupo es requerido");
        }
        if (request.getIdProfesor() == null) {
            throw new IllegalArgumentException("Profesor titular es requerido");
        }
        if (request.getIdProfesorAplicador() == null) {
            throw new IllegalArgumentException("Profesor aplicador es requerido");
        }
        if (request.getTipoExamen() == null) {
            throw new IllegalArgumentException("Tipo de examen es requerido");
        }
        if (request.getPeriodoAcademico() == null) {
            throw new IllegalArgumentException("Período académico es requerido");
        }
    }

    /**
     * Método batch para crear múltiples horarios de examen
     * Útil para crear todos los horarios de un período
     */
    public List<Schedule> crearMultiplesHorariosExamen(List<ExamScheduleRequest> solicitudes) {
        logger.info("Creando {} horarios de examen en batch", solicitudes.size());
        
        return solicitudes.stream()
                .map(this::crearHorarioExamen)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el impacto de un horario propuesto (sin guardarlo)
     * Útil para prévisualización antes de crear
     */
    public Integer calcularImpacto(ExamScheduleRequest request) {
        logger.info("Calculando impacto del horario propuesto");
        return buscador.calcularImpactoHorario(request);
    }
}

