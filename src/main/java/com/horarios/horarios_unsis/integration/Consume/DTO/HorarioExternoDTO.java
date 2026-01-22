package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO para datos de horarios desde API externa
 * Estructura esperada: GET /api/horarios/{periodo}/{idprofesor}
 * Incluye: materia, profesor, aula, tiempo, fecha
 */
public class HorarioExternoDTO {
    
    @JsonProperty("id")
    private Integer idHorario;
    
    @JsonProperty("id_materia")
    private Integer idMateria;
    
    @JsonProperty("materia")
    private MateriaExternaDTO materia;
    
    @JsonProperty("id_profesor")
    private Integer idProfesor;
    
    @JsonProperty("profesor")
    private ProfesorExternoDTO profesor;
    
    @JsonProperty("id_aula")
    private Integer idAula;
    
    @JsonProperty("aula")
    private AulaExternaDTO aula;
    
    @JsonProperty("id_bloque")
    private Integer idBloque;
    
    @JsonProperty("numero_bloque")
    private Integer numeroBloque;
    
    @JsonProperty("hora_inicio")
    private LocalTime horaInicio;
    
    @JsonProperty("hora_fin")
    private LocalTime horaFin;
    
    @JsonProperty("es_descanso")
    private Boolean esDescanso;
    
    @JsonProperty("descripcion_bloque")
    private String descripcionBloque;
    
    @JsonProperty("fecha")
    private LocalDate fecha;
    
    @JsonProperty("grupo")
    private String grupo;
    
    @JsonProperty("id_grupo")
    private Integer idGrupo;

    public HorarioExternoDTO() {
    }

    // Getters y Setters
    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public MateriaExternaDTO getMateria() {
        return materia;
    }

    public void setMateria(MateriaExternaDTO materia) {
        this.materia = materia;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public ProfesorExternoDTO getProfesor() {
        return profesor;
    }

    public void setProfesor(ProfesorExternoDTO profesor) {
        this.profesor = profesor;
    }

    public Integer getIdAula() {
        return idAula;
    }

    public void setIdAula(Integer idAula) {
        this.idAula = idAula;
    }

    public AulaExternaDTO getAula() {
        return aula;
    }

    public void setAula(AulaExternaDTO aula) {
        this.aula = aula;
    }

    public Integer getIdBloque() {
        return idBloque;
    }

    public void setIdBloque(Integer idBloque) {
        this.idBloque = idBloque;
    }

    public Integer getNumeroBloque() {
        return numeroBloque;
    }

    public void setNumeroBloque(Integer numeroBloque) {
        this.numeroBloque = numeroBloque;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getEsDescanso() {
        return esDescanso;
    }

    public void setEsDescanso(Boolean esDescanso) {
        this.esDescanso = esDescanso;
    }

    public String getDescripcionBloque() {
        return descripcionBloque;
    }

    public void setDescripcionBloque(String descripcionBloque) {
        this.descripcionBloque = descripcionBloque;
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

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    @Override
    public String toString() {
        return "HorarioExternoDTO{" +
                "idHorario=" + idHorario +
                ", idMateria=" + idMateria +
                ", idProfesor=" + idProfesor +
                ", idAula=" + idAula +
                ", numeroBloque=" + numeroBloque +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", fecha=" + fecha +
                ", grupo='" + grupo + '\'' +
                '}';
    }
}
