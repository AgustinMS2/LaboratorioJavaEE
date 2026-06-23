# Gestión de Movilidad Eléctrica — Taller Java 2026

Sistema de gestión de cargas para vehículos eléctricos, desarrollado con Jakarta EE 10 sobre WildFly, siguiendo una arquitectura monolítica modular.

---

## Índice

1. [Descripción del sistema](#descripción-del-sistema)
2. [Arquitectura](#arquitectura)
3. [Estructura de paquetes](#estructura-de-paquetes)
4. [Modelo de dominio](#modelo-de-dominio)
5. [Módulos y casos de uso](#módulos-y-casos-de-uso)
6. [Iteración 2 — Integración con sistemas externos](#iteración-2--integración-con-sistemas-externos)
    - [Esquema de integración](#esquema-de-integración)
    - [APIs que se exponen](#apis-que-se-exponen)
    - [Autenticación App Móvil](#autenticación-app-móvil)
    - [APIs que se consumen](#apis-que-se-consumen)
    - [Pago rechazado y deuda pendiente](#pago-rechazado-y-deuda-pendiente)
    - [Manejo de errores REST](#manejo-de-errores-rest)
7. [Iteración 3 — Observabilidad](#arquitectura-de-observabilidad)
    - [Arquitectura de observabilidad](#arquitectura-de-observabilidad)
    - [Métricas implementadas](#métricas-implementadas)
    - [Flujo de eventos](#flujo-de-eventos)
    - [Configuración del servidor de observabilidad](#configuración-del-servidor-de-observabilidad)
    - [Dashboard Grafana](#dashboard-grafana)
8. [Iteración 4 — Messaging](#iteración-4--messaging)
    - [Arquitectura de mensajería](#arquitectura-de-mensajería)
    - [Configuración de la queue (WildFly)](#configuración-de-la-queue-wildfly)
    - [Integración con el LLM (Llama2 local vía Ollama)](#integración-con-el-llm-llama2-local-vía-ollama)
    - [Métrica de monitoreo agregada](#métrica-de-monitoreo-agregada)
    - [Probar el flujo](#probar-el-flujo)
9. [Configuración del entorno](#configuración-del-entorno)
    - [Linux](#linux)
    - [Windows](#windows)
10. [Cómo correr el proyecto](#cómo-correr-el-proyecto)
11. [Tecnologías](#tecnologías)
12. [Mocks de sistemas externos](#mocks-de-sistemas-externos)
13. [Rate Limiter](#rate-limiter)
14. [Problemas frecuentes](#problemas-frecuentes)

---

## Descripción del sistema

El sistema permite gestionar la carga de vehículos eléctricos en estaciones distribuidas. Los clientes pueden iniciar y finalizar cargas, gestionar sus medios de pago y realizar reclamos. El sistema procesa los pagos automáticamente al finalizar cada carga.

---

## Arquitectura

El proyecto sigue una **arquitectura monolítica modular**, donde cada módulo está diseñado para ser independiente y potencialmente evolucionar hacia un microservicio.

Los módulos se comunican entre sí únicamente a través de las interfaces definidas en sus paquetes `aplicacion/`, respetando el principio de bajo acoplamiento. Cuando `moduloCargas` necesita cobrar un pago, invoca la interfaz `ServicioPago` del `moduloPagos` sin conocer su implementación interna.

### Sistemas y subsistemas
![Integración del sistema](docs/integracion-sistema.png)

### Modulos
![Arquitectura modular](docs/arquitectura-modulos.png)

---

## Estructura de paquetes

Cada módulo sigue la misma estructura interna:

```
moduloXxx/
├── dominio/
│   ├── repositorio/            → interfaces de persistencia (contratos)
│   └── Entidad.java            → clases de dominio con lógica de negocio
├── aplicacion/
│   ├── ServicioXxx.java        → interfaz pública del módulo (casos de uso)
│   └── impl/
│       └── ServicioXxxImpl.java → implementación (@ApplicationScoped @Transactional)
├── interfase/                  → interfaces remotas e inter-módulo (próximas iteraciones)
└── infraestructura/
    └── persistencia/
        └── XxxRepositorioImpl.java → implementación JPA con EntityManager
```

### Diagrama de paquetes completo

```
org.tallerJava
│
├── moduloClientes
│   ├── dominio
│   │   ├── repositorio
│   │   │   ├── ClienteRepositorio
│   │   │   └── MedioPagoRepositorio
│   │   ├── Cliente (abstract)
│   │   ├── ClienteComun
│   │   ├── ClienteProfesional
│   │   ├── MedioPago (abstract)
│   │   ├── Tarjeta
│   │   ├── CuentaUTE
│   │   ├── Reclamo
│   │   ├── TipoProfesional (enum)
│   │   └── TipoTarjeta (enum)
│   ├── aplicacion
│   │   ├── ServicioCliente
│   │   └── impl / ServicioClienteImpl
│   ├── interfase
│   └── infraestructura / persistencia
│       ├── ClienteRepositorioImpl
│       └── MedioPagoRepositorioImpl
│
├── moduloCargas
│   ├── dominio
│   │   ├── repositorio
│   │   │   ├── CargaRepositorio
│   │   │   ├── CargadorRepositorio
│   │   │   └── EstacionCargaRepositorio
│   │   ├── Carga
│   │   ├── Cargador
│   │   ├── EstacionCarga
│   │   ├── EstadoCargador (enum)
│   │   ├── EstadoCarga (enum)
│   │   ├── TipoCargador (enum)
│   │   └── TipoConector (enum)
│   ├── aplicacion
│   │   ├── ServicioCarga
│   │   └── impl / ServicioCargaImpl
│   ├── interfase
│   │   └── evento
│   │       ├── CargaFinalizadaEvento
│   │       └── CargaIniciadaEvento
│   └── infraestructura / persistencia
│       ├── CargaRepositorioImpl
│       ├── CargadorRepositorioImpl
│       └── EstacionCargaRepositorioImpl
│
├── moduloPagos
│   ├── dominio
│   │   ├── repositorio
│   │   │   └── PagoRepositorio
│   │   └── Pago
│   ├── aplicacion
│   │   ├── ServicioPago
│   │   └── impl / ServicioPagoImpl
│   ├── interfase
│   │   └── evento
│   │       ├── PagoTarjetaEvento
│   │       ├── PagoUTEEvento
│   │       └── PagoRechazadoEvento
│   └── infraestructura / persistencia
│       └── PagoRepositorioImpl
│
└── moduloMonitoreo
    ├── infraestructura
    │   └── RegistradorDeMetricas
    └── interfase
        └── evento
            └── ObserverMonitoreo
```

---

## Modelo de dominio

![Arquitectura modular](docs/diagrama-dominio.png)

---

## Módulos y casos de uso

### Módulo Clientes

| Operación | Descripción | Consumidor |
|---|---|---|
| `registrarCliente(cliente)` | Registra un nuevo cliente (valida cédula única) | App móvil |
| `altaMedioPago(clienteId, medioPago)` | Agrega un medio de pago al cliente | App móvil |
| `obtenerClientes()` | Devuelve todos los clientes registrados | Gestor web |
| `realizarReclamo(clienteId, comentario)` | Registra un reclamo del cliente | App móvil |

### Módulo Cargas

| Operación | Descripción | Consumidor |
|---|---|---|
| `iniciarCarga(clienteId, cargadorId, medioPagoId)` | Inicia una carga, ocupa el cargador | App móvil |
| `verCargaActual(clienteId)` | Devuelve la carga activa del cliente | App móvil |
| `verHistorico(clienteId, desde, hasta)` | Histórico de cargas por rango de fechas | App móvil |
| `finalizarCarga(cargadorId, cargaId, consumo, recargo)` | Finaliza la carga, libera el cargador y dispara el pago | Cargador |
| `altaEstacion(estacion)` | Da de alta una nueva estación de carga | Gestor web |
| `altaCargador(estacionId, cargadorId)` | Asocia un cargador existente a una estación | Gestor web |
| `obtenerEstaciones()` | Lista todas las estaciones con sus cargadores y estados | App móvil |

### Módulo Pagos

| Operación | Descripción | Consumidor |
|---|---|---|
| `pagarCarga(clienteId, cargaId, importe, medioPagoId)` | Cobra la carga usando el medio de pago del cliente | Módulo Cargas |
| `consultarPagos(clienteId, desde, hasta)` | Lista los pagos del cliente en el rango de fechas | Gestor web |

---

## Iteración 2 — Integración con sistemas externos

En esta iteración el sistema se conecta al exterior: expone sus servicios a distintos actores mediante APIs REST y consume servicios externos para procesar los pagos.

---

### Esquema de integración

```mermaid
flowchart LR
    subgraph Actores
        AM[App Móvil]
        CA[Cargador]
        GW[Gestor Web]
    end

    subgraph GestorMovilidad Core
        API[API REST]
    end

    subgraph Sistemas externos
        MP[ServicioMedioPagoMock]
        UTE[FacturaUTEMock]
    end

    AM -->|Basic Auth\nHTTP| API
    CA -->|HTTP| API
    GW -->|HTTP| API

    API -->|POST /pagos/autorizar\nTarjeta de crédito| MP
    API -->|POST /factura-ute/notificar-pago\nCuenta UTE| UTE
```

El sistema cumple dos roles simultáneamente:
- **Servidor**: expone APIs REST consumidas por el Cargador, la App Móvil y el Gestor Web.
- **Cliente**: consume las APIs REST de los sistemas externos de pago.

---

### APIs que se exponen

#### Para el Cargador

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/gestion/cargas/finalizar` | Notifica que la carga finalizó; dispara el cobro automáticamente |

#### Para el Gestor Web

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/gestion/cargas/estaciones` | Da de alta una estación de carga |
| `POST` | `/gestion/cargas/estaciones/{id}/cargadores` | Asocia un cargador a una estación |
| `GET`  | `/gestion/clientes` | Lista todos los clientes registrados |
| `GET`  | `/gestion/pagos/{clienteId}?desde=&hasta=` | Consulta pagos del cliente por rango de fechas |

#### Para la App Móvil

Todos los endpoints de la app móvil requieren autenticación Basic Auth (ver sección siguiente).

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/gestion/clientes` | Registro de nuevo cliente (no requiere auth) |
| `POST` | `/gestion/clientes/{id}/mediosPago` | Asocia un medio de pago al cliente |
| `POST` | `/gestion/clientes/{id}/reclamos` | Registra un reclamo |
| `POST` | `/gestion/cargas/iniciar` | Inicia una sesión de carga |
| `GET`  | `/gestion/cargas/actual/{clienteId}` | Consulta el estado de la carga activa |
| `GET`  | `/gestion/cargas/historico/{clienteId}?desde=&hasta=` | Histórico de cargas (protegido por rate limiter) |
| `GET`  | `/gestion/cargas/estaciones` | Lista estaciones y cargadores disponibles |
| `POST` | `/gestion/pagos/{clienteId}/pagar-deuda` | Salda una deuda pendiente por pago rechazado |

---

### Autenticación App Móvil

Los endpoints de la App Móvil están protegidos con **HTTP Basic Authentication**. Cada request debe incluir el header:

```
Authorization: Basic <base64(cedula:contraseña)>
```

**Ejemplo:**

```bash
# cedula: 12345678 / contraseña: pass123
curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/cargas/iniciar \
  -H "Authorization: Basic MTIzNDU2Nzg6cGFzczEyMw==" \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"cargadorId":1,"medioPagoId":1}'
```

Un request sin credenciales o con credenciales inválidas recibe `401 Unauthorized`.

El mecanismo está implementado como un `ContainerRequestFilter` (`FiltroAutenticacion`) que se activa mediante la anotación `@AppMovil` aplicada a cada endpoint protegido.

---

### APIs que se consumen

Al finalizar una carga, el sistema invoca automáticamente al servicio de pago correspondiente según el medio de pago utilizado.

#### Servicio de Medio de Pago (tarjeta)

| Campo | Valor |
|---|---|
| Endpoint | `POST http://localhost:8080/ServicioMedioPagoMock-1.0.0/api/pagos/autorizar` |
| Body | `{ "idCliente": "1", "numeroTarjeta": "4111111111111111", "monto": 10.50 }` |
| Respuesta aprobada | HTTP 200 `{ "estado": "APROBADO" }` |
| Respuesta rechazada | HTTP 402 `{ "estado": "RECHAZADO" }` |

Para saldar una deuda pendiente:

| Campo | Valor |
|---|---|
| Endpoint | `POST http://localhost:8080/ServicioMedioPagoMock-1.0.0/api/pagos/deuda/pagar` |
| Body | `{ "numeroTarjeta": "4000000000000002" }` |

**Tarjetas de prueba:**

| Número | Comportamiento |
|---|---|
| `4111111111111111` | Siempre aprobada |
| `4000000000000002` | Siempre rechazada |
| Cualquier otro | Aleatorio, ratio 5:1 a favor del aprobado |

#### Servicio de Factura UTE (cuenta UTE)

| Campo | Valor |
|---|---|
| Endpoint | `POST http://localhost:8080/FacturaUTEMock/api/factura-ute/notificar-pago` |
| Body | `{ "clienteId": 1, "cargaId": 1, "numeroCuenta": "UTE-12345", "importe": 10.50 }` |
| Respuesta | Siempre HTTP 200 `{ "estado": "CONFIRMADO", "codigoUTE": "..." }` |

---

### Pago rechazado y deuda pendiente

Cuando el servicio de Medio de Pago rechaza el cobro de una tarjeta, el pago queda registrado con estado `RECHAZADO`. A partir de ese momento el cliente **no puede iniciar una nueva carga** hasta que salde la deuda.

**Flujo:**

```
1. finalizarCarga()
       → mock rechaza la tarjeta
       → Pago.estado = "RECHAZADO"

2. iniciarCarga()  (cualquier tarjeta)
       → verifica deuda pendiente → bloqueado (HTTP 409)

3. POST /gestion/pagos/{clienteId}/pagar-deuda
       → llama a mock POST /pagos/deuda/pagar
       → Pago.estado = "SALDADO"

4. iniciarCarga()  → permitido nuevamente
```

**Estados posibles de un Pago:**

| Estado | Descripción |
|---|---|
| `APROBADO` | Cobro con tarjeta exitoso |
| `RECHAZADO` | Tarjeta rechazada; cliente bloqueado |
| `SALDADO` | Deuda pagada; cliente desbloqueado |
| `PROCESADO` | Cobrado vía factura UTE |

---

### Manejo de errores REST

Un `ExceptionMapper` global convierte las excepciones del dominio en respuestas HTTP con cuerpo JSON:

| Excepción | HTTP | Ejemplo |
|---|---|---|
| `IllegalStateException` | `409 Conflict` | Cliente bloqueado por deuda, carga ya activa |
| `IllegalArgumentException` | `400 Bad Request` | Entidad no encontrada, dato inválido |
| Cualquier otra | `500 Internal Server Error` | Error inesperado |

```json
{ "error": "El cliente tiene un pago rechazado pendiente. Debe saldar la deuda antes de iniciar una nueva carga." }
```

---

## Iteración 3 — Observabilidad

En esta iteración se agrega observabilidad al sistema. Los módulos de negocio disparan eventos CDI al ocurrir acciones relevantes, y el `moduloMonitoreo` los registra como métricas en InfluxDB sin que los módulos de negocio conozcan la existencia del monitoreo.

---

### Arquitectura de observabilidad

El sistema de observabilidad es completamente externo al core y corre en un contenedor Docker independiente. El backend envía métricas hacia InfluxDB usando la librería Micrometer, y Grafana las visualiza consultando InfluxDB.

![Arquitecua_Observabilidad](docs/diagrama-observabilidad.png)

Los módulos de negocio disparan eventos CDI cuando ocurren acciones relevantes, y el `moduloMonitoreo` los escucha de forma completamente desacoplada, sin que los módulos de negocio conozcan la existencia del monitoreo.

---

### Métricas implementadas

| Métrica | Measurement en InfluxDB | Tipo | Descripción |
|---|---|---|---|
| Cargas activas | `cargasActivas` | Gauge | Cantidad de cargas en curso en un momento dado (sube y baja) |
| Cargas realizadas | `cargasRealizadas` | Counter | Cantidad acumulada de cargas finalizadas |
| Pagos con tarjeta | `pagosTarjeta` | Counter | Cantidad acumulada de pagos aprobados con tarjeta |
| Pagos con UTE | `pagosUTE` | Counter | Cantidad acumulada de pagos procesados vía factura UTE |
| Pagos rechazados | `pagosRechazados` | Counter | Cantidad acumulada de pagos rechazados con tarjeta |

---

### Flujo de eventos

Cuando ocurre una acción en el sistema, el módulo correspondiente dispara un evento CDI que el `ObserverMonitoreo` escucha y registra en InfluxDB:

```
iniciarCarga()
    └── fire(CargaIniciadaEvento)
            └── ObserverMonitoreo.onCargaIniciada()
                    └── RegistradorDeMetricas.incrementarCargasActivas()   ← gauge sube

finalizarCarga()
    └── fire(CargaFinalizadaEvento)
            ├── ObserverMonitoreo.onCargaFinalizada()
            │       ├── RegistradorDeMetricas.decrementarCargasActivas()  ← gauge baja
            │       └── RegistradorDeMetricas.incrementarCounter("cargasRealizadas")
            └── CargaFinalizadaObservador → ServicioPago.pagarCarga()
                    ├── tarjeta aprobada → fire(PagoTarjetaEvento)
                    │       └── ObserverMonitoreo.onPagoTarjeta()
                    │               └── RegistradorDeMetricas.incrementarCounter("pagosTarjeta")
                    ├── tarjeta rechazada → fire(PagoRechazadoEvento)
                    │       └── ObserverMonitoreo.onPagoRechazado()
                    │               └── RegistradorDeMetricas.incrementarCounter("pagosRechazados")
                    └── cuenta UTE → fire(PagoUTEEvento)
                            └── ObserverMonitoreo.onPagoUTE()
                                    └── RegistradorDeMetricas.incrementarCounter("pagosUTE")
```

---

### Configuración del servidor de observabilidad

El servidor de observabilidad usa la imagen Docker `philhawthorne/docker-influxdb-grafana` que incluye InfluxDB y Grafana preconfigurados en un mismo contenedor.

**Levantar el contenedor:**

```
docker run -d --name influxdb-grafana -p 3003:3003 -p 8083:8083 -p 8086:8086 -p 22022:22 philhawthorne/docker-influxdb-grafana:latest
```

**Para volver a levantarlo en sesiones futuras:**

```
docker start influxdb-grafana
```

**Accesos:**

| Herramienta | URL | Credenciales |
|---|---|---|
| Grafana | http://localhost:3003 | root / root |
| Chronograf (admin InfluxDB) | http://localhost:8083 | sin credenciales |
| InfluxDB API | http://localhost:8086 | sin credenciales |

**Crear la base de datos de métricas** (solo la primera vez, desde Chronograf → Explore):

```sql
CREATE DATABASE metricasTallerJava
```

**Configurar Micrometer** (`RegistradorDeMetricas`):

```
Base de datos : metricasTallerJava
URL InfluxDB  : http://localhost:8086
Intervalo     : 10 segundos
```

---

### Dashboard Grafana

El dashboard `GestorMovilidad` muestra 5 paneles de métricas en tiempo real con auto-refresh cada 5 segundos, accesible en `http://localhost:3003/d/taller-java-2026/gestormovilidad`.

Para importarlo en una instalación nueva de Grafana:

1. Configurar datasource InfluxDB apuntando a `http://localhost:8086`, base `metricasTallerJava`
2. Ir a Dashboards → Import → subir el archivo `dash-grafana.json` (en la raíz del proyecto)

Los paneles usan las siguientes queries InfluxDB:

| Panel | Query |
|---|---|
| Cargas Activas | `SELECT last("value") FROM "cargasActivas"` |
| Cargas Realizadas | `SELECT last("value") FROM "cargasRealizadas"` |
| Pagos Tarjeta | `SELECT last("value") FROM "pagosTarjeta"` |
| Pagos UTE | `SELECT last("value") FROM "pagosUTE"` |
| Pagos Rechazados con Tarjeta | `SELECT last("value") FROM "pagosRechazados"` |

> **Nota sobre los valores en InfluxDB:** Micrometer publica los counters como **deltas por intervalo** (no acumulados). El valor que aparece en cada punto representa los eventos ocurridos en los últimos 10 segundos. `cargasActivas` es un Gauge y sí refleja el valor absoluto en tiempo real.

---

## Iteración 4 — Messaging

En esta iteración se incorpora una **queue de mensajes JMS punto a punto** (Jakarta Messaging) para procesar los reclamos de los clientes de forma **asincrónica**. Cuando un cliente realiza un reclamo respondemos de inmediato que fue aceptado, y el etiquetado (positivo / negativo / neutro) se hace en segundo plano con ayuda de un modelo de lenguaje (LLM). Así no le trasladamos al cliente el tiempo de procesamiento del LLM y desacoplamos temporalmente los componentes.

---

### Arquitectura de mensajería

```
Cliente ── nuevo reclamo ──▶ ClienteAPI ──▶ ServicioCliente
                                                 │  (persiste el reclamo, responde 201)
                                                 └──▶ ProductorReclamos ──▶ [ queue: reclamos ]
                                                                                   │
                                                                                   ▼
                                                                          ConsumidorReclamos (MDB)
                                                                                   │
                                                          ┌────────────────────────┴───────────────┐
                                                          ▼                                          ▼
                                                  ClienteLLMHTTP (Ollama)                ServicioEtiquetadoReclamo
                                                  cataloga el reclamo                    guarda etiqueta en BD
                                                                                         + fire(ReclamoNegativoEvento)
                                                                                                   │
                                                                                                   ▼
                                                                                         ObserverMonitoreo → reclamosNegativos
```

| Componente | Rol |
|---|---|
| `ProductorReclamos` | Encola el reclamo (texto + `reclamoId`) en la queue al recibir el request del cliente |
| `ConsumidorReclamos` | MDB que consume la queue, clasifica vía LLM y persiste la etiqueta de forma asincrónica |
| `ClienteLLMHTTP` | Integración HTTP con Ollama (`http://localhost:11434/api/generate`, modelo `llama3.2`) |
| `ServicioEtiquetadoReclamo` | Persiste la `etiqueta` en el `Reclamo` y dispara `ReclamoNegativoEvento` si es negativo |

El `onMessage` del MDB se marca como `NOT_SUPPORTED` para **no** mantener abierta una transacción JTA durante la llamada al LLM (que demora varios minutos según el documento de referencia). La persistencia de la etiqueta abre su propia transacción corta en `ServicioEtiquetadoReclamo`.

---

### Configuración de la queue (WildFly)

La queue se crea automáticamente al provisionar el servidor desde `config.cli` sobre el subsistema `messaging-activemq` (incluido en `standalone-full.xml`):

```
/subsystem=messaging-activemq/server=default/jms-queue=reclamos:add(entries=["java:/jms/queue/reclamos","java:jboss/exported/jms/queue/reclamos"])
```

El productor y el consumidor referencian la queue por su JNDI `java:/jms/queue/reclamos`.

---

### Integración con el LLM (Ollama)

Siguiendo el documento de referencia *"modelo Llama2 local"*, el motor se levanta con Docker. La propia letra sugiere investigar un modelo más liviano y rápido que Llama2, por eso usamos **llama3.2** (≈2 GB vs ≈3.8 GB de llama2, y notablemente más rápido):

```
docker run -d -p 11434:11434 --name ollama ollama/ollama
docker exec -it ollama ollama pull llama3.2
```

`ClienteLLMHTTP` envía el reclamo al endpoint `/api/generate` pidiéndole catalogarlo en una palabra (positivo / negativo / neutro) y mapea la respuesta a la etiqueta. Si el LLM no está disponible, el reclamo se etiqueta como `NEUTRO` (degradación elegante).

El modelo se puede cambiar sin recompilar mediante la variable de entorno `OLLAMA_MODEL` (por defecto `llama3.2`).

> **Nota:** el read timeout del cliente HTTP está fijado en 10 minutos por las dudas; con llama3.2 la clasificación tarda unos pocos segundos.

---

### Métrica de monitoreo agregada

| Métrica | Measurement en InfluxDB | Tipo | Descripción |
|---|---|---|---|
| Reclamos negativos | `reclamosNegativos` | Counter | Cantidad acumulada de reclamos catalogados como negativos por el LLM |

Se agregó al dashboard `GestorMovilidad` un panel **"Reclamos Negativos"** (`SELECT last("value") FROM "reclamosNegativos"`) que se importa con el `dash-grafana.json` actualizado.

---

### Probar el flujo

```bash
# 1. Autenticarse como app móvil y obtener token (ver Iteración 2)
# 2. Enviar un reclamo
curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/clientes/1/reclamos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d "\"Hace horas que no puedo cargar, el cargador está roto y nadie me ayuda\""
# Responde 201 inmediatamente; el etiquetado ocurre en segundo plano.
```


---

## Configuración del entorno

### Requisitos

| Herramienta | Versión | Notas |
|---|---|---|
| Java | **17** (Temurin/OpenJDK) | No usar Java 21 |
| Maven | 3.x | |
| WildFly | 27.0.1 | Se descarga automáticamente |
| MariaDB | 10.x o superior | Puerto **3307**, usuario `root`, contraseña `root` |
| Docker | Desktop o Engine | Para el servidor de observabilidad |

> ⚠️ Se usa el puerto **3307** para no chocar con MySQL/MariaDB que usan el 3306 por defecto.

El archivo `mariadb-java-client-3.3.3.jar` debe estar en la **raíz del proyecto**. Ya está incluido en el repositorio.

---

### Linux

Instrucciones probadas en distribuciones basadas en **Debian/Ubuntu**. En otras distros, usar el gestor de paquetes equivalente.

#### 1. Instalar dependencias

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk maven mariadb-server
```

Comprobar versiones:

```bash
java -version    # debe mostrar 17.x
mvn -version
```

Si `java -version` muestra Java 21 u otra versión, fijar Java 17 antes de compilar o ejecutar:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH="$JAVA_HOME/bin:$PATH"
java -version
```

Para dejarlo permanente, agregar esas dos líneas `export` al final de `~/.bashrc` (o `~/.profile`).

#### 2. Configurar MariaDB (puerto 3307 y contraseña)

Editar el archivo de configuración del servidor (ruta habitual en Ubuntu/Debian):

```bash
sudo nano /etc/mysql/mariadb.conf.d/50-server.cnf
```

Buscar la línea `#port = 3306`, descomentarla y cambiarla a:

```ini
port = 3307
```

Reiniciar el servicio:

```bash
sudo systemctl restart mariadb
sudo systemctl enable mariadb
```

Definir la contraseña del usuario `root` (en instalaciones nuevas suele bastar con acceso por socket):

```bash
sudo mariadb -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root'; FLUSH PRIVILEGES;"
```

Verificar que escucha en el puerto correcto:

```bash
sudo ss -tlnp | grep 3307
```

#### 3. Crear la base de datos

Ejecutar **una sola vez** desde la raíz del proyecto (o cualquier directorio):

```bash
mysql -u root -proot -P 3307 -h 127.0.0.1 -e "CREATE DATABASE IF NOT EXISTS tallerJava CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

#### 4. Ejecutar el proyecto

Clonar o abrir el repositorio y, desde su raíz:

```bash
cd /ruta/al/LaboratorioJavaEE
mvn wildfly:run
```

Al correr este comando:

1. Maven descarga y levanta WildFly automáticamente
2. Se ejecuta `config.cli`, que registra el driver MariaDB y crea el datasource `java:jboss/MariaDB`
3. Se despliega la aplicación
4. Hibernate genera las tablas en la base `tallerJava`

La **primera ejecución** puede tardar varios minutos (descarga de WildFly, ~200 MB).

Para detener el servidor: `Ctrl + C`

La consola de administración de WildFly queda disponible en [http://localhost:9990](http://localhost:9990) (usuario `root` / contraseña `rootpass1234`, según `pom.xml`).

---

### Windows

#### 1. Instalar MariaDB

1. Descargar desde [https://mariadb.org/download/](https://mariadb.org/download/) — **Windows x86_64 MSI Package**
2. Durante la instalación configurar:
   - Contraseña de root: `root`
   - Puerto: `3307`

#### 2. Crear la base de datos

Ejecutar **una sola vez** desde la terminal:

```bash
"C:\Program Files\MariaDB 12.2\bin\mysql.exe" -u root -proot -P 3307 -e "CREATE DATABASE IF NOT EXISTS tallerJava CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

#### 3. Ejecutar el proyecto

Desde la raíz del proyecto:

```bash
mvn wildfly:run
```

Mismo comportamiento que en Linux (WildFly, `config.cli`, despliegue y tablas). Detener con `Ctrl + C`.

---

## Cómo correr el proyecto

En **Linux** y **Windows**, desde la raíz del repositorio:

```bash
mvn wildfly:run
```

Los pasos previos (Java 17, MariaDB en el puerto 3307, base `tallerJava`) están detallados en [Configuración del entorno](#configuración-del-entorno).

Para el servidor de observabilidad, levantar el contenedor Docker antes de iniciar el proyecto:

```powershell
docker start influxdb-grafana
```

---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 (Temurin) | Lenguaje |
| Jakarta EE | 10 | Framework principal |
| WildFly | 27.0.1 | Servidor de aplicaciones |
| Hibernate | incluido en WildFly | ORM / JPA |
| MariaDB | 12.2 | Base de datos |
| CDI | Jakarta | Inyección de dependencias |
| Maven | 3.x | Build y dependencias |
| Micrometer | 1.13.0 | Registro de métricas |
| InfluxDB | incluido en Docker | Repositorio de métricas |
| Grafana | incluido en Docker | Visualización de métricas |
| Docker | Desktop | Contenedor de observabilidad |

---

## Mocks de sistemas externos

El proyecto requiere dos mocks para funcionar correctamente:

| Mock | Repositorio | WAR generado |
|---|---|---|
| FacturaUTEMock | FacturacionUTE | `FacturaUTEMock.war` |
| ServicioMedioPagoMock | ServicioMedioPagoMock | `ServicioMedioPagoMock-1.0.0.war` |

### Actualizar los mocks

Si se modifica alguno de los mocks y se quiere actualizar el WAR en el repositorio, los tres proyectos deben estar en la misma carpeta padre:

    proyectos/
    ├── LaboratorioJavaEE/       ← core (este repositorio)
    ├── FacturacionUTE/          ← mock UTE
    └── ServicioMedioPagoMock/   ← mock medio de pago

Luego correr el script (`run.bat` o `run.sh`), que recompila los mocks, actualiza `mocks/` y levanta el servidor.

---

## Rate Limiter

El endpoint `verHistorico` está protegido por un rate limiter del tipo Token Bucket, implementado con la librería Bucket4j.

### Configuración

| Parámetro | Valor |
|---|---|
| Capacidad inicial | 10 tokens |
| Tasa de reposición | 5 tokens por segundo |
| Endpoint protegido | `GET /cargas/historico/{clienteId}` |

Cuando se supera el límite el servidor responde con HTTP `429 Too Many Requests`.

### Pruebas de carga

Se incluye el archivo `PlanPruebasRateLimiter.jmx` para probar el rate limiter con JMeter. Envía 15 requests por segundo durante 40 segundos, lo que permite observar claramente la transición de respuestas `200` a `429` en el gráfico *Response Codes per Second*.

---

## Problemas frecuentes

**`release version 21 not supported`**
→ El JDK instalado es menor a 21. El proyecto está configurado para Java 17. Verificar con `java -version` que sea Java 17.

**`PKIX path building failed`**
→ Error de certificado SSL al descargar dependencias de Maven. Generalmente se resuelve conectándose a otra red o desactivando el proxy/antivirus.

**Puerto 3306 en uso al instalar MariaDB**
→ MySQL ya está usando ese puerto. Durante la instalación de MariaDB elegir el puerto `3307`.

**`Access denied for user`**
→ Las credenciales de MariaDB no coinciden. Verificar que el usuario sea `root` y la contraseña `root`, y que el puerto sea `3307`.

**MariaDB no arranca tras cambiar el puerto (Linux)**
→ Revisar que no haya otro proceso en el 3307: `sudo ss -tlnp | grep 3307`. Ver logs: `sudo journalctl -u mariadb -n 50`.

**`mvn wildfly:run` usa Java incorrecta (Linux)**
→ Exportar `JAVA_HOME` apuntando a OpenJDK 17 (ver sección [Linux](#linux)).

**Las métricas no aparecen en Grafana**
→ Verificar que el contenedor Docker esté corriendo: `docker ps`. Verificar que la base `metricasTallerJava` exista en InfluxDB (Chronograf → Explore → `SHOW DATABASES`). Las métricas se envían cada 10 segundos, esperar al menos 15 segundos después de realizar una operación.