# HORARIOS UNSIS — Backend

API REST para gestionar catálogos universitarios y programar exámenes de la Universidad de la Sierra Sur. Centraliza usuarios, profesores, materias, aulas, grupos y bloques horarios, e integra información del sistema universitario.

## Funcionalidades

- Autenticación de usuarios mediante JWT y contraseñas BCrypt.
- Administración de usuarios con roles `ADMIN`, `JEFE` y `SERV`.
- Creación, consulta, actualización y eliminación de profesores, materias, aulas y bloques horarios.
- Consulta de grupos y materias por carrera y período.
- Creación, consulta, actualización y eliminación de exámenes.
- Generación de exámenes por licenciatura, grupos, período y rango de fechas.
- Consulta de exámenes por materia, profesor, período, estado y fechas.
- Registro y eliminación de sinodales, y consulta de sus asignaciones por carrera y período.
- Sincronización de catálogos y asignaciones profesor–materia desde la API universitaria.
- Documentación interactiva de la API con Swagger UI.

## Tecnologías

| Componente | Uso |
| --- | --- |
| Java 21 | Lenguaje y entorno de ejecución |
| Spring Boot 3.5.7 | Aplicación y API REST |
| Spring Security | Autenticación y permisos por rol |
| JJWT 0.12.3 | Tokens JWT |
| Spring Data JPA / Hibernate | Persistencia de datos |
| PostgreSQL | Base de datos |
| Springdoc OpenAPI 2.6.0 | Swagger UI y especificación OpenAPI |
| Maven Wrapper 3.9.11 | Compilación y gestión de dependencias |
| Lombok | Generación de código |

Las dependencias se encuentran en [pom.xml](pom.xml).

## Configuración y ejecución

Se requiere JDK 21 y una base de datos PostgreSQL. El proyecto incluye Maven Wrapper para descargar Maven y las dependencias.

Crea la base de datos, por ejemplo desde `psql`:

```sql
CREATE DATABASE horarios_unsis;
```

Copia la plantilla de configuración:

```bash
cp .env.example .env
```

Edita `.env` con los datos de conexión y un secreto JWT propio:

| Variable | Descripción | Valor predeterminado |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | URL JDBC de PostgreSQL | Configurar |
| `SPRING_DATASOURCE_USERNAME` | Usuario de PostgreSQL | Configurar |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de PostgreSQL | Configurar |
| `JWT_SECRET` | Secreto de firma JWT, de al menos 32 bytes | Configurar |
| `SERVER_PORT` | Puerto HTTP del backend | `8088` |
| `APP_JWT_EXPIRATION` | Vigencia del token en milisegundos | `86400000` |
| `INTEGRATION_EXTERNAL_ENABLED` | Activar integración universitaria | `false` |
| `API_EXTERNAL_BASEURL` | Dirección de la API universitaria | `http://serv-horarios.unsis.lan` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Administración del esquema por Hibernate | `update` |

Carga las variables en Bash e inicia la aplicación:

```bash
set -a
source .env
set +a
bash mvnw spring-boot:run
```

En Windows, configura las variables en la terminal o el IDE y ejecuta `mvnw.cmd spring-boot:run`.

La configuración de Spring Boot está en [application.properties](src/main/resources/application.properties). Hibernate crea o actualiza las tablas al iniciar con `ddl-auto=update`.

## Swagger y acceso a la API

Con el backend iniciado en el puerto predeterminado:

| Dirección | Contenido |
| --- | --- |
| `http://localhost:8088/swagger-ui.html` | Documentación interactiva Swagger UI |
| `http://localhost:8088/api-docs` | Especificación OpenAPI en JSON |
| `http://localhost:8088/api` | Información del servicio; requiere JWT |
| `http://localhost:8088/api/health` | Estado e información básica del servicio; requiere JWT |

Swagger UI permite consultar los endpoints, sus parámetros y los cuerpos de las solicitudes. Usa **Authorize** con el token JWT para acceder a las operaciones protegidas.

## Autenticación y roles

Los usuarios se almacenan en la tabla `usuario`, con sus contraseñas codificadas mediante BCrypt. Para acceder, utiliza las credenciales de un usuario activo:

```bash
curl -X POST http://localhost:8088/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"tu_usuario","password":"tu_contrasena"}'
```

La respuesta contiene `token`, `type`, `username`, `role` e `idUsuario`. Envía el token en las siguientes solicitudes:

```bash
curl http://localhost:8088/api/schedules \
  -H 'Authorization: Bearer TU_TOKEN'
```

| Recurso | Roles con acceso |
| --- | --- |
| Usuarios | `ADMIN` |
| Profesores, materias y aulas | `ADMIN` y `JEFE`; creación solo `ADMIN` |
| Bloques horarios | Lectura: `ADMIN` y `JEFE`; escritura e importación: `ADMIN` |
| Exámenes y generación | `ADMIN`, `JEFE` y `SERV`; importación solo `ADMIN` |
| Sinodales y grupos | `ADMIN` y `JEFE` |
| Sincronización de catálogos (`/api/sync`) | `ADMIN` |
| Asignaciones (`/api/asignaciones`) | Usuario autenticado |

Para preparar el primer administrador en una base nueva, inserta un usuario en `usuario` después del arranque inicial, utilizando un hash generado con `BCryptPasswordEncoder.encode(...)`:

```sql
INSERT INTO usuario (nombre, email, username, password, rol, activo)
VALUES ('Administrador', 'admin@example.test', 'admin',
        '<HASH_BCRYPT>', 'ADMIN', true);
```

Los administradores pueden gestionar los demás usuarios mediante `/api/users`.

## Generación y gestión de exámenes

La generación utiliza los catálogos de carreras, profesores, materias, aulas, bloques horarios y asignaciones profesor–materia–grupo–período.

`POST /api/schedules/generate` recibe una solicitud como esta, con valores correspondientes a los catálogos cargados:

```json
{
  "licenciatura": "Informatica",
  "grupos": ["101"],
  "periodo": "2025-2026A",
  "tipo": "PARCIAL",
  "start": "2026-11-02",
  "end": "2026-11-06"
}
```

El servicio busca la licenciatura por nombre, selecciona las asignaciones y recorre el rango de fechas. Para parciales utiliza el bloque de la asignación; para ordinarios busca dos bloques contiguos y selecciona un profesor adicional como sinodal durante el cálculo. Los exámenes generados se guardan directamente con estado `PENDING`.

También se pueden registrar exámenes individualmente mediante `POST /api/schedules` y editarlos con `PUT /api/schedules/{id}`:

```json
{
  "idMateria": 1,
  "idAula": 1,
  "idHorario": 1,
  "idTipo": 1,
  "idPeriodo": 1,
  "profesorId": 1,
  "fecha": "2026-11-02",
  "grupo": "101",
  "status": "PENDING"
}
```

Los identificadores del ejemplo se sustituyen por los correspondientes a la base de datos.

## Integración universitaria

Para consumir la API universitaria, configura estas variables antes de iniciar el backend:

```bash
export INTEGRATION_EXTERNAL_ENABLED=true
export API_EXTERNAL_BASEURL='http://serv-horarios.unsis.lan'
```

La sincronización general se ejecuta con un token de administrador:

```bash
curl -X POST http://localhost:8088/api/sync/all \
  -H 'Authorization: Bearer TU_TOKEN'
```

El proceso sincroniza períodos, carreras, grupos y aulas; obtiene profesores y materias a partir de horarios y sincroniza las asignaciones del período seleccionado por el servicio. La respuesta incluye un mensaje y contadores de registros procesados.

También se puede sincronizar cada catálogo mediante `POST /api/sync/periods`, `/api/sync/careers`, `/api/sync/groups`, `/api/sync/teachers`, `/api/sync/subjects` y `/api/sync/classrooms`.

Las asignaciones de un período concreto se sincronizan con `POST /api/asignaciones/sincronizar/{clavePeriodo}`.

## Endpoints principales

Para los recursos con CRUD se utilizan `GET` y `POST` sobre la ruta base, y `GET`, `PUT` y `DELETE` sobre `/{id}`.

| Ruta | Función |
| --- | --- |
| `POST /api/auth/login` | Inicio de sesión |
| `/api/users` | CRUD de usuarios |
| `/api/teachers` | CRUD de profesores |
| `/api/subjects` | CRUD de materias y filtros por `claveCarrera` y `clavePeriodo` |
| `/api/classrooms` | CRUD de aulas |
| `/api/school-hours` | CRUD de bloques horarios |
| `GET /api/groups` | Consulta de grupos |
| `GET /api/groups/{id}` | Consulta de un grupo |
| `/api/schedules` | CRUD de exámenes |
| `POST /api/schedules/generate` | Generación de exámenes |
| `POST /api/schedules/importar` | Importación de horarios externos |
| `GET /api/schedules/materia/{idMateria}` | Exámenes por materia |
| `GET /api/schedules/profesor/{profesorId}` | Exámenes por profesor |
| `GET /api/schedules/periodo/{idPeriodo}` | Exámenes por período |
| `GET /api/schedules/status/{status}` | Exámenes por estado |
| `GET /api/schedules/rango?inicio=2026-11-02&fin=2026-11-06` | Exámenes por rango de fechas |
| `GET /api/schedules/filtrar?claveCarrera=06&clavePeriodo=2025-2026A` | Exámenes por carrera y período |
| `POST /api/synodals` | Registro de sinodal |
| `DELETE /api/synodals/{id}` | Eliminación de sinodal |
| `GET /api/synodals/assignments` | Consulta de asignaciones de sinodales |
| `/api/asignaciones` | Sincronización y consulta de asignaciones |
| `/api/sync` | Sincronización de catálogos |

## Organización del proyecto

```text
src/main/java/com/horarios/horarios_unsis/
├── HorariosUnsisApplication.java   # Inicio de la aplicación
├── config/                        # Seguridad, JWT, BCrypt y Swagger
├── data/                          # Usuarios, catálogos y asignaciones
│   └── <modulo>/
│       ├── application/            # DTO, mappers y servicios
│       ├── domain/                 # Modelos, puertos y servicios
│       └── infrastructure/         # Controladores y persistencia
├── schedule/                      # Gestión y generación de exámenes
├── integration/
│   ├── Consume/                   # Clientes de la API universitaria
│   └── sync/                      # Servicios de sincronización
└── shared/                        # Componentes compartidos
src/main/resources/
├── application.properties         # Configuración de Spring Boot
└── db/seeddata.sql                 # Datos de ejemplo
```

Las solicitudes pasan de los controladores a los servicios y repositorios. Los DTO representan los datos de entrada y salida, los mappers convierten los objetos y las entidades JPA representan las tablas de PostgreSQL.

## Compilación

Para generar el archivo JAR, con las variables de entorno configuradas y PostgreSQL disponible:

```bash
bash mvnw clean package
```

Para ejecutar el archivo generado:

```bash
java -jar target/horarios-unsis-0.0.1-SNAPSHOT.jar
```
