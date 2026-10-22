# BirdTracker

API REST para registrar, consultar, actualizar y eliminar avistamientos de aves. El proyecto está construido con Java 21 y Spring Boot 3.4.5; usa Spring Data JPA y una base de datos H2 en archivo para conservar los registros entre reinicios.

## Uso de IA

Se utilizó ChatGPT (modelo GPT-6) como asistencia para redactar este README.

## Requisitos

- JDK 21
- Git (opcional, para clonar el repositorio)

No es necesario instalar Maven: el repositorio incluye el Maven Wrapper (`mvnw` / `mvnw.cmd`).

## Ejecutar

Clona el repositorio y entra en el directorio del proyecto. En Windows PowerShell:

```powershell
git clone <URL_DEL_REPOSITORIO>
cd BirdTracker
.\mvnw.cmd spring-boot:run
```

En macOS o Linux:

```bash
git clone <URL_DEL_REPOSITORIO>
cd BirdTracker
./mvnw spring-boot:run
```

La aplicación inicia en `http://localhost:8080`. La ruta raíz redirige a la documentación interactiva de Swagger UI.

Para generar el artefacto ejecutable:

```powershell
.\mvnw.cmd -DskipTests package
java -jar target/bird-tracker-1.0.0.jar
```

En macOS o Linux, reemplaza `.\mvnw.cmd` por `./mvnw`.

## Documentación

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- Especificación OpenAPI: <http://localhost:8080/v3/api-docs>
- Archivo OpenAPI del repositorio: [`openapi.yaml`](openapi.yaml)

## API

Todas las rutas usan JSON. Un avistamiento contiene:

| Campo | Tipo | Reglas |
| --- | --- | --- |
| `id` | número entero | Lo asigna el servidor; no se envía al crear o actualizar. |
| `especie` | texto | Obligatorio, entre 3 y 50 caracteres (sin contar espacios al inicio y al final). |
| `lugar` | texto | Obligatorio, no puede estar vacío (máximo 200 caracteres). |
| `fecha` | fecha `AAAA-MM-DD` | Obligatoria, fecha real y no posterior a hoy. |
| `observador` | texto | Obligatorio, no puede estar vacío (máximo 120 caracteres). |

| Método | Ruta | Descripción | Respuesta habitual |
| --- | --- | --- | --- |
| `GET` | `/avistamientos` | Lista todos los avistamientos. | `200 OK` |
| `GET` | `/avistamientos/{id}` | Consulta un avistamiento por ID. | `200 OK`, `404 Not Found` |
| `GET` | `/avistamientos/resumen` | Cuenta los registros agrupados por especie. | `200 OK` |
| `POST` | `/avistamientos` | Registra un avistamiento. | `201 Created`, `400 Bad Request` |
| `PUT` | `/avistamientos/{id}` | Reemplaza los datos de un avistamiento. | `200 OK`, `400 Bad Request`, `404 Not Found` |
| `DELETE` | `/avistamientos/{id}` | Elimina un avistamiento. | `204 No Content`, `404 Not Found` |

### Ejemplos

Crear un registro:

```bash
curl -i -X POST http://localhost:8080/avistamientos \
  -H "Content-Type: application/json" \
  -d '{"especie":"Colibrí","lugar":"Jardín Botánico","fecha":"2025-09-29","observador":"Ana Pérez"}'
```

Consultar todos los registros:

```bash
curl http://localhost:8080/avistamientos
```

Consultar el resumen por especie:

```bash
curl http://localhost:8080/avistamientos/resumen
```

La respuesta de creación incluye el `id` asignado y una cabecera `Location` con la ruta del recurso. Si el JSON está mal formado o incumple las validaciones, la API responde con `400`; si el ID no existe, responde con `404`.

La regla de negocio vive en `validacion/ValidadorDeAvistamiento`, separada del controlador. Cuando un dato no es válido, `POST` y `PUT` responden `400` indicando qué campo falló:

```json
{
  "estado": 400,
  "error": "Bad Request",
  "mensaje": "Datos inválidos en: especie",
  "detalles": { "especie": "La especie debe tener entre 3 y 50 caracteres (tiene 2)" }
}
```

## Persistencia y consola H2

La base H2 en modo archivo se guarda en `data/birdtracker.mv.db`. Por eso los avistamientos persisten cuando se detiene y vuelve a iniciar la aplicación. La consola H2 está disponible en <http://localhost:8080/h2-console> con estos valores predeterminados:

- JDBC URL: `jdbc:h2:file:./data/birdtracker;DB_CLOSE_ON_EXIT=FALSE`
- Usuario: `sa`
- Contraseña: vacía

## Pruebas

Las pruebas están en `src/test/java` y usan JUnit 5, Mockito y MockMvc (`@SpringBootTest`). Las pruebas usan una base H2 **en memoria** (`src/test/resources/application.properties`), así que no tocan `data/birdtracker.mv.db`.

| Clase | Nivel | Qué prueba |
| --- | --- | --- |
| `validacion/ValidadorDeAvistamientoTest` | Unitaria | La regla de negocio con partición y valores límite; usa el stub `RelojFijo`. |
| `service/AvistamientoServiceTest` | Unitaria | El servicio con un mock de `AvistamientoRepository` y el stub `RelojFijo`. |
| `controller/AvistamientoApiIntegrationTest` | Integración | Los endpoints por HTTP (MockMvc), del controlador hasta H2. |

Ejecutar solo las pruebas (desde la carpeta `BirdTracker`):

```powershell
.\mvnw.cmd test
```

Ejecutar las pruebas y generar el reporte de cobertura con JaCoCo:

```powershell
.\mvnw.cmd clean verify
```

El reporte queda en `target/site/jacoco/index.html` (ábrelo en el navegador). En macOS o Linux, usa `./mvnw test` y `./mvnw clean verify`.

La justificación de las decisiones de pruebas está en [`docs/justificacion-pruebas.md`](docs/justificacion-pruebas.md).
