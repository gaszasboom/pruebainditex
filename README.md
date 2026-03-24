# Price Service
**Autor:** Fernando Pilar Arce de la Plaza
**email:** fernarce@gmail.com

Microservicio de prueba para la obtención de tarifas de precios, diseñado aplicando Arquitectura Hexagonal y patrones tácticos de Domain-Driven Design (DDD).

## 1. El Problema
En la base de datos de comercio electrónico disponemos de una tabla `PRICES` que refleja el precio final (PVP) y la tarifa aplicable a un producto de una cadena. Esta información está segmentada por intervalos de fechas y precios (tarifas).
El objetivo principal de este microservicio es consultar dicha tabla y proveer un endpoint REST que, al recibir tres parámetros de entrada (fecha de aplicación, identificador de producto e identificador de cadena), sea capaz de determinar unívocamente el precio final aplicable y la tarifa correspondiente. Para ello, debe resolver posibles colisiones y solapamientos de fechas mediante una regla de prioridad (a mayor prioridad, prevalece la tarifa).

**Resolución Técnica:**
El conflicto de solapamiento y obtención del precio se resuelve a nivel de persistencia de manera eficiente. En el *Adapter* de base de datos (`SpringDataPriceRepository`), mediante una query SQL nativa, se realiza un filtrado por producto, cadena y fechas (`:applicationDate BETWEEN START_DATE AND END_DATE`), y posteriormente se ordena el resultado por la prioridad de forma descendente, delegando a la base de datos que devuelva únicamente el registro más prioritario (`ORDER BY PRIORITY DESC LIMIT 1`).

**Decisión de diseño — Eficiencia vs Escalabilidad:**
Existe un trade-off consciente en esta implementación. El enfoque alternativo "más escalable" sería traer todos los precios aplicables a la capa de dominio y resolver la prioridad en Java:
```java
// Enfoque escalable: lógica de prioridad en el dominio
List<Price> candidates = repository.findAllApplicable(date, productId, brandId);
return candidates.stream().max(Comparator.comparing(Price::getPriority));
```
Esto mantiene la regla de negocio en el dominio (más testeable y flexible ante cambios), pero implica transferir N registros desde la BD para quedarse con 1.

Se ha optado deliberadamente por el enfoque eficiente: delegar la selección a la BD con `LIMIT 1`, de forma que solo viaja un registro por petición. Esto reduce latencia, consumo de memoria y carga de red, a costa de que la regla de prioridad queda expresada en SQL en lugar de en el dominio. Esta decisión responde a un requisito explícito del cliente, que prioriza la eficiencia por encima de la escalabilidad de la lógica de negocio.

## 2. Arquitectura Hexagonal y SOLID
El proyecto está estructurado estrictamente en tres capas para garantizar el Principio de Inversión de Dependencias (SOLID):
- **Capa de Dominio (`domain`)**: Contiene la lógica central y es agnóstica de frameworks. Aquí se definen los *Ports* (interfaces).
- **Capa de Aplicación (`application`)**: Orquesta los casos de uso (ej. `GetPriceUseCase`), conectando los puertos.
- **Capa de Infraestructura (`infrastructure`)**: Implementa los *Adapters* de entrada (API REST) y salida (Base de datos H2).

## 3. Patrones Tácticos DDD
Se han aplicado principios de DDD para modelar el núcleo de negocio rico:
- **Value Objects**: Se usan clases inmutables y validadas (`BrandId`, `ProductId`, `Money`) para representar conceptos de dominio sin identidad pero con reglas específicas.
- **Aggregate Root / Entity**: `Price` encapsula y agrupa los distintos *Value Objects* para representar de forma íntegra un precio en el tiempo.

## 4. Endpoints
### Consultar Tarifa Aplicable
**GET** `/api/v1/prices`

**Query Parameters:**
- `application-date`: Fecha en formato ISO-8601 (ej. `2020-06-14T10:00:00`)
- `product-id`: ID numérico del producto (ej. `35455`)
- `brand-id`: ID numérico de la cadena (ej. `1` para ZARA)

**Ejemplo de Petición cURL:**
```bash
curl -G "http://localhost:8080/api/v1/prices" \
  --data-urlencode "application-date=2020-06-14T10:00:00" \
  --data-urlencode "product-id=35455" \
  --data-urlencode "brand-id=1"
```

### Comprobación de Estado (Health Check)
**GET** `/actuator/health`

Devuelve el estado de la aplicación y sus dependencias (incluida la conexión a base de datos H2).

**Ejemplo de Petición cURL:**
```bash
curl http://localhost:8080/actuator/health
```

**Ejemplo de Respuesta:**
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

### Gestión de Errores (RFC 9457)
La API implementa el estándar [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457.html) (Problem Details for HTTP APIs) para estandarizar las respuestas de error devueltas a los clientes.

**Ejemplo de Respuesta de Error (400 Bad Request):**
```json
{
  "type": "https://tools.ietf.org/html/rfc9457",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid parameters provided",
  "instance": "/api/v1/prices",
  "code": "invalid_request",
  "request_id": "aa1c3a64-77cc-4fae-9d8a-7debb58cdfed"
}
```

## 5. Pruebas de Integración y Unitarias
El proyecto incluye:
- **Test Unitarios (`GetPriceUseCaseTest`)**: Validan la lógica del caso de uso.
- **Test de Integración (`PriceControllerIntegrationTest`)**: Ejecutan todo el contexto de Spring (con Base de Datos H2 In-Memory mapeada con `schema.sql` y `data.sql`). Cubren los 5 escenarios obligatorios del enunciado validando el correcto filtrado por fecho, id de cadena, producto y desambiguación jerárquica por prioridad.
```bash
# Ejecutar suite de pruebas:
./mvnw clean test
```

## 6. Monitorización (Actuator)
Se ha integrado Spring Boot Actuator para la monitorización de la salud de la aplicación y la exposición de métricas básicas:
- **Health Check**: `GET http://localhost:8080/actuator/health` (Incluye detalle de la conexión a Base de datos H2).
## 7. Documentación Interactiva (Swagger / OpenAPI)
Para facilitar las pruebas y la consulta de la especificación de la API, se ha incluido **Swagger UI**.
Una vez iniciada la aplicación, la documentación interactiva está disponible en:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Todos los códigos HTTP (200, 400, 404, 500) que devuelve la API y sus modelos (`ProblemDetail`, `PagedResponse`) están especificados y expuestos para ser auto-consumidos.

## 8. Ecosistema de Desarrollo (Docker, CI & Code Quality)

Para garantizar la mantenibilidad y un despliegue ágil, el proyecto incorpora herramientas clave:

* **Docker & Docker Compose**: Se adjunta un `Dockerfile` (Multi-stage) y `docker-compose.yml`. El despliegue de pruebas es tan sencillo como ejecutar `docker compose up --build`. Se incluye además un *healthcheck* para monitorear el contenedor base a través de Spring Actuator.
* **Integración Continua (CI)**: La validación del código y ejecución de los tests unitarios e integrados corre a cargo de **GitHub Actions**. Ante cada push o pull request, el pipeline se dispara automáticamente bajo un entorno estéril confirmando que nada se ha roto (`.github/workflows/ci.yml`).
* **Calidad de código y Code Formatting**: Aunque se trata de un ecosistema Java, se valora el linting y la consistencia de estilos. Opcionalmente pueden configurarse plugins como *fmt-maven-plugin*, *Spotless*, o un equivalente a *ESLint/Prettier* para auto-formateo. Actualmente el código base respeta las convenciones del IDE.
