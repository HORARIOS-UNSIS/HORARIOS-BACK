#!/bin/bash

# ============================================================================
# SCRIPT PARA INSERTAR DATOS DE PRUEBA EN LA BASE DE DATOS
# ============================================================================
# Este script ejecuta el archivo SQL seeddata.sql en PostgreSQL
# Requiere que docker-compose esté ejecutando

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SQL_FILE="$SCRIPT_DIR/src/main/resources/db/seeddata.sql"
CONTAINER_NAME="horarios-db"
DB_NAME="prueba_examen_horario"
DB_USER="postgres"
DB_PASSWORD="root"

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║  INSERCIÓN DE DATOS DE PRUEBA - HORARIOS UNSIS              ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Verificar que el archivo SQL existe
if [ ! -f "$SQL_FILE" ]; then
    echo "❌ Error: No se encontró el archivo $SQL_FILE"
    exit 1
fi

echo "📋 Archivo SQL: $SQL_FILE"
echo "🐳 Contenedor: $CONTAINER_NAME"
echo "💾 Base de datos: $DB_NAME"
echo ""

# Verificar que docker y el contenedor existen
if ! command -v docker &> /dev/null; then
    echo "❌ Error: Docker no está instalado"
    exit 1
fi

# Esperar a que la BD esté lista
echo "⏳ Esperando a que PostgreSQL esté listo..."
sleep 2

# Ejecutar el archivo SQL en el contenedor
echo "📥 Insertando datos en la base de datos..."
cat "$SQL_FILE" | docker exec -i "$CONTAINER_NAME" psql -U "$DB_USER" -d "$DB_NAME" 2>&1

if [ ${PIPESTATUS[0]} -eq 0 ]; then
    echo ""
    echo "✅ Datos insertados exitosamente"
    echo ""
    echo "📊 RESUMEN DE REGISTROS INSERTADOS:"
    echo "   • 20 profesores"
    echo "   • 20 materias"
    echo "   • 20 aulas"
    echo "   • 9 horarios (períodos + recesos)"
    echo "   • 20 asignaciones profesor-materia"
    echo "   • 20 asignaciones sinodales"
    echo ""
    echo "🔍 Para verificar los datos, ejecuta:"
    echo "   docker exec -it horarios-db psql -U postgres -d horarios_unsis -c \"SELECT COUNT(*) as total FROM profesor;\""
else
    echo ""
    echo "❌ Error al insertar datos"
    exit 1
fi
