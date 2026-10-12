README
BirdTracker - API REST de avistamientos
API síncrona construida con Java 21, Spring Boot, Spring Web, Spring Data JPA, Bean Validation y H2. El cliente envía una petición HTTP y espera su respuesta. Los recursos y las operaciones siguen el contrato REST: JSON, rutas en plural, verbos HTTP y códigos de estado específicos. H2 guarda los datos localmente en ./data/birdtracker.
Requisitos
JDK 21
Maven 3.6 o superior (o el Maven Wrapper incluido)
Instalar y ejecutar
En Windows:
.\mvnw.cmd clean spring-boot:run
​
En macOS o Linux:
./mvnw clean spring-boot:run
​
La API queda disponible en http://localhost:8080. La página inicial abre Swagger UI; también puedes entrar directamente a http://localhost:8080/swagger-ui/index.html para probar las rutas. El contrato JSON está en http://localhost:8080/v3/api-docs. La consola de H2 está en http://localhost:8080/h2-console (JDBC URL: jdbc:h2:file:./data/birdtracker, usuario sa, contraseña vacía).
Recurso
Avistamiento tiene un id asignado por el sistema, especie, lugar, fecha (JSON como texto AAAA-MM-DD) y observador. Al crear o reemplazar un avistamiento se requieren todos los campos salvo id; la fecha debe ser válida. Datos ausentes o JSON/fecha inválidos responden 400.
Endpoints
Operación
Petición
Respuesta
Listar
GET /avistamientos
200 y lista JSON (lista vacía si no hay registros)
Consultar
GET /avistamientos/{id}
200 y recurso, o 404
Registrar
POST /avistamientos
201, recurso creado y encabezado Location, o 400
Reemplazar
PUT /avistamientos/{id}
200 y recurso actualizado, 404 o 400
Eliminar
DELETE /avistamientos/{id}
204 sin cuerpo, o 404
Resumen (bonus)
GET /avistamientos/resumen
200 con el total por especie
Los errores se entregan en JSON, por ejemplo: {"estado":404,"error":"Not Found","mensaje":"Avistamiento no encontrado"}.
Ejemplos con curl
Crear (la fecha de ejemplo debe ser hoy o anterior):
curl -i -X POST <http://localhost:8080/avistamientos> \
  -H "Content-Type: application/json" \
  -d '{"especie":"Colibrí","lugar":"Jardín Botánico","fecha":"2025-09-29","observador":"Ana Pérez"}'
​
Listar:
curl -i <http://localhost:8080/avistamientos>
​
Consultar (reemplace 1 por el ID creado):
curl -i <http://localhost:8080/avistamientos/1>
​
Reemplazar el avistamiento completo:
curl -i -X PUT <http://localhost:8080/avistamientos/1> \
  -H "Content-Type: application/json" \
  -d '{"especie":"Colibrí chillón","lugar":"Parque principal","fecha":"2026-09-28","observador":"Ana Pérez"}'
​
Eliminar:
curl -i -X DELETE <http://localhost:8080/avistamientos/1>
​
Resumen por especie:
curl -i <http://localhost:8080/avistamientos/resumen>
​
Notas de diseño y entrega
Las peticiones son síncronas: el consumidor necesita conocer de inmediato si el registro se creó, se actualizó o no existe.
POST crea; PUT reemplaza el recurso completo; DELETE elimina. La URL identifica el recurso y no codifica la acción.
H2 es una base de datos embebida que Maven incorpora en el proyecto; no hace falta instalar un servidor de base de datos.
El contrato de recursos, métodos, JSON y respuestas está documentado en openapi.yaml.
Para entregar, crea un repositorio público en GitHub o GitLab, sube el proyecto y comparte su URL.
Asistencia de IA: ChatGPT (GPT-6) para revisión e implementación. El estudiante debe revisar y comprender el código que entregue.
