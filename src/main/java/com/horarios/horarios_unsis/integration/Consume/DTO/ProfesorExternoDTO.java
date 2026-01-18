package com.horarios.horarios_unsis.integration.Consume.DTO;

/**
 * DTO para datos de profesores desde API externa
 */
public class ProfesorExternoDTO {
    
    private Integer idProfesor;
    private String nombre;
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
