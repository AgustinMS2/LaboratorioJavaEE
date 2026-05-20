# Gestión de Movilidad Eléctrica — Taller Java 2026

Sistema de gestión de cargas para vehículos eléctricos, desarrollado con Jakarta EE 10 sobre WildFly, siguiendo una arquitectura modular inspirada en microservicios.

---

## Índice

1. [Descripción del sistema](#descripción-del-sistema)
2. [Arquitectura](#arquitectura)
3. [Estructura de paquetes](#estructura-de-paquetes)
4. [Modelo de dominio](#modelo-de-dominio)
5. [Módulos y casos de uso](#módulos-y-casos-de-uso)
6. [Configuración del entorno](#configuración-del-entorno)
7. [Cómo correr el proyecto](#cómo-correr-el-proyecto)

---

## Descripción del sistema

El sistema permite gestionar la carga de vehículos eléctricos en estaciones distribuidas. Los clientes pueden iniciar y finalizar cargas, gestionar sus medios de pago y realizar reclamos. El sistema procesa los pagos automáticamente al finalizar cada carga.

---

## Arquitectura

El proyecto sigue una **arquitectura monolítica modular**, donde cada módulo está diseñado para ser independiente y potencialmente evolucionar hacia un microservicio.

Los módulos se comunican entre sí únicamente a través de interfaces definidas en el paquete `aplicacion/`, respetando el principio de bajo acoplamiento.

```
┌─────────────────────────────────────────────────────────┐
│                      WildFly 27                         │
│                                                         │
│   ┌──────────────┐  ┌──────────────┐  ┌─────────────┐  │
│   │moduloClientes│  │ moduloCargas │  │moduloPagos  │  │
│   └──────┬───────┘  └──────┬───────┘  └──────┬──────┘  │
│          │                 │ invoca           │         │
│          └─────────────────┴──────────────────┘         │
│                            │                            │
│                     ┌──────▼──────┐                     │
│                     │  MariaDB    │                     │
│                     │ tallerJava  │                     │
│                     └─────────────┘                     │
└─────────────────────────────────────────────────────────┘
```

---

## Estructura de paquetes

Cada módulo sigue la misma estructura interna:

```
moduloXxx/
├── dominio/
│   ├── repositorio/       → interfaces de persistencia
│   └── Entidad.java       → clases de dominio con lógica de negocio
├── aplicacion/
│   ├── ServicioXxx.java   → interfaz del servicio (casos de uso)
│   └── impl/
│       └── ServicioXxxImpl.java  → implementación (@Transactional)
├── interfase/             → interfaces remotas e inter-módulo
└── infraestructura/
    └── persistencia/
        └── XxxRepositorioImpl.java  → implementación JPA
```

### Diagrama de paquetes

```
org.tallerJava
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
│   └── infraestructura / persistencia
│       ├── CargaRepositorioImpl
│       ├── CargadorRepositorioImpl
│       └── EstacionCargaRepositorioImpl
│
└── moduloPagos
    ├── dominio
    │   ├── repositorio
    │   │   └── PagoRepositorio
    │   └── Pago
    ├── aplicacion
    │   ├── ServicioPago
    │   └── impl / ServicioPagoImpl
    ├── interfase
    └── infraestructura / persistencia
        └── PagoRepositorioImpl
```

---

## Modelo de dominio

```
Cliente (abstract)
├── cedula: String
├── nombreCompleto: String
├── telefono: String
├── contrasena: String
├── mediosPago: List<MedioPago>
└── reclamos: List<Reclamo>
    │
    ├── ClienteComun
    └── ClienteProfesional
        ├── tipo: TipoProfesional
        └── porcentajeDescuento: float

MedioPago (abstract)
    ├── Tarjeta
    │   ├── numero: String
    │   ├── fechaVencimiento: LocalDate
    │   ├── digitoVerificacion: String
    │   └── tipo: TipoTarjeta
    └── CuentaUTE
        └── numeroCuenta: String

EstacionCarga
├── descripcion: String
├── calle: String
├── departamento: String
├── longitud: int
├── latitud: int
└── cargadores: List<Cargador>

Cargador
├── tipo: TipoCargador
├── tieneCable: boolean
├── tipoConector: TipoConector
├── estado: EstadoCargador
├── tiempoEstimadoFinalizacion: LocalDateTime  (solo si OCUPADO)
├── fechaEstimadaReparacion: LocalDate         (solo si FUERA_SERVICIO)
└── potenciaMinima: int                        (solo si RAPIDO)

Carga
├── clienteId: Long
├── cargador: Cargador
├── fecha: LocalDate
├── horaInicio: LocalDateTime
├── horaFin: LocalDateTime
├── importeTotal: float
├── recargoPorDemora: float
├── porcentajeAvance: int   (0..100, solo si ACTIVA)
├── horaEstimadaFin: LocalDateTime (solo si ACTIVA)
└── estado: EstadoCarga

Pago
├── clienteId: Long
├── cargaId: Long
├── medioPagoId: Long
├── importe: Double
├── fecha: LocalDateTime
└── estado: String
```

---

## Módulos y casos de uso

### Módulo Clientes
| Operación | Descripción | Consumidor |
|---|---|---|
| `registrarCliente(cliente)` | Registra un nuevo cliente | App móvil |
| `altaMedioPago(clienteId, medioPago)` | Agrega un medio de pago al cliente | App móvil |
| `obtenerClientes()` | Devuelve todos los clientes | Gestor web |
| `realizarReclamo(clienteId, comentario)` | Registra un reclamo del cliente | App móvil |

### Módulo Cargas
| Operación | Descripción | Consumidor |
|---|---|---|
| `iniciarCarga(clienteId, cargadorId, medioPagoId)` | Inicia una carga | App móvil |
| `verCargaActual(clienteId)` | Devuelve la carga activa | App móvil |
| `verHistorico(clienteId, desde, hasta)` | Histórico de cargas por rango de fechas | App móvil |
| `finalizarCarga(cargadorId, cargaId, consumo, recargo)` | Finaliza la carga y dispara el pago | Cargador |
| `altaEstacion(estacion)` | Da de alta una estación | Gestor web |
| `altaCargador(estacionId, cargadorId)` | Asocia un cargador a una estación | Gestor web |
| `obtenerEstaciones()` | Lista estaciones y cargadores disponibles | App móvil |

### Módulo Pagos
| Operación | Descripción | Consumidor |
|---|---|---|
| `pagarCarga(clienteId, cargaId, importe, medioPagoId)` | Cobra la carga al cliente | Módulo Cargas |
| `consultarPagos(clienteId, desde, hasta)` | Lista pagos del cliente | Gestor web |

---

## Configuración del entorno

### Requisitos
- Java 21
- Maven 3.x
- WildFly 27.0.1 (se descarga automáticamente con Maven)
- MariaDB 12.2 — puerto **3307**, usuario **root**, contraseña **root**

### Instalar MariaDB
1. Descargar desde [https://mariadb.org/download/](https://mariadb.org/download/) — **Windows MSI Package**
2. Durante la instalación:
   - Contraseña de root: `root`
   - Puerto: `3307` (para no chocar con MySQL si lo tenés instalado)

### Crear la base de datos
Una sola vez, ejecutar:
```bash
mysql -u root -proot -P 3307 -e "CREATE DATABASE IF NOT EXISTS tallerJava CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```
> El cliente `mysql` está en `C:\Program Files\MariaDB 12.2\bin\`

### JAR del driver
El archivo `mariadb-java-client-3.3.3.jar` debe estar en la **raíz del proyecto**. Ya está commiteado en el repositorio.

---

## Cómo correr el proyecto

```bash
mvn wildfly:run
```

Esto:
1. Descarga y levanta WildFly automáticamente
2. Ejecuta el `config.cli` que registra el driver MariaDB y crea el datasource
3. Deploya la aplicación
4. Hibernate crea las tablas automáticamente en `tallerJava`

> La primera vez puede tardar varios minutos porque descarga WildFly.

Para detener el servidor: `Ctrl + C`

---

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Jakarta EE | 10 | Framework principal |
| WildFly | 27.0.1 | Servidor de aplicaciones |
| Hibernate | (incluido en WildFly) | ORM / JPA |
| MariaDB | 12.2 | Base de datos |
| CDI | (Jakarta) | Inyección de dependencias |
| Maven | 3.x | Build y gestión de dependencias |
