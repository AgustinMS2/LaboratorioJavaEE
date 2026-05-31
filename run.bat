@echo off
setlocal

echo.
echo === Compilando mocks ===
echo.

cd ..\FacturacionUTE
if errorlevel 1 (
    echo [ERROR] No se encontro la carpeta FacturacionUTE en el directorio padre.
    exit /b 1
)
call mvn clean package -q
if errorlevel 1 (
    echo [ERROR] Fallo la compilacion de FacturacionUTE.
    exit /b 1
)

cd ..\ServicioMedioPagoMock
if errorlevel 1 (
    echo [ERROR] No se encontro la carpeta ServicioMedioPagoMock en el directorio padre.
    exit /b 1
)
call mvn clean package -q
if errorlevel 1 (
    echo [ERROR] Fallo la compilacion de ServicioMedioPagoMock.
    exit /b 1
)

cd ..\LaboratorioJavaEE

echo.
echo === Actualizando mocks en el proyecto ===
echo.

if not exist mocks mkdir mocks
copy /Y ..\FacturacionUTE\target\FacturaUTEMock.war mocks\ > nul && echo [OK] FacturaUTEMock.war copiado.
copy /Y ..\ServicioMedioPagoMock\target\ServicioMedioPagoMock-1.0.0.war mocks\ > nul && echo [OK] ServicioMedioPagoMock-1.0.0.war copiado.

echo.
echo === Levantando servidor ===
echo.

call mvn wildfly:run