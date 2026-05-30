@echo off
title Attendance Risk Calculator
color 0A

echo.
echo ============================================
echo    Attendance Risk Calculator v2.0.0
echo ============================================
echo.

REM ── Check if Java is installed ───────────────────────────────
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 17 or above
    pause
    exit /b 1
)

REM ── Check if JavaFX SDK exists ───────────────────────────────
if not exist "lib\javafx-sdk\lib" (
    echo ERROR: JavaFX SDK not found!
    echo Please download JavaFX SDK and place it in lib\javafx-sdk\
    echo Download from: https://gluonhq.com/products/javafx/
    pause
    exit /b 1
)

REM ── Check if SQLite JAR exists ───────────────────────────────
if not exist "lib\sqlite-jdbc-3.43.0.0.jar" (
    echo ERROR: SQLite JDBC JAR not found!
    echo Please download sqlite-jdbc-3.43.0.0.jar and place it in lib\
    echo Download from: https://github.com/xerial/sqlite-jdbc/releases
    pause
    exit /b 1
)

REM ── Create out folder if not exists ─────────────────────────
if not exist "out" mkdir out

REM ── Compile ──────────────────────────────────────────────────
echo Compiling source files...
echo.

javac -cp "lib\sqlite-jdbc-3.43.0.0.jar;lib\javafx-sdk\lib\*" ^
  --module-path "lib\javafx-sdk\lib" ^
  --add-modules javafx.controls,javafx.fxml ^
  -d out ^
  src\model\AttendanceRecord.java ^
  src\model\StudentProfile.java ^
  src\logic\AttendanceCalculator.java ^
  src\logic\RiskEvaluator.java ^
  src\logic\SuggestionEngine.java ^
  src\database\DatabaseConnection.java ^
  src\database\DBInitializer.java ^
  src\database\AttendanceDAO.java ^
  src\database\StudentDAO.java ^
  src\threads\SaveWorker.java ^
  src\threads\LoadWorker.java ^
  src\ui\MainController.java ^
  src\ui\HistoryPanel.java ^
  src\ui\ChartPanel.java ^
  src\ui\ProfilePanel.java ^
  src\ui\MainApp.java

REM ── Check if compile was successful ──────────────────────────
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check the errors above.
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo Starting Attendance Risk Calculator...
echo.

REM ── Run ──────────────────────────────────────────────────────
java -cp "out;lib\sqlite-jdbc-3.43.0.0.jar;lib\javafx-sdk\lib\*" ^
  --module-path "lib\javafx-sdk\lib" ^
  --add-modules javafx.controls,javafx.fxml ^
  ui.MainApp

REM ── If app crashes show error ─────────────────────────────────
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Application crashed!
    echo Please check the errors above.
    pause
)