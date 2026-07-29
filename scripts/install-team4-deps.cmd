@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "PROJECT_ROOT=%%~fI"
set "MICROSERVICES_DIR=%PROJECT_ROOT%\microservices"

if not exist "%MICROSERVICES_DIR%\pom.xml" (
  echo Could not find microservices pom.xml at:
  echo %MICROSERVICES_DIR%\pom.xml
  exit /b 1
)

echo Installing Team 4 microservice Maven dependencies...
echo Project root: %PROJECT_ROOT%
echo.

pushd "%MICROSERVICES_DIR%"
call mvn.cmd -U clean install -DskipTests
set "MVN_EXIT=%ERRORLEVEL%"
popd

if not "%MVN_EXIT%"=="0" (
  echo.
  echo Maven install failed with exit code %MVN_EXIT%.
  exit /b %MVN_EXIT%
)

echo.
echo Team 4 microservice dependencies installed successfully.
endlocal
