package com.horarios.horarios_unsis.integration.Consume.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para datos de horarios desde API externa
 * Endpoint: GET /api/horarios/{periodo}/aula/{idaula}
 * 
 * Estructura JSON real del API:
 * {
 *   "rowId": 27086,
 *   "idprofesor": "1134",
 *   "nombreCompleto": "DR. MAURICIO SOSA MONTES",
 *   "asignatura": "5032_2024",
 *   "idGrupo": "305",
 *   "idAula": "5",
 *   "dia": 1,
 *   "hora": 17,
 *   "carrera": "05C",
 *   "periodoq": "2526A",
 *   "materia": "MICROECONOMÍA",
 *   "nombreGrupo": "305",
 *   "nombreAula": "B2"
 * }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HorarioExternoDTO {
    
    @JsonProperty("rowId")
    private Integer rowId;
    
    @JsonProperty("idprofesor")
    private String idProfesorStr;
    
    @JsonProperty("nombreCompleto")
    private String nombreProfesor;
    
    @JsonProperty("asignatura")
    private String codigoAsignatura;
    
    @JsonProperty("idGrupo")
    private String idGrupoStr;
    
    @JsonProperty("idAula")
    private String idAulaStr;
    
    @JsonProperty("dia")
    private Integer dia;
    
    @JsonProperty("hora")
    private Integer hora;
    
    @JsonProperty("carrera")
    private String carrera;
    
    @JsonProperty("periodoq")
    private String periodo;
    
    @JsonProperty("materia")
    private String nombreMateria;
    
    @JsonProperty("nombreGrupo")
    private String nombreGrupo;
    
    @JsonProperty("nombreAula")
    private String nombreAula;

    public HorarioExternoDTO() {
    }

    // Métodos helper para obtener IDs como Integer (compatibilidad)
    // Marcados con @JsonIgnore para evitar conflictos con Jackson
    @JsonIgnore
    public Integer getIdProfesor() {
        try {
            return idProfesorStr != null ? Integer.parseInt(idProfesorStr) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    @JsonIgnore
    public Integer getIdAula() {
        try {
            return idAulaStr != null ? Integer.parseInt(idAulaStr) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    @JsonIgnore
    public Integer getIdGrupo() {
        try {
            return idGrupoStr != null ? Integer.parseInt(idGrupoStr) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    // Para compatibilidad con código existente
    @JsonIgnore
    public Integer getIdHorario() {
        return rowId;
    }
    
    @JsonIgnore
    public Integer getIdMateria() {
        // El código de asignatura puede tener formato "5032_2024", extraer el ID
        if (codigoAsignatura != null && codigoAsignatura.contains("_")) {
            try {
                return Integer.parseInt(codigoAsignatura.split("_")[0]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    @JsonIgnore
    public Integer getNumeroBloque() {
        return hora;
    }
    
    @JsonIgnore
    public Integer getIdBloque() {
        return hora;
    }
    
    @JsonIgnore
    public String getGrupo() {
        return nombreGrupo;
    }

    // Getters y Setters
    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getIdProfesorStr() {
        return idProfesorStr;
    }

    public void setIdProfesorStr(String idProfesorStr) {
        this.idProfesorStr = idProfesorStr;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public String getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public void setCodigoAsignatura(String codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

    public String getIdGrupoStr() {
        return idGrupoStr;
    }

    public void setIdGrupoStr(String idGrupoStr) {
        this.idGrupoStr = idGrupoStr;
    }

    public String getIdAulaStr() {
        return idAulaStr;
    }

    public void setIdAulaStr(String idAulaStr) {
        this.idAulaStr = idAulaStr;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getNombreAula() {
        return nombreAula;
    }

    public void setNombreAula(String nombreAula) {
        this.nombreAula = nombreAula;
    }

    @Override
    public String toString() {
        return "HorarioExternoDTO{" +
                "rowId=" + rowId +
                ", idProfesor='" + idProfesorStr + '\'' +
                ", nombreProfesor='" + nombreProfesor + '\'' +
                ", materia='" + nombreMateria + '\'' +
                ", grupo='" + nombreGrupo + '\'' +
                ", aula='" + nombreAula + '\'' +
                ", dia=" + dia +
                ", hora=" + hora +
                '}';
    }
}
