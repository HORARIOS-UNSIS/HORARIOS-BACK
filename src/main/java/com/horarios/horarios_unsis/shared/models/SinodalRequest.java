package com.horarios.horarios_unsis.shared.models;

/**
 * Modelo para representar un sinodal en la solicitud de examen
 * El frontend puede enviar NOMBRE O ID (prioridad: ID si está disponible)
 */
public class SinodalRequest {
    
    private Integer idProfesor;        // ID del profesor sinodal (si lo tiene)
    private String nombreProfesor;     // Nombre del profesor sinodal (si no tiene ID)

    public SinodalRequest() {
    }

    public SinodalRequest(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public SinodalRequest(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public SinodalRequest(Integer idProfesor, String nombreProfesor) {
        this.idProfesor = idProfesor;
        this.nombreProfesor = nombreProfesor;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    /**
     * Validación: Al menos ID o nombre debe estar presente
     */
    public boolean esValido() {
        return (idProfesor != null) || (nombreProfesor != null && !nombreProfesor.trim().isEmpty());
    }

    @Override
    public String toString() {
        return "SinodalRequest{" +
                "idProfesor=" + idProfesor +
                ", nombreProfesor='" + nombreProfesor + '\'' +
                '}';
    }
}
