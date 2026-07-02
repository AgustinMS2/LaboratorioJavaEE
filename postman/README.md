# Pruebas de la API

Colección de Postman para probar las funcionalidades del Gestor de Movilidad.

La colección tiene las carpetas por módulo (Clientes, Pagos, Cargas) como referencia
de los endpoints, y una carpeta **Pruebas funcionales** que recorre los flujos completos
del sistema y valida las respuestas.

## Qué recorren las pruebas

- Registro de clientes y alta de medios de pago (tarjeta y cuenta UTE)
- Alta de estaciones y cargadores
- Iniciar y finalizar una carga
- Pago con tarjeta (aprobado), pago con cuenta UTE y pago rechazado
- Bloqueo del cliente por deuda pendiente (debe devolver 409)
- Reclamos (negativo, positivo y neutro)

## Requisitos

- El proyecto corriendo (`mvn wildfly:run` con Java 17) en `http://localhost:8080`.
- MariaDB en el puerto 3307 (contenedor `tallerjava-mariadb`, usuario/clave `root`).
- Para correr por consola: `node`/`npx` y `docker`.

## Cómo correr

### Con el script

Desde esta carpeta:

```bash
./pruebas.sh
```

Limpia la base, corre la carpeta *Pruebas funcionales* y muestra el resultado.
Con `./pruebas.sh --no-reset` corre sin limpiar.

### Desde Postman

1. **Import** → `TallerJava-GestorMovilidad.postman_collection.json`.
2. Limpiar la base antes de correr (ver abajo).
3. Click derecho en la carpeta **Pruebas funcionales** → **Run folder**.

Las pruebas se corren **en orden** y sobre una **base limpia**: el alta de medio de pago
no devuelve el id, así que se referencian por orden de creación. Si la base ya tiene datos,
el registro del cliente falla con `400` (cédula repetida).

## Limpiar la base

Vacía las tablas y reinicia los ids (no hace falta reiniciar el servidor):

```bash
docker exec tallerjava-mariadb mariadb -uroot -proot tallerJava -e "
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE clientes_cliente_clientes_medioPago;
TRUNCATE TABLE clientes_cliente_clientes_reclamo;
TRUNCATE TABLE clientes_clienteComun;
TRUNCATE TABLE clientes_clienteProfesional;
TRUNCATE TABLE clientes_tarjeta;
TRUNCATE TABLE clientes_cuentaUTE;
TRUNCATE TABLE clientes_medioPago;
TRUNCATE TABLE clientes_reclamo;
TRUNCATE TABLE clientes_cliente;
TRUNCATE TABLE cargas_estacionCarga_cargas_cargador;
TRUNCATE TABLE cargas_carga;
TRUNCATE TABLE cargas_cargador;
TRUNCATE TABLE cargas_estacionCarga;
TRUNCATE TABLE pagos_pago;
SET FOREIGN_KEY_CHECKS=1;"
```

(El script `pruebas.sh` ya hace esto solo.)

## Notas

- Las tarjetas de prueba: `4111111111111111` siempre se aprueba y `4000000000000002`
  siempre se rechaza; cualquier otra es aleatoria.
- La carpeta *Pruebas funcionales* usa dos clientes propios (cédulas `41111111` y `42222222`)
  y variables con prefijo `pf_` para no pisar las de las otras carpetas.
