#!/bin/bash
set -e

echo ""
echo "=== Compilando mocks ==="
echo ""

if [ ! -d "../FacturacionUTE" ]; then
    echo "[ERROR] No se encontró la carpeta FacturacionUTE en el directorio padre."
    exit 1
fi
(cd ../FacturacionUTE && mvn clean package -q)
echo "[OK] FacturacionUTE compilado."

if [ ! -d "../ServicioMedioPagoMock" ]; then
    echo "[ERROR] No se encontró la carpeta ServicioMedioPagoMock en el directorio padre."
    exit 1
fi
(cd ../ServicioMedioPagoMock && mvn clean package -q)
echo "[OK] ServicioMedioPagoMock compilado."

cd "$(dirname "$0")"

echo ""
echo "=== Actualizando mocks en el proyecto ==="
echo ""

mkdir -p mocks
cp ../FacturacionUTE/target/FacturaUTEMock.war mocks/ && echo "[OK] FacturaUTEMock.war copiado."
cp ../ServicioMedioPagoMock/target/ServicioMedioPagoMock-1.0.0.war mocks/ && echo "[OK] ServicioMedioPagoMock-1.0.0.war copiado."

echo ""
echo "=== Levantando servidor ==="
echo ""

mvn wildfly:run