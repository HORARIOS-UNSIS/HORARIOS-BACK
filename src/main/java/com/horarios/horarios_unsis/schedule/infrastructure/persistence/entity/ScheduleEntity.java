package com.horarios.horarios_unsis.schedule.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

// Entidad de persistencia para exámenes (JPA)
@Entity
@Table(name = "examen")
public class ScheduleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_examen")
	private Integer idExamen;

	@Column(name = "id_materia", nullable = false)
	private Integer idMateria;

	@Column(name = "id_aula", nullable = false)
	private Integer idAula;

	@Column(name = "id_horario", nullable = false)
	private Integer idHorario;

	@Column(name = "id_tipo", nullable = false)
	private Integer idTipo;

	@Column(name = "id_periodo", nullable = false)
	private Integer idPeriodo;

	@Column(name = "profesor_id", nullable = false)
	private Integer profesorId;

	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;

	@Column(name = "grupo", length = 50)
	private String grupo;

	@Column(name = "status", length = 50)
	private String status;

	@Column(name = "clave_materia", length = 50)
	private String claveMateria;

	@Column(name = "nombre_aula", length = 100)
	private String nombreAula;

	@Column(name = "nombre_materia", length = 200)
	private String nombreMateria;

	@Column(name = "nombre_profesor", length = 200)
	private String nombreProfesor;

	@Column(name = "en_horario_oficial")
	private Boolean enHorarioOficial;

	@Column(name = "hora_fin")
	private LocalTime horaFin;

	@Column(name = "hora_inicio")
	private LocalTime horaInicio;

	@Column(name = "is_locked")
	private Boolean isLocked;

	// Constructor requerido por JPA
	public ScheduleEntity() {
	}

	public ScheduleEntity(Integer idExamen, Integer idMateria, Integer idAula, Integer idHorario, 
	                      Integer idTipo, Integer idPeriodo, Integer profesorId, 
	                      LocalDate fecha, String grupo, String status) {
		this.idExamen = idExamen;
		this.idMateria = idMateria;
		this.idAula = idAula;
		this.idHorario = idHorario;
		this.idTipo = idTipo;
		this.idPeriodo = idPeriodo;
		this.profesorId = profesorId;
		this.fecha = fecha;
		this.grupo = grupo;
		this.status = status;
	}

	public Integer getIdExamen() {
		return idExamen;
	}

	public void setIdExamen(Integer idExamen) {
		this.idExamen = idExamen;
	}

	public Integer getIdMateria() {
		return idMateria;
	}

	public void setIdMateria(Integer idMateria) {
		this.idMateria = idMateria;
	}

	public Integer getIdAula() {
		return idAula;
	}

	public void setIdAula(Integer idAula) {
		this.idAula = idAula;
	}

	public Integer getIdHorario() {
		return idHorario;
	}

	public void setIdHorario(Integer idHorario) {
		this.idHorario = idHorario;
	}

	public Integer getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}

	public Integer getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(Integer idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public Integer getProfesorId() {
		return profesorId;
	}

	public void setProfesorId(Integer profesorId) {
		this.profesorId = profesorId;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getClaveMateria() {
		return claveMateria;
	}

	public void setClaveMateria(String claveMateria) {
		this.claveMateria = claveMateria;
	}

	public String getNombreAula() {
		return nombreAula;
	}

	public void setNombreAula(String nombreAula) {
		this.nombreAula = nombreAula;
	}

	public String getNombreMateria() {
		return nombreMateria;
	}

	public void setNombreMateria(String nombreMateria) {
		this.nombreMateria = nombreMateria;
	}

	public String getNombreProfesor() {
		return nombreProfesor;
	}

	public void setNombreProfesor(String nombreProfesor) {
		this.nombreProfesor = nombreProfesor;
	}

	public Boolean getEnHorarioOficial() {
		return enHorarioOficial;
	}

	public void setEnHorarioOficial(Boolean enHorarioOficial) {
		this.enHorarioOficial = enHorarioOficial;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}
}
