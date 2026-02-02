package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para datos de materia desde API externa
 * Puede venir anidado en el objeto de horario o como respuesta directa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MateriaExternaDTO {
    
    @JsonProperty("idMateria")
    @JsonAlias({"id", "clave", "id_materia"})
    private Integer idMateria;
    
    @JsonProperty("nombre")
    @JsonAlias({"nombreMateria", "name", "descripcion"})
    private String nombre;

    @JsonProperty("esAcademia")
    @JsonAlias({"es_academia", "academia"})
    private Boolean esAcademia;

    public MateriaExternaDTO() {
    }

    public MateriaExternaDTO(Integer idMateria, String nombre) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.esAcademia = false;
    }

    public MateriaExternaDTO(Integer idMateria, String nombre, Boolean esAcademia) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.esAcademia = esAcademia;
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

    public Boolean getEsAcademia() {
        return esAcademia;
    }

    public void setEsAcademia(Boolean esAcademia) {
        this.esAcademia = esAcademia;
    }
    
    @Override
    public String toString() {
        return "MateriaExternaDTO{" +
                "idMateria=" + idMateria +
                ", nombre='" + nombre + '\'' +
                ", esAcademia=" + esAcademia +
                '}';
    }
}
