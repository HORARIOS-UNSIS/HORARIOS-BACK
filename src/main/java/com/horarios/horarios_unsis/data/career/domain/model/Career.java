package com.horarios.horarios_unsis.data.career.domain.model;

public class Career {
    private Integer idCarrera;
    private String clave;
    private String nombre;
    private Boolean vigente;

    public Career() {
    }

    public Career(Integer idCarrera, String clave, String nombre, Boolean vigente) {
        this.idCarrera = idCarrera;
        this.clave = clave;
        this.nombre = nombre;
        this.vigente = vigente;
    }

    public Integer getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(Integer idCarrera) {
        this.idCarrera = idCarrera;
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

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }
}
