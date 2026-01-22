package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MateriaExternaDTO {
    
    @JsonProperty("idMateria")
    private Integer idMateria;
    
    @JsonProperty("nombre")
    private String nombre;

    public MateriaExternaDTO() {
    }

    public MateriaExternaDTO(Integer idMateria, String nombre) {
        this.idMateria = idMateria;
        this.nombre = nombre;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
