package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para datos de grupos desde API externa
 * Estructura esperada: GET /api/horarios/{periodo}/grupo/{idGrupo}
 */
public class GrupoExternoDTO {
    
    @JsonProperty("id")
    private Integer idGrupo;
    
    @JsonProperty("codigo")
    private String codigo;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("cantidad_estudiantes")
    private Integer cantidadEstudiantes;

    public GrupoExternoDTO() {
    }

    public GrupoExternoDTO(Integer idGrupo, String codigo, String nombre, Integer cantidadEstudiantes) {
        this.idGrupo = idGrupo;
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidadEstudiantes = cantidadEstudiantes;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidadEstudiantes() {
        return cantidadEstudiantes;
    }

    public void setCantidadEstudiantes(Integer cantidadEstudiantes) {
        this.cantidadEstudiantes = cantidadEstudiantes;
    }

    @Override
    public String toString() {
        return "GrupoExternoDTO{" +
                "idGrupo=" + idGrupo +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cantidadEstudiantes=" + cantidadEstudiantes +
                '}';
    }
}
