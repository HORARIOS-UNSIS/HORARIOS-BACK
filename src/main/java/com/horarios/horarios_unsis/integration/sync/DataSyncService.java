package com.horarios.horarios_unsis.integration.sync;

import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.repository.TeacherRepository;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.repository.SubjectRepository;
import com.horarios.horarios_unsis.data.classrooms.infrastructure.persistence.entity.ClassroomsEntity;
import com.horarios.horarios_unsis.data.classrooms.infrastructure.persistence.repository.ClassroomsRepository;
import com.horarios.horarios_unsis.data.period.infrastructure.persistence.entity.PeriodEntity;
import com.horarios.horarios_unsis.data.period.infrastructure.persistence.repository.PeriodRepository;
import com.horarios.horarios_unsis.data.career.infrastructure.persistence.entity.CareerEntity;
import com.horarios.horarios_unsis.data.career.infrastructure.persistence.repository.CareerRepository;
import com.horarios.horarios_unsis.data.group.infrastructure.persistence.entity.GroupEntity;
import com.horarios.horarios_unsis.data.group.infrastructure.persistence.repository.GroupRepository;

import com.horarios.horarios_unsis.integration.Consume.AulaConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.PeriodoConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.CarreraConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.GrupoConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.DTO.AulaExternaDTO;
import com.horarios.horarios_unsis.integration.Consume.DTO.PeriodoExternoDTO;
import com.horarios.horarios_unsis.integration.Consume.DTO.CarreraExternaDTO;
import com.horarios.horarios_unsis.integration.Consume.DTO.GrupoExternoDTO;
import com.horarios.horarios_unsis.integration.Consume.DTO.HorarioExternoDTO;
import com.horarios.horarios_unsis.data.asignacion.application.service.AsignacionProfesorMateriaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Servicio para sincronizar datos desde la API externa a la base de datos local.
 * 
 * Uso:
 * 1. Configurar en application.properties:
 *    - api.external.base-url=http://serv-horarios.unsis.lan
 *    - integration.external.enabled=true
 * 
 * 2. Inyectar este servicio y llamar a los métodos de sincronización
 * 
 * ACTUALIZADO: Ahora persiste períodos, carreras y grupos en la BD local.
 */
@Service
public class DataSyncService {

    private static final Logger logger = LoggerFactory.getLogger(DataSyncService.class);

    private final AulaConsumeClient aulaClient;
    private final PeriodoConsumeClient periodoClient;
    private final CarreraConsumeClient carreraClient;
    private final GrupoConsumeClient grupoClient;
    private final RestTemplate restTemplate;
    
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final ClassroomsRepository classroomsRepository;
    private final PeriodRepository periodRepository;
    private final CareerRepository careerRepository;
    private final GroupRepository groupRepository;

    private final AsignacionProfesorMateriaService asignacionService;
    
    // Cache en memoria para consultas rápidas
    private List<PeriodoExternoDTO> periodosCache = new ArrayList<>();
    private List<CarreraExternaDTO> carrerasCache = new ArrayList<>();
    private List<GrupoExternoDTO> gruposCache = new ArrayList<>();

    @Value("${integration.external.enabled:false}")
    private boolean integrationEnabled;
    
    @Value("${api.external.base-url:http://serv-horarios.unsis.lan}")
    private String baseUrl;

    public DataSyncService(
            AulaConsumeClient aulaClient,
            PeriodoConsumeClient periodoClient,
            CarreraConsumeClient carreraClient,
            GrupoConsumeClient grupoClient,
            RestTemplate restTemplate,
            TeacherRepository teacherRepository,
            SubjectRepository subjectRepository,
            ClassroomsRepository classroomsRepository,
            PeriodRepository periodRepository,
            CareerRepository careerRepository,
            GroupRepository groupRepository,
            AsignacionProfesorMateriaService asignacionService) {
        this.aulaClient = aulaClient;
        this.periodoClient = periodoClient;
        this.carreraClient = carreraClient;
        this.grupoClient = grupoClient;
        this.restTemplate = restTemplate;
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.classroomsRepository = classroomsRepository;
        this.periodRepository = periodRepository;
        this.careerRepository = careerRepository;
        this.groupRepository = groupRepository;
        this.asignacionService = asignacionService;
    }

    /**
     * Sincroniza todos los datos desde la API externa
     * Continúa aunque algunos fallen para sincronizar lo máximo posible
     * Nota: NO usa @Transactional para permitir commits parciales - cada upsert tiene su propia transacción
     */
    public SyncResult sincronizarTodo() {
        SyncResult result = new SyncResult();
        StringBuilder errores = new StringBuilder();
        
        if (!integrationEnabled) {
            logger.warn("Integración externa deshabilitada. Habilita con integration.external.enabled=true");
            result.setMessage("Integración deshabilitada");
            return result;
        }

        // Sincronizar cada tipo por separado, capturando errores individuales
        // ORDEN IMPORTANTE: Primero aulas, luego horarios (de donde se extraen profesores/materias)
        
        try {
            result.setPeriodosSincronizados(cargarPeriodos());
        } catch (Exception e) {
            logger.error("Error sincronizando períodos: {}", e.getMessage());
            errores.append("Períodos: ").append(e.getMessage()).append("; ");
        }

        try {
            result.setCarrerasSincronizadas(cargarCarreras());
        } catch (Exception e) {
            logger.error("Error sincronizando carreras: {}", e.getMessage());
            errores.append("Carreras: ").append(e.getMessage()).append("; ");
        }

        try {
            result.setGruposSincronizados(cargarGrupos());
        } catch (Exception e) {
            logger.error("Error sincronizando grupos: {}", e.getMessage());
            errores.append("Grupos: ").append(e.getMessage()).append("; ");
        }

        // Sincronizar AULAS primero (necesario para consultar horarios)
        try {
            result.setAulasSincronizadas(sincronizarAulas());
        } catch (Exception e) {
            logger.error("Error sincronizando aulas: {}", e.getMessage());
            errores.append("Aulas: ").append(e.getMessage()).append("; ");
        }

        // Sincronizar PROFESORES y MATERIAS desde horarios
        // Se hace una sola consulta de horarios para extraer ambos
        try {
            int[] resultadosProfesoresMaterias = sincronizarProfesoresYMaterias();
            result.setProfesoresSincronizados(resultadosProfesoresMaterias[0]);
            result.setMateriasSincronizadas(resultadosProfesoresMaterias[1]);
        } catch (Exception e) {
            logger.error("Error sincronizando profesores/materias: {}", e.getMessage());
            errores.append("Profesores/Materias: ").append(e.getMessage()).append("; ");
        }

        // Sincronizar ASIGNACIONES PROFESOR-MATERIA usando los horarios del período actual
        try {
            String periodoActual = obtenerPeriodoActual();
            if (periodoActual != null) {
                logger.info("Sincronizando asignaciones profesor-materia para período: {}", periodoActual);
                int asignaciones = asignacionService.sincronizarAsignacionesPorPeriodo(periodoActual);
                result.setAsignacionesSincronizadas(asignaciones);
                logger.info("Se sincronizaron {} asignaciones profesor-materia", asignaciones);
            } else {
                logger.warn("No se pudo determinar el período actual para sincronizar asignaciones");
            }
        } catch (Exception e) {
            logger.error("Error sincronizando asignaciones: {}", e.getMessage());
            errores.append("Asignaciones: ").append(e.getMessage()).append("; ");
        }

        // Determinar resultado
        int totalSincronizados = result.getPeriodosSincronizados() + result.getCarrerasSincronizadas() +
                                 result.getGruposSincronizados() + result.getProfesoresSincronizados() +
                                 result.getMateriasSincronizadas() + result.getAulasSincronizadas() +
                                 result.getAsignacionesSincronizadas();
        
        if (errores.length() == 0) {
            result.setSuccess(true);
            result.setMessage("Sincronización completada exitosamente. Total: " + totalSincronizados + " registros");
        } else if (totalSincronizados > 0) {
            result.setSuccess(true);
            result.setMessage("Sincronización parcial. " + totalSincronizados + " registros. Errores: " + errores);
        } else {
            result.setSuccess(false);
            result.setMessage("Errores: " + errores);
        }

        return result;
    }

    /**
     * Carga y persiste períodos desde la API externa a la BD local
     */
    @Transactional
    public int cargarPeriodos() {
        logger.info("Sincronizando períodos desde API externa a BD local...");
        
        try {
            periodosCache = periodoClient.obtenerTodosPeriodos();
            int count = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            for (PeriodoExternoDTO externo : periodosCache) {
                try {
                    // Buscar por clave o crear nuevo
                    PeriodEntity entity = periodRepository.findByClave(externo.getClave())
                            .orElse(new PeriodEntity());
                    
                    entity.setClave(externo.getClave());
                    entity.setNombre(externo.getNombre());
                    entity.setTipo(externo.getTipo());
                    
                    // Convertir fechas de String a LocalDate
                    if (externo.getFechaInicio() != null && !externo.getFechaInicio().isEmpty()) {
                        try {
                            entity.setFechaInicio(LocalDate.parse(externo.getFechaInicio(), formatter));
                        } catch (DateTimeParseException e) {
                            logger.debug("No se pudo parsear fechaInicio: {}", externo.getFechaInicio());
                        }
                    }
                    if (externo.getFechaFin() != null && !externo.getFechaFin().isEmpty()) {
                        try {
                            entity.setFechaFin(LocalDate.parse(externo.getFechaFin(), formatter));
                        } catch (DateTimeParseException e) {
                            logger.debug("No se pudo parsear fechaFin: {}", externo.getFechaFin());
                        }
                    }
                    
                    periodRepository.save(entity);
                    count++;
                    logger.debug("Período sincronizado: {} - {}", externo.getClave(), externo.getNombre());
                } catch (Exception e) {
                    logger.error("Error sincronizando período {}: {}", externo.getClave(), e.getMessage());
                }
            }
            
            logger.info("Se sincronizaron {} períodos en BD", count);
            return count;
        } catch (Exception e) {
            logger.error("Error cargando períodos: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Sincroniza períodos (alias de cargarPeriodos para compatibilidad con controlador)
     */
    public int sincronizarPeriodos() {
        return cargarPeriodos();
    }

    /**
     * Carga y persiste carreras desde la API externa a la BD local
     */
    @Transactional
    public int cargarCarreras() {
        logger.info("Sincronizando carreras desde API externa a BD local...");
        
        try {
            carrerasCache = carreraClient.obtenerCarreras();
            int count = 0;
            
            for (CarreraExternaDTO externa : carrerasCache) {
                try {
                    // Buscar por clave o crear nuevo
                    CareerEntity entity = careerRepository.findByClave(externa.getClave())
                            .orElse(new CareerEntity());
                    
                    entity.setClave(externa.getClave());
                    entity.setNombre(externa.getNombre());
                    entity.setVigente(externa.getVigente());
                    
                    careerRepository.save(entity);
                    count++;
                    logger.debug("Carrera sincronizada: {} - {}", externa.getClave(), externa.getNombre());
                } catch (Exception e) {
                    logger.error("Error sincronizando carrera {}: {}", externa.getClave(), e.getMessage());
                }
            }
            
            logger.info("Se sincronizaron {} carreras en BD", count);
            return count;
        } catch (Exception e) {
            logger.error("Error cargando carreras: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Sincroniza carreras (alias de cargarCarreras para compatibilidad con controlador)
     */
    public int sincronizarCarreras() {
        return cargarCarreras();
    }

    /**
     * Carga y persiste grupos del período actual desde la API externa a la BD local
     */
    @Transactional
    public int cargarGrupos() {
        logger.info("Sincronizando grupos del período actual desde API externa a BD local...");
        
        try {
            // Obtener período actual
            String periodoActual = obtenerPeriodoActualClave();
            if (periodoActual == null) {
                logger.warn("No se pudo determinar el período actual, usando todos los grupos");
                gruposCache = grupoClient.obtenerGrupos();
            } else {
                logger.info("Sincronizando grupos del período: {}", periodoActual);
                gruposCache = grupoClient.obtenerGruposPorPeriodo(periodoActual);
            }
            int count = 0;
            
            for (GrupoExternoDTO externo : gruposCache) {
                try {
                    // Buscar por clave + período o crear nuevo
                    GroupEntity entity = groupRepository.findByClaveAndClavePeriodo(
                            externo.getClave(), externo.getPeriodo())
                            .orElse(new GroupEntity());
                    
                    entity.setClave(externo.getClave());
                    entity.setNombre(externo.getNombre());
                    entity.setClaveCarrera(externo.getCarrera());
                    entity.setSemestre(externo.getSemestre());
                    entity.setAlumnos(externo.getAlumnos());
                    entity.setClavePeriodo(externo.getPeriodo());
                    
                    groupRepository.save(entity);
                    count++;
                    logger.debug("Grupo sincronizado: {} periodo {}", externo.getClave(), externo.getPeriodo());
                } catch (Exception e) {
                    logger.error("Error sincronizando grupo {}: {}", externo.getClave(), e.getMessage());
                }
            }
            
            logger.info("Se sincronizaron {} grupos en BD", count);
            return count;
        } catch (Exception e) {
            logger.error("Error cargando grupos: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Sincroniza grupos (alias de cargarGrupos para compatibilidad con controlador)
     */
    public int sincronizarGrupos() {
        return cargarGrupos();
    }

    /**
     * Obtiene los períodos cargados en cache
     */
    public List<PeriodoExternoDTO> obtenerPeriodos() {
        if (periodosCache.isEmpty()) {
            cargarPeriodos();
        }
        return periodosCache;
    }

    /**
     * Obtiene las carreras cargadas en cache
     */
    public List<CarreraExternaDTO> obtenerCarreras() {
        if (carrerasCache.isEmpty()) {
            cargarCarreras();
        }
        return carrerasCache;
    }

    /**
     * Obtiene los grupos cargados en cache
     */
    public List<GrupoExternoDTO> obtenerGrupos() {
        if (gruposCache.isEmpty()) {
            cargarGrupos();
        }
        return gruposCache;
    }

    /**
     * Obtiene la clave del período actual (wrapper público)
     * @return Clave del período actual (ej: "2526A") o null si no se puede determinar
     */
    public String obtenerPeriodoActualClave() {
        return obtenerPeriodoActual();
    }

    /**
     * Obtiene el período actual basándose en la fecha actual
     * 
     * Lógica:
     * 1. Busca un período donde la fecha actual esté entre fechaInicio y fechaFin
     * 2. Si no encuentra, toma el período más reciente que ya haya iniciado
     * 3. Evita tomar períodos futuros que aún no tienen horarios
     */
    private String obtenerPeriodoActual() {
        try {
            if (periodosCache.isEmpty()) {
                periodosCache = periodoClient.obtenerTodosPeriodos();
            }
            if (periodosCache.isEmpty()) {
                return null;
            }
            
            LocalDate hoy = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            // Buscar período donde hoy esté entre fechaInicio y fechaFin
            for (PeriodoExternoDTO periodo : periodosCache) {
                try {
                    if (periodo.getFechaInicio() != null && periodo.getFechaFin() != null) {
                        LocalDate inicio = LocalDate.parse(periodo.getFechaInicio(), formatter);
                        LocalDate fin = LocalDate.parse(periodo.getFechaFin(), formatter);
                        
                        if (!hoy.isBefore(inicio) && !hoy.isAfter(fin)) {
                            logger.info("Período actual encontrado por fechas: {}", periodo.getClave());
                            return periodo.getClave();
                        }
                    }
                } catch (Exception e) {
                    // Ignorar errores de parseo de fechas
                }
            }
            
            // Si no encontramos por fechas, buscar el período más reciente que ya inició
            String periodoMasReciente = null;
            LocalDate fechaInicioMasReciente = null;
            
            for (PeriodoExternoDTO periodo : periodosCache) {
                try {
                    if (periodo.getFechaInicio() != null) {
                        LocalDate inicio = LocalDate.parse(periodo.getFechaInicio(), formatter);
                        
                        // Solo considerar períodos que ya iniciaron
                        if (!inicio.isAfter(hoy)) {
                            if (fechaInicioMasReciente == null || inicio.isAfter(fechaInicioMasReciente)) {
                                fechaInicioMasReciente = inicio;
                                periodoMasReciente = periodo.getClave();
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignorar errores de parseo
                }
            }
            
            if (periodoMasReciente != null) {
                logger.info("Período más reciente que ya inició: {}", periodoMasReciente);
                return periodoMasReciente;
            }
            
            // Fallback: tomar el penúltimo (el último suele ser futuro)
            if (periodosCache.size() >= 2) {
                String penultimo = periodosCache.get(periodosCache.size() - 2).getClave();
                logger.info("Usando penúltimo período (fallback): {}", penultimo);
                return penultimo;
            }
            
            // Último recurso: el único que hay
            return periodosCache.get(periodosCache.size() - 1).getClave();
            
        } catch (Exception e) {
            logger.error("Error obteniendo período actual: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los horarios del período actual consultando todas las aulas
     * @return Lista de horarios del período actual
     */
    private List<HorarioExternoDTO> obtenerTodosLosHorariosDelPeriodoActual() {
        List<HorarioExternoDTO> todosLosHorarios = new ArrayList<>();
        
        String periodoActual = obtenerPeriodoActual();
        if (periodoActual == null) {
            logger.warn("No se pudo obtener el período actual");
            return todosLosHorarios;
        }
        
        logger.info("Obteniendo horarios del período: {}", periodoActual);
        
        // Obtener lista de aulas
        List<AulaExternaDTO> aulas = aulaClient.obtenerAulasDelAPI();
        logger.info("Se encontraron {} aulas para consultar", aulas.size());
        
        int aulasConsultadas = 0;
        int aulasConError = 0;
        for (AulaExternaDTO aula : aulas) {
            try {
                String url = baseUrl + "/api/horarios/" + periodoActual + "/aula/" + aula.getClave();
                logger.debug("Consultando: {}", url);
                
                // Usar Map[] en lugar del DTO para evitar problemas de deserialización
                @SuppressWarnings("unchecked")
                Map<String, Object>[] horariosMap = restTemplate.getForObject(url, Map[].class);
                
                if (horariosMap != null && horariosMap.length > 0) {
                    for (Map<String, Object> h : horariosMap) {
                        // Convertir Map a DTO manualmente
                        HorarioExternoDTO dto = new HorarioExternoDTO();
                        dto.setRowId(h.get("rowId") != null ? ((Number)h.get("rowId")).intValue() : null);
                        dto.setIdProfesorStr(h.get("idprofesor") != null ? h.get("idprofesor").toString() : null);
                        dto.setNombreProfesor(h.get("nombreCompleto") != null ? h.get("nombreCompleto").toString() : null);
                        dto.setCodigoAsignatura(h.get("asignatura") != null ? h.get("asignatura").toString() : null);
                        dto.setNombreMateria(h.get("materia") != null ? h.get("materia").toString() : null);
                        dto.setIdGrupoStr(h.get("idGrupo") != null ? h.get("idGrupo").toString() : null);
                        dto.setNombreGrupo(h.get("nombreGrupo") != null ? h.get("nombreGrupo").toString() : null);
                        dto.setIdAulaStr(h.get("idAula") != null ? h.get("idAula").toString() : null);
                        dto.setNombreAula(h.get("nombreAula") != null ? h.get("nombreAula").toString() : null);
                        dto.setDia(h.get("dia") != null ? ((Number)h.get("dia")).intValue() : null);
                        dto.setHora(h.get("hora") != null ? ((Number)h.get("hora")).intValue() : null);
                        dto.setCarrera(h.get("carrera") != null ? h.get("carrera").toString() : null);
                        dto.setPeriodo(h.get("periodoq") != null ? h.get("periodoq").toString() : null);
                        
                        todosLosHorarios.add(dto);
                    }
                    logger.debug("Aula {}: {} horarios encontrados", aula.getClave(), horariosMap.length);
                }
                aulasConsultadas++;
            } catch (Exception e) {
                aulasConError++;
                // Mostrar los primeros 5 errores para diagnóstico
                if (aulasConError <= 5) {
                    logger.warn("Error consultando aula {} ({}): {}", aula.getClave(), aula.getNombre(), e.getMessage());
                    // Mostrar causa raíz si existe
                    if (e.getCause() != null) {
                        logger.warn("  Causa: {}", e.getCause().getMessage());
                        if (e.getCause().getCause() != null) {
                            logger.warn("  Causa raíz: {}", e.getCause().getCause().getMessage());
                        }
                    }
                }
            }
        }
        
        if (aulasConError > 0) {
            logger.warn("Total aulas con error: {} de {}", aulasConError, aulas.size());
        }
        
        logger.info("Total: {} horarios obtenidos de {} aulas", todosLosHorarios.size(), aulasConsultadas);
        return todosLosHorarios;
    }

    /**
     * Sincroniza PROFESORES y MATERIAS en una sola pasada
     * Optimización: Consulta horarios UNA sola vez y extrae ambos
     * 
     * @return Array [profesoresSincronizados, materiasSincronizadas]
     * 
     * Nota: NO usa @Transactional para permitir commits parciales en caso de errores
     */
    public int[] sincronizarProfesoresYMaterias() {
        logger.info("=== Sincronizando PROFESORES y MATERIAS desde horarios ===");
        
        // Obtener todos los horarios UNA sola vez
        List<HorarioExternoDTO> horarios = obtenerTodosLosHorariosDelPeriodoActual();
        
        if (horarios.isEmpty()) {
            logger.warn("No se obtuvieron horarios para extraer profesores/materias");
            return new int[]{0, 0};
        }
        
        // Extraer profesores y materias únicos en una sola pasada
        Map<Integer, String> profesoresUnicos = new HashMap<>();
        Map<String, String> materiasUnicas = new HashMap<>();
        
        for (HorarioExternoDTO horario : horarios) {
            // Extraer profesor
            Integer idProfesor = horario.getIdProfesor();
            String nombreProfesor = horario.getNombreProfesor();
            if (idProfesor != null && !profesoresUnicos.containsKey(idProfesor)) {
                profesoresUnicos.put(idProfesor, nombreProfesor != null ? nombreProfesor : "Profesor #" + idProfesor);
            }
            
            // Extraer materia
            String codigoAsignatura = horario.getCodigoAsignatura();
            String nombreMateria = horario.getNombreMateria();
            if (codigoAsignatura != null && !materiasUnicas.containsKey(codigoAsignatura)) {
                materiasUnicas.put(codigoAsignatura, nombreMateria != null ? nombreMateria : "Materia " + codigoAsignatura);
            }
        }
        
        logger.info("Encontrados: {} profesores únicos, {} materias únicas", 
                   profesoresUnicos.size(), materiasUnicas.size());
        
        // Persistir profesores usando UPSERT nativo
        int countProfesores = 0;
        for (Map.Entry<Integer, String> entry : profesoresUnicos.entrySet()) {
            try {
                teacherRepository.upsertProfesor(entry.getKey(), entry.getValue(), false);
                countProfesores++;
            } catch (Exception e) {
                logger.error("Error guardando profesor {}: {}", entry.getKey(), e.getMessage());
            }
        }
        
        // Persistir materias usando UPSERT nativo
        int countMaterias = 0;
        for (Map.Entry<String, String> entry : materiasUnicas.entrySet()) {
            try {
                Integer idMateria = extraerIdDeCodigoAsignatura(entry.getKey());
                if (idMateria == null) continue;
                
                subjectRepository.upsertMateria(idMateria, entry.getValue(), false);
                countMaterias++;
            } catch (Exception e) {
                logger.error("Error guardando materia {}: {}", entry.getKey(), e.getMessage());
            }
        }
        
        logger.info("✓ Sincronización completada: {} profesores, {} materias", countProfesores, countMaterias);
        return new int[]{countProfesores, countMaterias};
    }

    /**
     * Sincroniza profesores extrayéndolos directamente de los horarios del período actual
     * 
     * Flujo:
     * 1. Obtener período actual
     * 2. Consultar horarios de todas las aulas
     * 3. Extraer profesores únicos de los horarios
     * 4. Persistir en BD
     * 
     * Nota: NO usa @Transactional para permitir commits parciales en caso de errores
     */
    public int sincronizarProfesores() {
        logger.info("=== Sincronizando PROFESORES desde horarios del período actual ===");
        
        // Obtener todos los horarios
        List<HorarioExternoDTO> horarios = obtenerTodosLosHorariosDelPeriodoActual();
        
        if (horarios.isEmpty()) {
            logger.warn("No se obtuvieron horarios para extraer profesores");
            return 0;
        }
        
        // Extraer profesores únicos (Map para evitar duplicados)
        Map<Integer, String> profesoresUnicos = new HashMap<>();
        for (HorarioExternoDTO horario : horarios) {
            Integer idProfesor = horario.getIdProfesor();
            String nombreProfesor = horario.getNombreProfesor();
            
            if (idProfesor != null && !profesoresUnicos.containsKey(idProfesor)) {
                profesoresUnicos.put(idProfesor, nombreProfesor != null ? nombreProfesor : "Profesor #" + idProfesor);
            }
        }
        
        logger.info("Se encontraron {} profesores únicos en los horarios", profesoresUnicos.size());
        
        // Persistir profesores usando UPSERT nativo (evita problemas de concurrencia)
        int count = 0;
        for (Map.Entry<Integer, String> entry : profesoresUnicos.entrySet()) {
            try {
                teacherRepository.upsertProfesor(entry.getKey(), entry.getValue(), false);
                count++;
                logger.debug("Profesor sincronizado: {} - {}", entry.getKey(), entry.getValue());
            } catch (Exception e) {
                logger.error("Error sincronizando profesor {}: {}", entry.getKey(), e.getMessage());
            }
        }

        logger.info("✓ Sincronización de profesores completada. {} registros.", count);
        return count;
    }

    /**
     * Sincroniza materias extrayéndolas directamente de los horarios del período actual
     * 
     * Flujo:
     * 1. Obtener período actual
     * 2. Consultar horarios de todas las aulas
     * 3. Extraer materias únicas de los horarios
     * 4. Persistir en BD
     * 
     * Nota: NO usa @Transactional para permitir commits parciales en caso de errores
     */
    public int sincronizarMaterias() {
        logger.info("=== Sincronizando MATERIAS desde horarios del período actual ===");
        
        // Obtener todos los horarios
        List<HorarioExternoDTO> horarios = obtenerTodosLosHorariosDelPeriodoActual();
        
        if (horarios.isEmpty()) {
            logger.warn("No se obtuvieron horarios para extraer materias");
            return 0;
        }
        
        // Extraer materias únicas (Map para evitar duplicados)
        // Usamos el código de asignatura como identificador único
        Map<String, String> materiasUnicas = new HashMap<>();
        for (HorarioExternoDTO horario : horarios) {
            String codigoAsignatura = horario.getCodigoAsignatura(); // ej: "5032_2024"
            String nombreMateria = horario.getNombreMateria();
            
            if (codigoAsignatura != null && !materiasUnicas.containsKey(codigoAsignatura)) {
                materiasUnicas.put(codigoAsignatura, nombreMateria != null ? nombreMateria : "Materia " + codigoAsignatura);
            }
        }
        
        logger.info("Se encontraron {} materias únicas en los horarios", materiasUnicas.size());
        
        // Persistir materias usando UPSERT nativo
        int count = 0;
        for (Map.Entry<String, String> entry : materiasUnicas.entrySet()) {
            try {
                // Extraer ID numérico del código de asignatura (ej: "5032_2024" -> 5032)
                Integer idMateria = extraerIdDeCodigoAsignatura(entry.getKey());
                if (idMateria == null) {
                    logger.debug("No se pudo extraer ID de asignatura: {}", entry.getKey());
                    continue;
                }
                
                subjectRepository.upsertMateria(idMateria, entry.getValue(), false);
                count++;
                logger.debug("Materia sincronizada: {} - {}", idMateria, entry.getValue());
            } catch (Exception e) {
                logger.error("Error sincronizando materia {}: {}", entry.getKey(), e.getMessage());
            }
        }

        logger.info("✓ Sincronización de materias completada. {} registros.", count);
        return count;
    }
    
    /**
     * Extrae el ID numérico del código de asignatura
     * Ej: "5032_2024" -> 5032
     */
    private Integer extraerIdDeCodigoAsignatura(String codigoAsignatura) {
        if (codigoAsignatura == null) return null;
        try {
            if (codigoAsignatura.contains("_")) {
                return Integer.parseInt(codigoAsignatura.split("_")[0]);
            }
            return Integer.parseInt(codigoAsignatura);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Sincroniza aulas desde la API externa a la BD local
     */
    @Transactional
    public int sincronizarAulas() {
        logger.info("Iniciando sincronización de aulas...");
        
        List<AulaExternaDTO> aulasExternas = aulaClient.obtenerAulasDelAPI();
        int count = 0;

        for (AulaExternaDTO externa : aulasExternas) {
            try {
                // Buscar por clave primero, si no existe crear nuevo
                ClassroomsEntity entity = classroomsRepository.findByClave(externa.getClave())
                        .orElse(new ClassroomsEntity());
                
                entity.setClave(externa.getClave());
                entity.setNombre(externa.getNombre());
                entity.setCapacidad(externa.getCapacidad());
                entity.setTipo(externa.getTipo());
                entity.setStatusProyector(externa.getStatusProyector());
                
                classroomsRepository.save(entity);
                count++;
                logger.debug("Aula sincronizada: {} - {}", externa.getClave(), externa.getNombre());
            } catch (Exception e) {
                logger.error("Error sincronizando aula {}: {}", externa.getClave(), e.getMessage());
            }
        }

        logger.info("Sincronización de aulas completada. {} registros procesados.", count);
        return count;
    }

    /**
     * Clase para representar el resultado de la sincronización
     */
    public static class SyncResult {
        private boolean success;
        private String message;
        private int periodosSincronizados;
        private int carrerasSincronizadas;
        private int gruposSincronizados;
        private int profesoresSincronizados;
        private int materiasSincronizadas;
        private int aulasSincronizadas;
        private int asignacionesSincronizadas;

        // Getters y Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public int getPeriodosSincronizados() { return periodosSincronizados; }
        public void setPeriodosSincronizados(int periodosSincronizados) { this.periodosSincronizados = periodosSincronizados; }
        
        public int getCarrerasSincronizadas() { return carrerasSincronizadas; }
        public void setCarrerasSincronizadas(int carrerasSincronizadas) { this.carrerasSincronizadas = carrerasSincronizadas; }
        
        public int getGruposSincronizados() { return gruposSincronizados; }
        public void setGruposSincronizados(int gruposSincronizados) { this.gruposSincronizados = gruposSincronizados; }
        
        public int getProfesoresSincronizados() { return profesoresSincronizados; }
        public void setProfesoresSincronizados(int profesoresSincronizados) { this.profesoresSincronizados = profesoresSincronizados; }
        
        public int getMateriasSincronizadas() { return materiasSincronizadas; }
        public void setMateriasSincronizadas(int materiasSincronizadas) { this.materiasSincronizadas = materiasSincronizadas; }
        
        public int getAulasSincronizadas() { return aulasSincronizadas; }
        public void setAulasSincronizadas(int aulasSincronizadas) { this.aulasSincronizadas = aulasSincronizadas; }
        
        public int getAsignacionesSincronizadas() { return asignacionesSincronizadas; }
        public void setAsignacionesSincronizadas(int asignacionesSincronizadas) { this.asignacionesSincronizadas = asignacionesSincronizadas; }
    }

    /**
     * Sincroniza las asignaciones profesor-materia desde el endpoint de horarios
     * Se debe llamar después de sincronizar grupos
     */
    @Transactional
    public int sincronizarAsignacionesPorPeriodo(String clavePeriodo) {
        logger.info("Sincronizando asignaciones para período: {}", clavePeriodo);
        return asignacionService.sincronizarAsignacionesPorPeriodo(clavePeriodo);
    }

    /**
     * Sincroniza asignaciones solo para una carrera
     */
    @Transactional
    public int sincronizarAsignacionesPorCarrera(String clavePeriodo, String claveCarrera) {
        logger.info("Sincronizando asignaciones para carrera {} en período {}", claveCarrera, clavePeriodo);
        return asignacionService.sincronizarAsignacionesPorCarrera(clavePeriodo, claveCarrera);
    }
}
