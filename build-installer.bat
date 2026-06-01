@echo off
REM ============================================================
REM  build-installer.bat - Packages MazeProgram into a real
REM                       Windows .exe INSTALLER (uses WiX)
REM
REM  Output: dist\MazeProgram-1.0.0.exe
REM          (run it once to install MazeProgram permanently,
REM           with Start menu shortcut + uninstaller entry)
REM
REM  Requirements:
REM    - JDK 14+ on PATH (you have JDK 25)
REM    - WiX Toolset 3.x installed and on PATH
REM      Download: https://github.com/wixtoolset/wix3/releases
REM      (install wix311.exe, then ADD its bin folder to PATH:
REM       typically C:\Program Files (x86)\WiX Toolset v3.11\bin)
REM    - MazeProgram.jar and myRuntime\ present
REM ============================================================

setlocal

cd /d "%~dp0"

echo.
echo === Checking prerequisites ===

where jpackage >nul 2>&1
if errorlevel 1 (
    echo ERROR: jpackage was not found on PATH.
    echo        Install JDK 14 or newer and add its bin folder to PATH.
    pause
    exit /b 1
)

where light.exe >nul 2>&1
if errorlevel 1 (
    echo ERROR: WiX Toolset was not found on PATH.
    echo.
    echo        Download from: https://github.com/wixtoolset/wix3/releases
    echo        Install wix311.exe, then add the WiX bin folder to PATH:
    echo          C:\Program Files ^(x86^)\WiX Toolset v3.11\bin
    echo.
    echo        After updating PATH, open a NEW terminal and try again.
    echo.
    echo        Or, if you just want a portable folder ^(no installer^),
    echo        run build.bat instead - it doesn't need WiX.
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
    pause
    exit /b 1
)

echo OK.

echo.
echo === Cleaning previous installer build ===
if exist "build-input" rmdir /s /q "build-input"
REM Note: we don't wipe dist\ here - build.bat output (the portable
REM folder) may live alongside the installer. jpackage just writes
REM dist\MazeProgram-1.0.0.exe and leaves other files alone.

echo.
echo === Staging input ===
mkdir "build-input"
copy /y "MazeProgram.jar" "build-input\" >nul

echo.
echo === Running jpackage (this may take 1-2 minutes) ===
jpackage ^
  --type exe ^
  --name MazeProgram ^
  --app-version 1.0.0 ^
  --vendor "Vivaan" ^
  --description "Maze generator and solver - AP CSA project" ^
  --input build-input ^
  --main-jar MazeProgram.jar ^
  --main-class MazeRunner ^
  --runtime-image myRuntime ^
  --win-console ^
  --win-shortcut ^
  --win-menu ^
  --win-menu-group "MazeProgram" ^
  --win-dir-chooser ^
  --win-per-user-install ^
  --dest dist

if errorlevel 1 (
    echo.
    echo ERROR: jpackage failed.
    echo        Common causes:
    echo          - WiX toolset version mismatch ^(use WiX 3.x, not 4.x^)
    echo          - WiX bin folder not on PATH in this terminal
    echo          - Antivirus blocking the installer build
    rmdir /s /q "build-input"
    pause
    exit /b 1
)

rmdir /s /q "build-input"

echo.
echo ============================================================
echo  SUCCESS!
echo.
echo  Your installer is at:  dist\MazeProgram-1.0.0.exe
echo.
echo  To install:    double-click MazeProgram-1.0.0.exe
echo  To uninstall:  Windows Settings ^> Apps ^> MazeProgram ^> Uninstall
echo.
echo  This installs PER-USER ^(no admin needed^). After install,
echo  MazeProgram appears in the Start menu under "MazeProgram".
echo.
echo  To share, send the single .exe file - the recipient runs it
echo  once and the app is installed on their machine.
echo ============================================================
echo.
pause
