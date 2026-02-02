package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para datos de profesor desde API externa
 * Puede venir anidado en el objeto de horario o como respuesta directa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfesorExternoDTO {
    
    @JsonProperty("idProfesor")
    @JsonAlias({"id", "clave", "id_profesor"})
    private Integer idProfesor;
    
    @JsonProperty("nombre")
    @JsonAlias({"nombreCompleto", "name"})
    private String nombre;
    
    @JsonProperty("sabatico")
    @JsonAlias({"esSabatico", "en_sabatico"})
    private Boolean sabatico;

    public ProfesorExternoDTO() {
    }

    public ProfesorExternoDTO(Integer idProfesor, String nombre, Boolean sabatico) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.sabatico = sabatico;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getSabatico() {
        return sabatico;
    }

    public void setSabatico(Boolean sabatico) {
        this.sabatico = sabatico;
    }
    
    @Override
    public String toString() {
        return "ProfesorExternoDTO{" +
                "idProfesor=" + idProfesor +
                ", nombre='" + nombre + '\'' +
                ", sabatico=" + sabatico +
                '}';
    }
}
