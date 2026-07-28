@echo off
REM Phone Usage Tracker - Build Validation Script (Windows)

echo.
echo ==========================================
echo Build Validation Script
echo ==========================================
echo.

setlocal enabledelayedexpansion
set ISSUES=0
set WARNINGS=0

echo [1/7] Checking Java version...
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found
    set /a ISSUES=ISSUES+1
) else (
    for /f "tokens=2" %%i in ('java -version 2^>^&1 ^| find "version"') do set JAVA_VER=%%i
    echo   Java version: !JAVA_VER!
    echo [OK] Java found
)
echo.

echo [2/7] Checking Gradle...
if not exist "gradlew.bat" (
    echo [WARNING] gradlew.bat not found
    if exist "gradlew" (
        echo [OK] Unix gradlew found
    ) else (
        echo [ERROR] Gradle wrapper not found
        set /a ISSUES=ISSUES+1
    )
) else (
    echo [OK] gradlew.bat found
)
echo.

echo [3/7] Checking build.gradle.kts files...
if exist "app\build.gradle.kts" (
    findstr /M "JavaVersion.VERSION_17" app\build.gradle.kts >nul
    if errorlevel 1 (
        echo [WARNING] Java version not set to 17
        set /a WARNINGS=WARNINGS+1
    ) else (
        echo [OK] Java 17 configured
    )
) else (
    echo [ERROR] app\build.gradle.kts not found
    set /a ISSUES=ISSUES+1
)
echo.

echo [4/7] Checking gradle.properties...
if exist "gradle.properties" (
    findstr /M "MaxPermSize" gradle.properties >nul
    if errorlevel 1 (
        echo [OK] No unsupported JVM options
    ) else (
        echo [ERROR] MaxPermSize found (not supported in Java 21)
        set /a ISSUES=ISSUES+1
    )
) else (
    echo [ERROR] gradle.properties not found
    set /a ISSUES=ISSUES+1
)
echo.

echo [5/7] Checking source files...
if exist "app\src\main\java\com\example\phoneusagetracker\MainActivity.kt" (
    echo [OK] MainActivity found
) else (
    echo [ERROR] MainActivity not found
    set /a ISSUES=ISSUES+1
)

if exist "app\src\main\AndroidManifest.xml" (
    echo [OK] AndroidManifest found
) else (
    echo [ERROR] AndroidManifest not found
    set /a ISSUES=ISSUES+1
)

if exist "app\src\main\res\layout\activity_main.xml" (
    echo [OK] Layout files found
) else (
    echo [ERROR] Layout files not found
    set /a ISSUES=ISSUES+1
)
echo.

echo [6/7] Checking Git status...
git status >nul 2>&1
if errorlevel 1 (
    echo [WARNING] Not a git repository
) else (
    git status --short | find /c /v "" >nul
    if errorlevel 1 (
        echo [OK] All changes committed
    ) else (
        echo [WARNING] You have uncommitted changes
        set /a WARNINGS=WARNINGS+1
    )
)
echo.

echo [7/7] Checking Android SDK compatibility...
findstr "compileSdk = 34" app\build.gradle.kts >nul
if errorlevel 1 (
    echo [WARNING] compileSdk not set to 34
    set /a WARNINGS=WARNINGS+1
) else (
    echo [OK] SDK 34 configured
)

findstr "minSdk = 24" app\build.gradle.kts >nul
if errorlevel 1 (
    echo [WARNING] minSdk not set to 24
    set /a WARNINGS=WARNINGS+1
) else (
    echo [OK] Min SDK 24 configured
)
echo.

echo ==========================================
echo Validation Results
echo ==========================================
echo.

if !ISSUES! equ 0 (
    echo [OK] No critical issues found!
) else (
    echo [ERROR] Found !ISSUES! critical issue(s)
)

if !WARNINGS! gtr 0 (
    echo [WARNING] Found !WARNINGS! warning(s)
)

echo.
if !ISSUES! gtr 0 (
    echo Fix the issues above before building
    pause
    exit /b 1
) else (
    echo Ready to build! You can now run:
    echo   gradlew.bat assembleDebug
    echo.
    echo Or push to GitHub:
    echo   git push origin main
    pause
    exit /b 0
)
