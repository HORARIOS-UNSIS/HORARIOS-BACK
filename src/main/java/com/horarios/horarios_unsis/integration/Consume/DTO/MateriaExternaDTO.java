package com.horarios.horarios_unsis.integration.Consume.DTO;

/**
 * DTO para datos de materias desde API externa
 */
public class MateriaExternaDTO {
    
    private Integer idMateria;
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
