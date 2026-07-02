#!/usr/bin/env bash
#
# pruebas.sh - corre las pruebas funcionales del proyecto (coleccion de Postman)
#
# Deja la base limpia, ejecuta la carpeta "Pruebas funcionales" con newman
# y muestra el resultado.
#
#   ./pruebas.sh            # limpia los datos y corre las pruebas
#   ./pruebas.sh --no-reset # corre sin limpiar la base

set -uo pipefail

DB_CONTAINER="${DB_CONTAINER:-tallerjava-mariadb}"
DB_NAME="${DB_NAME:-tallerJava}"
DB_USER="${DB_USER:-root}"
DB_PASS="${DB_PASS:-root}"
APP_URL="${APP_URL:-http://localhost:8080/LaboratorioJavaEE/gestion}"

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
COLLECTION="$DIR/TallerJava-GestorMovilidad.postman_collection.json"
FOLDER="Pruebas funcionales"

RESET=1
[ "${1:-}" = "--no-reset" ] && RESET=0

echo "== Verificando que la app este arriba =="
if ! curl -s -o /dev/null -w "%{http_code}" "$APP_URL/clientes" | grep -q 200; then
  echo "  La app no responde en $APP_URL. Levantar el servidor con: mvn wildfly:run"
  exit 1
fi
if ! docker exec "$DB_CONTAINER" mariadb -u"$DB_USER" -p"$DB_PASS" -e "SELECT 1;" >/dev/null 2>&1; then
  echo "  La base no responde. Arrancar el contenedor: docker start $DB_CONTAINER"
  exit 1
fi
echo "  OK"

if [ "$RESET" -eq 1 ]; then
  echo "== Limpiando la base $DB_NAME =="
  TABLES=$(docker exec "$DB_CONTAINER" mariadb -u"$DB_USER" -p"$DB_PASS" -N -B \
    -e "SELECT table_name FROM information_schema.tables WHERE table_schema='$DB_NAME';" 2>/dev/null)
  if [ -n "$TABLES" ]; then
    STMT="SET FOREIGN_KEY_CHECKS=0;"
    while IFS= read -r t; do [ -n "$t" ] && STMT="$STMT TRUNCATE TABLE \`$t\`;"; done <<< "$TABLES"
    STMT="$STMT SET FOREIGN_KEY_CHECKS=1;"
    docker exec "$DB_CONTAINER" mariadb -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -e "$STMT" 2>/dev/null \
      && echo "  Tablas vaciadas" || { echo "  No se pudo limpiar la base"; exit 1; }
  fi
fi

echo "== Corriendo pruebas =="
npx --yes newman run "$COLLECTION" --folder "$FOLDER" --reporters cli
EXIT=$?

echo
if [ "$EXIT" -eq 0 ]; then
  echo "Todas las pruebas pasaron."
else
  echo "Hubo pruebas que fallaron. Si son errores de cliente ya existente o ids,"
  echo "correr de nuevo dejando que limpie la base (sin --no-reset)."
fi
exit "$EXIT"
