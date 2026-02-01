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

    private List<Schedule> generateExams(ScheduleGeneralRequest request, String type, LocalDate startDate, LocalDate endDate) {
        log.info("DEBUG: Iniciando proceso para '{}'", request.getLicenciatura());

        // 1. Obtener Clave Base (ej: "06")
        String claveBase = careerRepository.findAll().stream()
                .filter(c -> c.getNombre().trim().equalsIgnoreCase(request.getLicenciatura().trim()))
                .map(Career::getClave).findFirst().orElse(null);

        if (claveBase == null) {
            log.error("DEBUG: La carrera '{}' no existe en DB", request.getLicenciatura());
            return new ArrayList<>();
        }

        // 2. Filtro FLEXIBLE de Asignaciones
        List<TeacherSubjectAssignmentEntity> asignaciones = assignmentRepository.findAll().stream()
                .filter(a -> a.getClaveCarrera() != null && a.getClaveCarrera().trim().startsWith(claveBase.trim())) // Cambio a startsWith para captar "06B"
                .filter(a -> a.getClavePeriodo().trim().equalsIgnoreCase(request.getPeriodo().trim()))
                .filter(a -> request.getGrupos() == null || request.getGrupos().isEmpty() || request.getGrupos().contains(a.getClaveGrupo()))
                .collect(Collectors.toList());

        log.info("DEBUG: Clave Base: {}, Asignaciones encontradas: {}", claveBase, asignaciones.size());

        if (asignaciones.isEmpty()) return new ArrayList<>();

        // 3. Carga de Recursos
        List<Teacher> claustro = teacherRepository.findAll();
        List<Classrooms> aulas = fetchAllAulas();
        List<SchoollHors> slots = fetchSortedSlots();
        Map<String, Integer> subjectMap = fetchSubjectMap();

        Set<String> teacherBusy = new HashSet<>();
        Set<String> roomBusy = new HashSet<>();
        Set<String> groupBusy = new HashSet<>();
        List<Schedule> result = new ArrayList<>();
        int idTipo = type.equalsIgnoreCase("PARCIAL") ? 1 : 2;

        // 4. Algoritmo de Generación
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (TeacherSubjectAssignmentEntity asig : asignaciones) {
                if (isAlreadyProcessed(result, asig.getClaveMateria(), asig.getClaveGrupo())) continue;

                Teacher titular = claustro.stream()
                        .filter(t -> t.getIdProfesor().equals(asig.getIdProfesor()))
                        .findFirst().orElse(null);
                
                if (titular == null) continue;

                String groupKey = asig.getClaveCarrera() + "-" + asig.getClaveGrupo();

                if (type.equalsIgnoreCase("PARCIAL")) {
                    processParcial(result, asig, titular, slots, aulas, date, idTipo, groupKey, teacherBusy, roomBusy, groupBusy, subjectMap);
                } else {
                    processOrdinario(result, asig, titular, slots, aulas, claustro, date, idTipo, groupKey, teacherBusy, roomBusy, groupBusy, subjectMap);
                }
            }
        }
        return result;
    }

    private void processParcial(List<Schedule> res, TeacherSubjectAssignmentEntity asig, Teacher tit, List<SchoollHors> slots, List<Classrooms> aulas, LocalDate d, int idT, String gK, Set<String> tB, Set<String> rB, Set<String> gB, Map<String, Integer> sM) {
        slots.stream()
            .filter(s -> s.getPeriodNumber() == asig.getHora())
            .findFirst().ifPresent(slot -> {
                if (checkAvailability(tit.getIdProfesor(), null, aulas.get(0).getIdAula(), gK, slot, null, d, tB, rB, gB)) {
                    saveAndMark(res, asig, tit, null, aulas.get(0), slot, null, d, idT, gK, tB, rB, gB, sM);
                }
            });
    }

    private void processOrdinario(List<Schedule> res, TeacherSubjectAssignmentEntity asig, Teacher tit, List<SchoollHors> slots, List<Classrooms> aulas, List<Teacher> claustro, LocalDate d, int idT, String gK, Set<String> tB, Set<String> rB, Set<String> gB, Map<String, Integer> sM) {
        for (int i = 0; i < slots.size() - 1; i++) {
            SchoollHors s1 = slots.get(i);
            SchoollHors s2 = slots.get(i + 1);
            if (!s1.getEndTime().equals(s2.getStartTime())) continue;

            Teacher sinodal = claustro.stream()
                    .filter(t -> !t.getIdProfesor().equals(tit.getIdProfesor()))
                    .findFirst().orElse(null);

            Classrooms aula = aulas.stream()
                    .filter(a -> !rB.contains(a.getIdAula() + "-" + s1.getId() + "-" + d))
                    .findFirst().orElse(null);

            if (sinodal != null && aula != null && checkAvailability(tit.getIdProfesor(), sinodal.getIdProfesor(), aula.getIdAula(), gK, s1, s2, d, tB, rB, gB)) {
                saveAndMark(res, asig, tit, sinodal, aula, s1, s2, d, idT, gK, tB, rB, gB, sM);
                break;
            }
        }
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
    private List<SchoollHors> fetchSortedSlots() { return schoolHoursService.getAllSchoolHours().stream().map(dto -> new SchoollHors(dto.getId().longValue(), dto.getPeriodNumber(), dto.getStartTime(), dto.getEndTime(), false, "")).sorted(Comparator.comparing(SchoollHors::getStartTime)).collect(Collectors.toList()); }
    private Map<String, Integer> fetchSubjectMap() { return subjectRepository.findAll().stream().collect(Collectors.toMap(Subject::getNombre, Subject::getIdMateria, (e, r) -> e)); }
    private boolean isAlreadyProcessed(List<Schedule> list, String cM, String cG) { return list.stream().anyMatch(s -> s.getGrupo().equals(cG) && s.getClaveMateria().equals(cM)); }
}