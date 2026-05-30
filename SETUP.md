# Setup Guide — Attendance Risk Calculator

## Prerequisites
- Java JDK 17 or above
- JavaFX SDK 21 (download from https://gluonhq.com/products/javafx/)
- SQLite JDBC Driver (download from https://github.com/xerial/sqlite-jdbc/releases)

## Setup Steps

### 1. Clone the repository
git clone https://github.com/yourusername/AttendanceRiskCalculator.git
cd AttendanceRiskCalculator

### 2. Create lib folder
mkdir lib

### 3. Download and place JavaFX SDK
- Download JavaFX SDK from https://gluonhq.com/products/javafx/
- Extract and place inside lib/ folder as lib/javafx-sdk/

### 4. Download SQLite JDBC Driver
- Download sqlite-jdbc-3.43.0.0.jar
- Place inside lib/ folder as lib/sqlite-jdbc-3.43.0.0.jar

### 5. Compile
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

### 6. Run
java -cp "out;lib\sqlite-jdbc-3.43.0.0.jar;lib\javafx-sdk\lib\*" ^
  --module-path "lib\javafx-sdk\lib" ^
  --add-modules javafx.controls,javafx.fxml ^
  ui.MainApp