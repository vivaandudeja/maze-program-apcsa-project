@echo off
REM ============================================================
REM  build.bat - Packages MazeProgram into a portable .exe
REM
REM  Output: dist\MazeProgram\MazeProgram.exe
REM          (a self-contained folder you can zip and share)
REM
REM  Requirements:
REM    - JDK 14+ installed and on PATH (you already have JDK 25)
REM    - MazeProgram.jar present in this folder
REM    - myRuntime\ folder present (your jlink runtime)
REM ============================================================

setlocal

cd /d "%~dp0"

echo.
echo === Checking prerequisites ===

where jpackage >nul 2>&1
if errorlevel 1 (
    echo ERROR: jpackage was not found on PATH.
    echo        Install JDK 14 or newer and make sure its bin folder is on PATH.
    pause
    exit /b 1
)

if not exist "MazeProgram.jar" (
    echo ERROR: MazeProgram.jar not found in this folder.
    pause
    exit /b 1
)

if not exist "myRuntime\bin\java.exe" (
    echo ERROR: myRuntime folder not found or incomplete.
    echo        Expected: myRuntime\bin\java.exe
    pause
    exit /b 1
)

echo OK.

echo.
echo === Cleaning previous build ===
if exist "dist" rmdir /s /q "dist"
if exist "build-input" rmdir /s /q "build-input"

echo.
echo === Staging input ===
mkdir "build-input"
copy /y "MazeProgram.jar" "build-input\" >nul

echo.
echo === Running jpackage ===
jpackage ^
  --type app-image ^
  --name MazeProgram ^
  --app-version 1.0.0 ^
  --vendor "Vivaan" ^
  --input build-input ^
  --main-jar MazeProgram.jar ^
  --main-class MazeRunner ^
  --runtime-image myRuntime ^
  --win-console ^
  --dest dist

if errorlevel 1 (
    echo.
    echo ERROR: jpackage failed.
    rmdir /s /q "build-input"
    pause
    exit /b 1
)

rmdir /s /q "build-input"

echo.
echo ============================================================
echo  SUCCESS!
echo.
echo  Your portable app is in:   dist\MazeProgram\
echo  Double-click to run:       dist\MazeProgram\MazeProgram.exe
echo.
echo  To share, zip the entire dist\MazeProgram folder.
echo  Recipients just unzip and run MazeProgram.exe - no Java install needed.
echo ============================================================
echo.
pause
