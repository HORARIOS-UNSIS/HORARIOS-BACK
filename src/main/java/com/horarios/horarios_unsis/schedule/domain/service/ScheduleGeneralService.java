package com.horarios.horarios_unsis.schedule.domain.service;

import com.horarios.horarios_unsis.data.classrooms.domain.model.Classrooms;
import com.horarios.horarios_unsis.data.classrooms.domain.port.in.ClassroomsUseCase;
import com.horarios.horarios_unsis.data.schoolHours.domain.model.SchoollHors;
import com.horarios.horarios_unsis.data.schoolHours.domain.port.in.SchoolHoursServicePort;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import com.horarios.horarios_unsis.data.subject.domain.port.out.SubjectRepositoryPort;
import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import com.horarios.horarios_unsis.data.teacher.domain.port.out.TeacherRepositoryPort;
import com.horarios.horarios_unsis.data.teacherSubjectAssignment.infrastructure.persistence.entity.TeacherSubjectAssignmentEntity;
import com.horarios.horarios_unsis.data.teacherSubjectAssignment.infrastructure.persistence.repository.TeacherSubjectAssignmentRepository;
import com.horarios.horarios_unsis.schedule.application.dto.ScheduleResponseDTO;
import com.horarios.horarios_unsis.schedule.application.dto.request.ScheduleGeneralRequest;
import com.horarios.horarios_unsis.schedule.application.mapper.ScheduleMapper;
import com.horarios.horarios_unsis.schedule.domain.model.Schedule;
import com.horarios.horarios_unsis.schedule.domain.port.out.SchedulePersistentPort;
import com.horarios.horarios_unsis.data.career.domain.model.Career;
import com.horarios.horarios_unsis.data.career.domain.port.out.CareerRepositoryPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleGeneralService {

    private static final Logger log = LoggerFactory.getLogger(ScheduleGeneralService.class);

    private final SubjectRepositoryPort subjectRepository;
    private final TeacherRepositoryPort teacherRepository;
    private final ClassroomsUseCase classroomUseCase;
    private final SchoolHoursServicePort schoolHoursService;
    private final SchedulePersistentPort schedulePersistentPort;
    private final TeacherSubjectAssignmentRepository assignmentRepository;
    private final CareerRepositoryPort careerRepository;

    public ScheduleGeneralService(
            SubjectRepositoryPort subjectRepository,
            TeacherRepositoryPort teacherRepository,
            ClassroomsUseCase classroomUseCase,
            SchoolHoursServicePort schoolHoursService,
            SchedulePersistentPort schedulePersistentPort,
            TeacherSubjectAssignmentRepository assignmentRepository,
            CareerRepositoryPort careerRepository) {
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.classroomUseCase = classroomUseCase;
        this.schoolHoursService = schoolHoursService;
        this.schedulePersistentPort = schedulePersistentPort;
        this.assignmentRepository = assignmentRepository;
        this.careerRepository = careerRepository;
    }

    public List<ScheduleResponseDTO> generateSchedules(ScheduleGeneralRequest request, String type, LocalDate start, LocalDate end) {
        List<Schedule> generated = generateExams(request, type, start, end);
        return generated.stream()
                .map(ScheduleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    private String normalize(String input) {
        if (input == null) return "";
        return java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", ""); // Normaliza a solo alfanuméricos para mejor matching
    }

    private boolean isFuzzyPeriodMatch(String dbPeriod, String reqPeriod) {
        String db = normalize(dbPeriod);
        String req = normalize(reqPeriod);
        if (db.contains(req) || req.contains(db)) return true;
        
        // Match especial para formato corto "2526A" vs "20252026A"
        // Verificamos si los caracteres de req aparecen en orden en db
        int dbIdx = 0;
        int matches = 0;
        for (char c : req.toCharArray()) {
            while (dbIdx < db.length()) {
                if (db.charAt(dbIdx) == c) {
                    matches++;
                    dbIdx++;
                    break;
                }
                dbIdx++;
            }
        }
        // Si encontramos todos los caracteres en orden, es match
        if (matches == req.length()) return true;

        return false;
    }

    private List<Schedule> generateExams(ScheduleGeneralRequest request, String type, LocalDate startDate, LocalDate endDate) {
        log.info("DEBUG: Iniciando proceso para '{}'", request.getLicenciatura());

        String normalizedRequestName = normalize(request.getLicenciatura());
        String normalizedRequestPeriod = normalize(request.getPeriodo());

        // 1. Obtener Clave Base (ej: "06") buscando por nombre normalizado
        // Intentamos coincidencia parcial si la exacta falla
        String claveBase = careerRepository.findAll().stream()
                .filter(c -> normalize(c.getNombre()).contains(normalizedRequestName) || normalizedRequestName.contains(normalize(c.getNombre())))
                .map(Career::getClave).findFirst().orElse(null);

        if (claveBase == null) {
            log.error("DEBUG: La carrera '{}' no fue encontrada (Normalizada: {})", request.getLicenciatura(), normalizedRequestName);
            // DEBUG EXTRA: Listar carreras disponibles para ver por qué falla
            careerRepository.findAll().forEach(c -> 
                log.info("DEBUG: Carrera DB: '{}' -> Norm: '{}', Clave: '{}'", 
                    c.getNombre(), normalize(c.getNombre()), c.getClave())
            );
            return new ArrayList<>();
        }

        // 2. Filtro de Asignaciones
        List<TeacherSubjectAssignmentEntity> allAssignments = assignmentRepository.findAll();
        log.info("DEBUG: Total asignaciones en DB: {}", allAssignments.size());

        List<TeacherSubjectAssignmentEntity> asignaciones = allAssignments.stream()
                .filter(a -> {
                    boolean careerMatch = a.getClaveCarrera() != null && a.getClaveCarrera().trim().startsWith(claveBase.trim());
                    if (!careerMatch && a.getClaveCarrera() != null && a.getClaveCarrera().startsWith("0")) { 
                         // Debug para ver qué claves de carrera hay si falla
                         // log.info("DEBUG: Asignacion rechazada por carrera: DB='{}' vs ClaveBase='{}'", a.getClaveCarrera(), claveBase);
                    }
                    return careerMatch;
                }) 
                .filter(a -> {
                    boolean periodMatch = isFuzzyPeriodMatch(a.getClavePeriodo(), request.getPeriodo());
                    if (!periodMatch) {
                         // Debug periodos
                         // log.info("DEBUG: Asignacion rechazada por periodo: DB='{}' vs Req='{}'", a.getClavePeriodo(), request.getPeriodo());
                    }
                    return periodMatch;
                })
                .filter(a -> request.getGrupos() == null || request.getGrupos().isEmpty() || 
                             request.getGrupos().stream().anyMatch(g -> g != null && a.getClaveGrupo() != null && a.getClaveGrupo().startsWith(g)))
                .collect(Collectors.toList());

        log.info("DEBUG: Clave Base: {}, Asignaciones filtradas: {}", claveBase, asignaciones.size());
        
        if (asignaciones.isEmpty()) {
            log.warn("DEBUG: No se encontraron asignaciones. Verificando datos de muestra en DB...");
            allAssignments.stream().limit(5).forEach(a -> 
                log.info("DEBUG: Muestra Asignacion -> Carrera: '{}', Periodo: '{}', Grupo: '{}'", 
                    a.getClaveCarrera(), a.getClavePeriodo(), a.getClaveGrupo())
            );
            return new ArrayList<>();
        }

        // 3. Carga de Recursos
        List<Teacher> claustro = teacherRepository.findAll();
        List<Classrooms> aulas = fetchAllAulas();
        List<SchoollHors> slots = fetchSortedSlots();
        Map<String, Integer> subjectMap = fetchSubjectMap();

        log.info("DEBUG: Recursos cargados -> Claustro: {}, Aulas: {}, Slots: {}", claustro.size(), aulas.size(), slots.size());
        if (!slots.isEmpty()) {
            log.info("DEBUG: Primer slot: {} - {}", slots.get(0).getStartTime(), slots.get(0).getEndTime());
            if (slots.size() > 1) log.info("DEBUG: Segundo slot: {} - {}", slots.get(1).getStartTime(), slots.get(1).getEndTime());
        }

        Set<String> teacherBusy = new HashSet<>();
        Set<String> roomBusy = new HashSet<>();
        Set<String> groupBusy = new HashSet<>();
        Set<String> groupDateBusy = new HashSet<>(); // Limita 1 examen por día por grupo
        List<Schedule> result = new ArrayList<>();
        int idTipo = type.equalsIgnoreCase("PARCIAL") ? 1 : 2;

        // 4. Algoritmo de Generación
        log.info("DEBUG: Iniciando algoritmo de asignación para {} materias.", asignaciones.size());
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            log.info("DEBUG: Evaluando fecha {}", date);
            for (TeacherSubjectAssignmentEntity asig : asignaciones) {
                if (isAlreadyProcessed(result, asig.getClaveMateria(), asig.getClaveGrupo())) {
                     continue;
                }

                String groupKey = asig.getClaveCarrera() + "-" + asig.getClaveGrupo();
                
                // Restriction: Only 1 exam per day per group
                if (groupDateBusy.contains(groupKey + "|" + date)) {
                    continue; 
                }

                Teacher titular = claustro.stream()
                        .filter(t -> t.getIdProfesor().equals(asig.getIdProfesor()))
                        .findFirst().orElse(null);
                
                if (titular == null) {
                    log.error("DEBUG: Profesor titular ID {} no encontrado", asig.getIdProfesor());
                    continue;
                }

                int sizeBefore = result.size();

                if (type.equalsIgnoreCase("PARCIAL")) {
                    processParcial(result, asig, titular, slots, aulas, date, idTipo, groupKey, teacherBusy, roomBusy, groupBusy, subjectMap);
                } else {
                    processOrdinario(result, asig, titular, slots, aulas, claustro, date, idTipo, groupKey, teacherBusy, roomBusy, groupBusy, subjectMap);
                }
                
                if (result.size() > sizeBefore) {
                    // Start tracking limit per day
                    groupDateBusy.add(groupKey + "|" + date);
                    log.info("DEBUG: EXITO Asignada materia {} grupo {} en fecha {}", asig.getNombreMateria(), asig.getClaveGrupo(), date);
                } else {
                    log.warn("DEBUG: FALLO al asignar materia {} grupo {} en fecha {} (Posible falta de slots/aulas)", asig.getNombreMateria(), asig.getClaveGrupo(), date);
                }
            }
        }
        log.info("DEBUG: Generación completada. Total horarios: {}", result.size());
        return result;
    }

    private void processParcial(List<Schedule> res, TeacherSubjectAssignmentEntity asig, Teacher tit, List<SchoollHors> slots, List<Classrooms> aulas, LocalDate d, int idT, String gK, Set<String> tB, Set<String> rB, Set<String> gB, Map<String, Integer> sM) {
        SchoollHors slot = slots.stream()
            .filter(s -> s.getPeriodNumber() == asig.getHora())
            .findFirst()
            .orElse(null);

        if (slot == null) {
            log.warn("DEBUG: No slot found for Partial exam (Period {}).", asig.getHora());
            return;
        }

        // Try to find available classroom
        for (Classrooms aula : aulas) {
             if (checkAvailability(tit.getIdProfesor(), null, aula.getIdAula(), gK, slot, null, d, tB, rB, gB)) {
                saveAndMark(res, asig, tit, null, aula, slot, null, d, idT, gK, tB, rB, gB, sM);
                return;
            }
        }
    }

    private void processOrdinario(List<Schedule> res, TeacherSubjectAssignmentEntity asig, Teacher tit, List<SchoollHors> slots, List<Classrooms> aulas, List<Teacher> claustro, LocalDate d, int idT, String gK, Set<String> tB, Set<String> rB, Set<String> gB, Map<String, Integer> sM) {
        int contiguousSlotsFound = 0;
        int titularAvailableCount = 0;
        int aulaAvailableCount = 0;

        for (int i = 0; i < slots.size() - 1; i++) {
            SchoollHors s1 = slots.get(i);
            SchoollHors s2 = slots.get(i + 1);
            
            // Check for contiguous slots
            if (!s1.getEndTime().equals(s2.getStartTime())) {
                 continue;
            }
            contiguousSlotsFound++;

            // Check Titular & Group availability first
            if (!checkAvailability(tit.getIdProfesor(), null, null, gK, s1, s2, d, tB, rB, gB)) {
                continue;
            }
            titularAvailableCount++;

            // Iterate through ALL classrooms
            for (Classrooms aula : aulas) {
                // Check Aula availability
                if (!checkAvailability(null, null, aula.getIdAula(), null, s1, s2, d, tB, rB, gB)) continue;
                aulaAvailableCount++;

                // Iterate through ALL synodals
                for (Teacher sinodal : claustro) {
                    if (sinodal.getIdProfesor().equals(tit.getIdProfesor())) continue;

                    // Check Synodal availability
                     if (checkAvailability(null, sinodal.getIdProfesor(), null, null, s1, s2, d, tB, rB, gB)) {
                        saveAndMark(res, asig, tit, sinodal, aula, s1, s2, d, idT, gK, tB, rB, gB, sM);
                        return; // Successfully assigned
                    }
                }
            }
        }
        
        // Only log detailed failure if really needed (first few times)
        log.warn("DEBUG: Fallo Ordinario '{}' -> Slots contiguos: {}, Titular Disp: {}, Aulas Disp: {}, Claustro: {}", 
            asig.getNombreMateria(), contiguousSlotsFound, titularAvailableCount, aulaAvailableCount, claustro.size());
    }

    private void saveAndMark(List<Schedule> list, TeacherSubjectAssignmentEntity asig, Teacher t, Teacher s, Classrooms a, SchoollHors s1, SchoollHors s2, LocalDate d, int idT, String gK, Set<String> tB, Set<String> rB, Set<String> gB, Map<String, Integer> sM) {
        Integer idM = sM.getOrDefault(asig.getNombreMateria(), 0);
        Schedule sch = new Schedule(null, idM, a.getIdAula(), s1.getId().intValue(), idT, 1, t.getIdProfesor(), d, asig.getClaveGrupo(), "PENDING", asig.getClaveMateria(), a.getNombre(), asig.getNombreMateria(), t.getNombre(), true, s1.getStartTime(), (s2 != null ? s2.getEndTime() : s1.getEndTime()), false);
        schedulePersistentPort.save(sch);
        list.add(sch);
        markBusy(t.getIdProfesor(), a.getIdAula(), gK, s1, s2, d, tB, rB, gB);
        if (s != null) markBusy(s.getIdProfesor(), null, null, s1, s2, d, tB, new HashSet<>(), new HashSet<>());
    }

    private void markBusy(Integer tI, Integer aI, String gK, SchoollHors s1, SchoollHors s2, LocalDate d, Set<String> tB, Set<String> rB, Set<String> gB) {
        String k1 = "-" + s1.getId() + "-" + d;
        if (tI != null) tB.add(tI + k1); if (aI != null) rB.add(aI + k1); if (gK != null) gB.add(gK + k1);
        if (s2 != null) {
            String k2 = "-" + s2.getId() + "-" + d;
            if (tI != null) tB.add(tI + k2); if (aI != null) rB.add(aI + k2); if (gK != null) gB.add(gK + k2);
        }
    }

    private boolean checkAvailability(Integer tI, Integer sI, Integer aI, String gK, SchoollHors s1, SchoollHors s2, LocalDate d, Set<String> tB, Set<String> rB, Set<String> gB) {
        String k1 = "-" + s1.getId() + "-" + d;
        String k2 = (s2 != null) ? "-" + s2.getId() + "-" + d : null;
        if (gB.contains(gK + k1) || (k2 != null && gB.contains(gK + k2))) return false;
        if (tB.contains(tI + k1) || (k2 != null && tB.contains(tI + k2))) return false;
        if (sI != null && (tB.contains(sI + k1) || (k2 != null && tB.contains(sI + k2)))) return false;
        if (aI != null && (rB.contains(aI + k1) || (k2 != null && rB.contains(aI + k2)))) return false;
        return true;
    }

    private List<Classrooms> fetchAllAulas() { return classroomUseCase.getAllClassrooms().stream().map(dto -> { Classrooms c = new Classrooms(); c.setIdAula(dto.getIdAula()); c.setNombre(dto.getNombre()); return c; }).collect(Collectors.toList()); }
    
    private List<SchoollHors> fetchSortedSlots() { 
        List<SchoollHors> slots = schoolHoursService.getAllSchoolHours().stream()
            .map(dto -> new SchoollHors(dto.getId().longValue(), dto.getPeriodNumber(), dto.getStartTime(), dto.getEndTime(), false, ""))
            .sorted(Comparator.comparing(SchoollHors::getStartTime))
            .collect(Collectors.toList());
        
        if (slots.isEmpty()) {
            log.warn("DEBUG: No se encontraron Slots en BD. Generando Slots en memoria (08:00 - 18:00)...");
            java.time.LocalTime time = java.time.LocalTime.of(8, 0);
            for (int i = 8; i <= 18; i++) {
                slots.add(new SchoollHors((long)i, i, time, time.plusHours(1), false, "Generated " + i));
                time = time.plusHours(1);
            }
        }
        return slots;
    }

    private Map<String, Integer> fetchSubjectMap() { return subjectRepository.findAll().stream().collect(Collectors.toMap(Subject::getNombre, Subject::getIdMateria, (e, r) -> e)); }
    private boolean isAlreadyProcessed(List<Schedule> list, String cM, String cG) { return list.stream().anyMatch(s -> s.getGrupo().equals(cG) && s.getClaveMateria().equals(cM)); }
}