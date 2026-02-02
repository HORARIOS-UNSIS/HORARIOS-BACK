package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para datos de grupos desde API externa
 * Endpoint: GET /api/grupos/
 * 
 * Estructura JSON:
 * {
 *   "clave": "107A",
 *   "nombre": "107A",
 *   "carrera": "07",
 *   "semestre": 1,
 *   "alumnos": 30,
 *   "periodo": "1516A"
 * }
 */
public class GrupoExternoDTO {
    
    @JsonProperty("clave")
    private String clave;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("carrera")
    private String carrera;
    
    @JsonProperty("semestre")
    private Integer semestre;
    
    @JsonProperty("alumnos")
    private Integer alumnos;
    
    @JsonProperty("periodo")
    private String periodo;

    public GrupoExternoDTO() {
    }

    public GrupoExternoDTO(String clave, String nombre, String carrera, Integer semestre, Integer alumnos, String periodo) {
        this.clave = clave;
        this.nombre = nombre;
        this.carrera = carrera;
        this.semestre = semestre;
        this.alumnos = alumnos;
        this.periodo = periodo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Integer getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(Integer alumnos) {
        this.alumnos = alumnos;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "GrupoExternoDTO{" +
                "clave='" + clave + '\'' +
                ", nombre='" + nombre + '\'' +
                ", carrera='" + carrera + '\'' +
                ", semestre=" + semestre +
                ", alumnos=" + alumnos +
                ", periodo='" + periodo + '\'' +
                '}';
    }
}
