package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfesorExternoDTO {
    
    @JsonProperty("idProfesor")
    private Integer idProfesor;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("sabatico")
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
}
