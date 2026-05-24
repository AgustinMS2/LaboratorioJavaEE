# Gestión de Movilidad Eléctrica — Taller Java 2026

Sistema de gestión de cargas para vehículos eléctricos, desarrollado con Jakarta EE 10 sobre WildFly, siguiendo una arquitectura monolítica modular.

---

## Índice

1. [Descripción del sistema](#descripción-del-sistema)
2. [Arquitectura](#arquitectura)
3. [Estructura de paquetes](#estructura-de-paquetes)
4. [Modelo de dominio](#modelo-de-dominio)
5. [Módulos y casos de uso](#módulos-y-casos-de-uso)
6. [Configuración del entorno](#configuración-del-entorno)
   - [Linux](#linux)
   - [Windows](#windows)
7. [Cómo correr el proyecto](#cómo-correr-el-proyecto)
8. [Tecnologías](#tecnologías)
9. [Problemas frecuentes](#problemas-frecuentes)

---

## Descripción del sistema

El sistema permite gestionar la carga de vehículos eléctricos en estaciones distribuidas. Los clientes pueden iniciar y finalizar cargas, gestionar sus medios de pago y realizar reclamos. El sistema procesa los pagos automáticamente al finalizar cada carga.

---

## Arquitectura

El proyecto sigue una **arquitectura monolítica modular**, donde cada módulo está diseñado para ser independiente y potencialmente evolucionar hacia un microservicio.

Los módulos se comunican entre sí únicamente a través de las interfaces definidas en sus paquetes `aplicacion/`, respetando el principio de bajo acoplamiento. Cuando `moduloCargas` necesita cobrar un pago, invoca la interfaz `ServicioPago` del `moduloPagos` sin conocer su implementación interna.

```
┌──────────────────────────────────────────────────────────────┐
│                         WildFly 27                           │
│                                                              │
│  ┌───────────────┐   ┌───────────────┐   ┌───────────────┐  │
│  │moduloClientes │   │ moduloCargas  │   │ moduloPagos   │  │
│  │               │   │               │──▶│               │  │
│  │  ServicioClien│   │  ServicioCarga│   │  ServicioPago │  │
│  └───────────────┘   └───────────────┘   └───────────────┘  │
│          │                   │                   │           │
│          └───────────────────┴───────────────────┘           │
│                              │                               │
│                    ┌─────────▼────────┐                      │
│                    │    MariaDB       │                      │
│                    │   tallerJava     │                      │
│                    └──────────────────┘                      │
└──────────────────────────────────────────────────────────────┘
```

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
Cliente (abstract)                          MedioPago (abstract)
├── cedula: String                          ├── Tarjeta
├── nombreCompleto: String                  │   ├── numero: String
├── telefono: String                        │   ├── fechaVencimiento: LocalDate
├── contrasena: String                      │   ├── digitoVerificacion: String
├── mediosPago: List<MedioPago>             │   └── tipo: TipoTarjeta
└── reclamos: List<Reclamo>                 └── CuentaUTE
    ├── ClienteComun                                └── numeroCuenta: String
    └── ClienteProfesional
        ├── tipo: TipoProfesional
        └── porcentajeDescuento: float

EstacionCarga                               Cargador
├── descripcion: String                     ├── tipo: TipoCargador
├── calle: String                           ├── tieneCable: boolean
├── departamento: String                    ├── tipoConector: TipoConector
├── longitud: int                           ├── estado: EstadoCargador
├── latitud: int                            ├── tiempoEstimadoFinalizacion: LocalDateTime *
└── cargadores: List<Cargador>              ├── fechaEstimadaReparacion: LocalDate **
                                            └── potenciaMinima: int ***

Carga                                       Pago
├── clienteId: Long                         ├── clienteId: Long
├── cargador: Cargador                      ├── cargaId: Long
├── fecha: LocalDate                        ├── medioPagoId: Long
├── horaInicio: LocalDateTime               ├── importe: Double
├── horaFin: LocalDateTime                  ├── fecha: LocalDateTime
├── importeTotal: float                     └── estado: String
├── recargoPorDemora: float
├── porcentajeAvance: int  (0..100) *
├── horaEstimadaFin: LocalDateTime *
└── estado: EstadoCarga

* solo si estado = ACTIVA
** solo si estado = FUERA_SERVICIO
*** solo si tipo = RAPIDO
```

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

## Configuración del entorno

### Requisitos

| Herramienta | Versión | Notas |
|---|---|---|
| Java | **17** (Temurin/OpenJDK) | No usar Java 21 |
| Maven | 3.x | |
| WildFly | 27.0.1 | Se descarga automáticamente |
| MariaDB | 10.x o superior | Puerto **3307**, usuario `root`, contraseña `root` |

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
