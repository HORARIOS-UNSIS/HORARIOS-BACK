package com.horarios.horarios_unsis.shared;

/**
 * Constantes compartidas para módulo de exámenes
 * 
 * NOTA: Las Academias son dinámicas y vienen del frontend como:
 * - esAcademia: boolean (true/false)
 * - idAcademia: Integer (ID de la academia si aplica)
 * NO se definen como constantes porque varían según negocio.
 * 
 * Las Áreas también son dinámicas (vienen desde BD de materias).
 */
public final class ExamConstants {

    private ExamConstants() {
        // Constructor privado para evitar instanciación
    }

    // ========== TIPOS DE EXAMEN ==========
    public static final String TIPO_PARCIAL = "PARCIAL";
    public static final String TIPO_ORDINARIO = "ORDINARIO";
    public static final String TIPO_EXTRAORDINARIO = "EXTRAORDINARIO";
    public static final String TIPO_ESPECIAL = "ESPECIAL";

    // ========== DURACIONES DE EXAMEN (en minutos) ==========
    public static final int DURACION_PARCIAL = 60;              // 1 hora
    public static final int DURACION_ORDINARIO = 120;           // 2 horas (TODOS los ordinarios, cualquier área)
    public static final int DURACION_EXTRAORDINARIO = 120;      // 2 horas

    // ========== ESTADOS DE SCHEDULE ==========
    public static final String STATUS_PROGRAMADO = "PROGRAMADO";
    public static final String STATUS_CONFIRMADO = "CONFIRMADO";
    public static final String STATUS_CANCELADO = "CANCELADO";

    // ========== CONFIGURACIÓN DE BÚSQUEDA DE HORAS ÓPTIMAS ==========
    // (Solo aplica para exámenes de ACADEMIA - esAcademia = true)
    public static final int PESO_CLASE_AFECTADA = 10;           // Penalidad por clase afectada
    public static final int PENALIDAD_INGLES = 20;              // Penalidad si afecta inglés
    public static final int BONUS_SALA = 5;                     // Bonus por usar sala

    // ========== VALIDACIONES ==========
    public static final int MIN_SINODALES_REQUERIDOS = 1;       // Todos los tipos requieren al menos 1 sinodal
}
