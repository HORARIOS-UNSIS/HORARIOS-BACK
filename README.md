# HORARIOS UNSIS — Backend

API REST para administrar catálogos universitarios y programar exámenes de la UNSIS. Permite autenticar usuarios, gestionar profesores, materias, aulas y bloques horarios, sincronizar información del sistema universitario y generar exámenes por carrera, grupo y período.

Este documento describe la rama **`dev2`**. El repositorio contiene el backend; no incluye una interfaz web de usuario. Las funcionalidades y limitaciones descritas se verificaron mediante lectura del código, sin ejecutar la aplicación.

## Tecnologías

| Componente | Versión o uso |
| --- | --- |
| Java | JDK 21 |
| Spring Boot | 3.5.7 |
| Maven Wrapper | Maven 3.9.11; no requiere instalar Maven por separado |
| PostgreSQL | Persistencia mediante Spring Data JPA / Hibernate |
| Spring Security y JJWT | Roles, contraseñas BCrypt y tokens JWT; JJWT 0.12.3 |
| Springdoc OpenAPI | 2.6.0, documentación interactiva con Swagger UI |
| Lombok | Generación de código durante la compilación |

Las dependencias y la versión del artefacto se definen en [pom.xml](pom.xml).

## Arranque local

### 1. Requisitos

- JDK 21 disponible mediante `java` y `JAVA_HOME`.
- PostgreSQL en ejecución y un usuario con permisos para crear y actualizar tablas en la base de desarrollo.
- Acceso a Internet para que el wrapper descargue Maven y las dependencias la primera vez.
- Acceso a la red universitaria únicamente si se habilita la integración externa.

Desde la raíz del repositorio, comprueba la rama y el JDK:

```bash
git switch dev2
java -version
```

### 2. Base de datos y configuración

Crea una base vacía en PostgreSQL, por ejemplo, ejecutando en `psql`:

```sql
CREATE DATABASE horarios_unsis;
```

Exporta estas variables en la misma terminal desde la que iniciarás el backend. Sustituye los valores de ejemplo:

```bash
export SPRING_DATASOURCE_URL='jdbc:postgresql://localhost:5432/horarios_unsis'
export SPRING_DATASOURCE_USERNAME='postgres'
export SPRING_DATASOURCE_PASSWORD='tu_clave_local'
export JWT_SECRET='reemplaza_esto_por_un_secreto_aleatorio_de_al_menos_32_bytes'
```

`JWT_SECRET` debe ser una clave propia, aleatoria y de al menos 32 bytes para la firma HMAC. No subas credenciales reales al repositorio. No hay un cargador de archivos `.env` configurado: crear ese archivo por sí solo no exporta las variables.

Como alternativa a los `export`, copia la plantilla [.env.example](.env.example) y edita sus valores:

```bash
cp .env.example .env
```

Después de editar `.env`, cárgalo en Bash antes de ejecutar Maven:

```bash
set -a
source .env
set +a
bash mvnw spring-boot:run
```

El archivo `.env` está excluido de Git; la plantilla `.env.example` sí se puede versionar.

| Variable | Obligatoria | Valor predeterminado / efecto |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | Sí | URL JDBC de PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Sí | Usuario de PostgreSQL |
| `SPRING_DATASOURCE_PASSWORD` | Sí | Contraseña de PostgreSQL |
| `JWT_SECRET` | Sí | Secreto utilizado por `app.jwt.secret` |
| `SERVER_PORT` | No | `8088` |
| `APP_JWT_EXPIRATION` | No | `86400000` milisegundos: 24 horas |
| `INTEGRATION_EXTERNAL_ENABLED` | No | `false`; habilita los clientes de la API universitaria |
| `API_EXTERNAL_BASEURL` | No | `http://serv-horarios.unsis.lan`; corresponde a `api.external.base-url` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | No | `update`; Hibernate modifica el esquema al arrancar |

La configuración base está en [application.properties](src/main/resources/application.properties). No hay migraciones versionadas de Flyway/Liquibase ni un perfil de pruebas con una base aislada.

### 3. Ejecutar

```bash
bash mvnw spring-boot:run
```

Se utiliza `bash mvnw` porque el script del repositorio no tiene permiso de ejecución. En Windows, utiliza `mvnw.cmd spring-boot:run` y configura las mismas variables en tu terminal.

Con la aplicación iniciada:

- API: `http://localhost:8088/api`
- Swagger UI: `http://localhost:8088/swagger-ui.html`
- Especificación OpenAPI: `http://localhost:8088/api-docs`
- Estado: `GET http://localhost:8088/api/health` (requiere JWT).

Swagger y el login son públicos. `/api` y `/api/health` están protegidos; recibir `401` sin token no significa que el servidor esté caído. El endpoint de salud devuelve información básica del servicio, no una comprobación de todas sus dependencias.

## Usuarios y autenticación

El login consulta la tabla `usuario`, verifica la contraseña con BCrypt y devuelve un JWT. Las propiedades `spring.security.user.*` del archivo de configuración **no crean un administrador en esa tabla**: la aplicación utiliza su propio `UserDetailsService`.

Para una base nueva necesitas provisionar el primer usuario `ADMIN` directamente en PostgreSQL, después de que Hibernate cree el esquema. Genera localmente el hash con `BCryptPasswordEncoder.encode(...)` y usa este modelo de inserción, reemplazando el marcador por el hash completo:

```sql
INSERT INTO usuario (nombre, email, username, password, rol, activo)
VALUES ('Administrador local', 'admin@example.test', 'admin',
        '<HASH_BCRYPT_GENERADO_LOCALMENTE>', 'ADMIN', true);
```

No insertes la contraseña en texto plano. Una vez autenticado ese administrador, puedes crear otros usuarios con `POST /api/users`.

```bash
curl -X POST http://localhost:8088/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"tu_contrasena"}'
```

La respuesta contiene `token`, `type`, `username`, `role` e `idUsuario`. Copia el token para las siguientes peticiones:

```bash
export TOKEN='token_devuelto_por_login'
curl http://localhost:8088/api/health -H "Authorization: Bearer $TOKEN"
```

En Swagger, utiliza **Authorize** con el JWT. No hay endpoints de renovación ni cierre de sesión implementados.

### Permisos efectivos

Los roles definidos son `ADMIN`, `JEFE` y `SERV`. Se combinan las reglas de [SecurityConfig](src/main/java/com/horarios/horarios_unsis/config/SecurityConfig.java) con las anotaciones de cada controlador.

| Recurso | Acceso |
| --- | --- |
| Usuarios | Solo `ADMIN` |
| Profesores, materias y aulas | `ADMIN` y `JEFE`; creación solo `ADMIN` |
| Bloques horarios | Lectura: `ADMIN` y `JEFE`; escritura e importación: `ADMIN` |
| Exámenes y generación | `ADMIN`, `JEFE`, `SERV`; importación solo `ADMIN` |
| Sinodales | `ADMIN` y `JEFE` |
| Grupos | `ADMIN` y `JEFE` entre los roles existentes |
| `/api/sync/**` | Solo `ADMIN` |
| `/api/asignaciones/**` | Usuario autenticado, incluida la sincronización |

Aunque la regla HTTP de bloques horarios incluye `SERV`, el controlador lo excluye. El controlador de grupos menciona `PROFESOR` y `ALUMNO`, pero esos valores no existen en el enum de roles.

## Flujo de trabajo

1. Configura PostgreSQL, arranca el servicio y prepara el administrador.
2. Carga catálogos: profesores, materias, aulas y bloques horarios. Para generar exámenes también hacen falta carreras y asignaciones profesor–materia–grupo–período coherentes.
3. Sincroniza los datos universitarios si tienes acceso a la API externa; también puedes trabajar con datos locales previamente preparados.
4. Consulta grupos y materias para seleccionar carrera y período.
5. Genera exámenes o créalos individualmente.
6. Consulta los exámenes guardados y ajusta los registros mediante la API.

### Sincronización externa

Actívala antes de iniciar el proceso:

```bash
export INTEGRATION_EXTERNAL_ENABLED=true
export API_EXTERNAL_BASEURL='http://serv-horarios.unsis.lan'
```

Con el backend iniciado y un token `ADMIN`:

```bash
curl -X POST http://localhost:8088/api/sync/all \
  -H "Authorization: Bearer $TOKEN"
```

`DataSyncService` sincroniza períodos, carreras, grupos y aulas; obtiene profesores y materias a partir de horarios y después sincroniza asignaciones del período que determina como actual. La operación permite resultados parciales: revisa `message`, los contadores y los logs incluso cuando `success` sea `true`.

También existen `POST /api/sync/periods`, `/careers`, `/groups`, `/teachers`, `/subjects` y `/classrooms`. Para sincronizar asignaciones de un período concreto utiliza `POST /api/asignaciones/sincronizar/{clavePeriodo}`. La sincronización general no sustituye la preparación de los bloques de `school_hours`.

### Generación de exámenes

`POST /api/schedules/generate` recibe, por ejemplo:

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

Los valores de carrera, grupo y período son ilustrativos: usa los correspondientes a tus datos. `licenciatura` se compara con el **nombre** de la carrera normalizado; los grupos se comparan por prefijo. Una lista de grupos vacía o nula incluye todos los grupos coincidentes. El período admite coincidencias normalizadas aproximadas.

El algoritmo implementado en [ScheduleGeneralService](src/main/java/com/horarios/horarios_unsis/schedule/domain/service/ScheduleGeneralService.java):

1. Busca la carrera y filtra las asignaciones por carrera, período y grupos.
2. Carga profesores, materias, aulas y bloques horarios.
3. Recorre el rango de fechas inclusive y busca el primer espacio disponible para cada materia y grupo.
4. Para `PARCIAL`, usa el bloque cuyo `periodNumber` coincide con la hora de la asignación. Para los demás valores aplica la lógica de ordinario: dos bloques contiguos y un profesor adicional disponible como sinodal.
5. Evita coincidencias de profesor, aula y grupo dentro de esa ejecución, con un máximo de un examen por día y grupo.
6. Guarda cada examen inmediatamente en PostgreSQL con estado `PENDING` y devuelve los resultados.

**No es una vista previa.** Volver a generar puede insertar nuevos registros. La disponibilidad se mantiene en memoria para la solicitud actual; no se inicializa con los exámenes ya guardados ni protege solicitudes concurrentes. No se excluyen fines de semana ni se comprueban capacidad del aula o sabáticos en este generador.

Otros límites relevantes de esta implementación:

- Guarda `idPeriodo = 1`, aunque usa el período solicitado para filtrar asignaciones.
- Si no encuentra una materia por nombre, utiliza `idMateria = 0`.
- El sinodal seleccionado se reserva en memoria, pero este flujo no persiste su asignación.
- Si no existen bloques, crea alternativas en memoria de una hora entre 08:00 y 19:00, sin insertarlas en `school_hours`.
- El adaptador guarda la entidad sin copiar su ID generado al modelo devuelto; consulta después `GET /api/schedules` para recuperar los IDs persistidos.
- Una carrera inexistente, asignaciones ausentes o recursos insuficientes pueden producir una lista vacía o incompleta. Revisa los logs.

### Creación manual

`POST /api/schedules` y `PUT /api/schedules/{id}` reciben:

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

Sustituye los IDs por los de tu base. Los seis IDs y `fecha` son obligatorios; `grupo` y `status` admiten hasta 50 caracteres. Este CRUD no ejecuta el algoritmo de generación. En el generador, `idTipo = 1` corresponde a parcial y `2` a ordinario.

## Mapa de la API

En los recursos con CRUD, las rutas comunes son `GET /recurso`, `GET /recurso/{id}`, `POST /recurso`, `PUT /recurso/{id}` y `DELETE /recurso/{id}`. Consulta Swagger para los cuerpos y esquemas completos.

| Ruta | Función |
| --- | --- |
| `POST /api/auth/login` | Autenticación |
| `/api/users` | CRUD de usuarios; consulta adicional `/username/{username}` |
| `/api/teachers` | CRUD de profesores; `POST /importar-api` |
| `/api/subjects` | CRUD de materias; `POST /importar-api`; filtros `claveCarrera` y `clavePeriodo` |
| `/api/classrooms` | CRUD de aulas |
| `/api/school-hours` | CRUD de bloques horarios; `POST /importar-api` |
| `GET /api/groups` | Consulta de grupos |
| `GET /api/groups/{id}` | Consulta de un grupo |
| `/api/schedules` | CRUD de exámenes |
| `POST /api/schedules/generate` | Generación y persistencia de exámenes |
| `POST /api/schedules/importar` | Importación de horarios externos |
| `GET /api/schedules/materia/{idMateria}` | Exámenes por materia |
| `GET /api/schedules/profesor/{profesorId}` | Exámenes por profesor |
| `GET /api/schedules/periodo/{idPeriodo}` | Exámenes por ID de período |
| `GET /api/schedules/status/{status}` | Exámenes por estado |
| `GET /api/schedules/rango?inicio=2026-11-02&fin=2026-11-06` | Exámenes por fechas |
| `GET /api/schedules/filtrar?claveCarrera=06&clavePeriodo=2025-2026A` | Filtro por claves de carrera y período |
| `/api/synodals` | Rutas de sinodales; implementación parcialmente pendiente |
| `GET /api/synodals/assignments` | Consulta de asignaciones de sinodales |
| `/api/asignaciones` | Sincronización y consultas por período, carrera, grupo, materia o profesor |
| `/api/sync` | Sincronización de catálogos |

## Organización del código

```text
src/main/java/com/horarios/horarios_unsis/
├── HorariosUnsisApplication.java   # Arranque y RestTemplate
├── config/                        # Seguridad, JWT, BCrypt, Swagger
├── data/                          # Catálogos, usuarios y asignaciones
│   └── <modulo>/
│       ├── application/            # DTO, mappers y algunos servicios
│       ├── domain/                 # Modelos, puertos y servicios
│       └── infrastructure/         # Controladores, entidades y repositorios
├── schedule/                      # CRUD y generación de exámenes
├── integration/
│   ├── Consume/                   # Clientes y DTO de la API universitaria
│   └── sync/                      # Coordinación de sincronización
└── shared/                        # Validadores, restricciones y servicios comunes
src/main/resources/
├── application.properties
└── db/seeddata.sql                 # Datos de prueba para carga manual
```

La estructura utiliza capas y puertos/adaptadores, aunque algunos servicios acceden directamente a repositorios JPA. El flujo habitual es controlador → servicio/caso de uso → repositorio o adaptador → PostgreSQL. Los DTO delimitan los datos HTTP y los mappers convierten entre modelos y entidades.

Las tablas principales incluyen `usuario`, `profesor`, `materia`, `aulas`, `school_hours`, `examen` y `asignacion_profesor_materia`. Esta última tiene dos representaciones JPA, en los módulos `asignacion` y `teacherSubjectAssignment`: sincronización y generación comparten esa tabla. Existen además entidades de carreras, períodos, grupos y relaciones entre materias y profesores.

## Datos de prueba y comprobaciones

[db/seeddata.sql](src/main/resources/db/seeddata.sql) contiene datos de ejemplo y un usuario `JEFE`. **No se ejecuta automáticamente** ni crea un administrador. Incluye referencias a IDs numéricos fijos y no es un script idempotente: revísalo antes de cargarlo en una base vacía. No prepara todos los catálogos y asignaciones necesarios para generar exámenes.

| Comando | Uso |
| --- | --- |
| `bash mvnw -version` | Verificar Maven y Java |
| `bash mvnw spring-boot:run` | Ejecutar en desarrollo |
| `bash mvnw test` | Ejecutar las pruebas |
| `bash mvnw clean package` | Compilar, probar y empaquetar |
| `java -jar target/horarios-unsis-0.0.1-SNAPSHOT.jar` | Ejecutar el JAR empaquetado con las variables configuradas |

Solo hay una prueba `contextLoads` con `@SpringBootTest`. Necesita configuración y conexión a PostgreSQL; no comprueba reglas de generación ni permisos de los endpoints. Usa una base de desarrollo también para las pruebas, dado que la configuración utiliza `ddl-auto=update`.

## Limitaciones y diagnóstico

- **Sinodales:** varias operaciones CRUD de `SynodalService` devuelven `null` o no realizan cambios. Que aparezcan en Swagger no implica que estén terminadas.
- **Generación por academia:** `ScheduleAcademyRequest` y `ScheduleAcademyService` son estructuras pendientes; la ruta `/generate` utiliza `ScheduleGeneralService`.
- **Configuración ausente:** si falla la resolución de propiedades, comprueba las cuatro variables obligatorias en el entorno del proceso o del IDE.
- **Conexión a PostgreSQL:** revisa URL, existencia de la base, credenciales y permisos para crear tablas.
- **Login `401`:** comprueba que el usuario exista en `usuario`, esté activo y tenga una contraseña BCrypt válida. Para otras rutas revisa también el token y su vencimiento.
- **Respuesta `403`:** revisa el rol y las restricciones del controlador, no solo las reglas HTTP generales.
- **Sincronización sin datos:** confirma que la integración esté habilitada y que el equipo resuelva y alcance el servidor universitario; revisa contadores y logs.

La configuración actual permite CORS desde cualquier origen con credenciales y registra SQL. Antes de desplegar, ajusta estas opciones al entorno y define cómo administrar los cambios de esquema. Esta documentación no acredita un despliegue ni una ejecución exitosa: en el entorno donde se elaboró no había Java instalado y no se ejecutaron compilación, pruebas ni conexiones a servicios externos.
