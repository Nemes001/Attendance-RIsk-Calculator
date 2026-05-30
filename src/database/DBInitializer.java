package database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBInitializer {

    public static void initialize() {
        System.out.println("🔧 Initializing database...");

        String attendanceTable = """
                CREATE TABLE IF NOT EXISTS attendance (
                    id                    INTEGER PRIMARY KEY AUTOINCREMENT,
                    subject_name          TEXT    NOT NULL,
                    total_classes         INTEGER NOT NULL,
                    attended_classes      INTEGER NOT NULL,
                    required_percentage   REAL    NOT NULL,
                    attendance_percentage REAL    NOT NULL,
                    risk_level            TEXT    NOT NULL,
                    suggestion            TEXT    NOT NULL,
                    created_at            DATETIME DEFAULT CURRENT_TIMESTAMP
                );
                """;

        String profileTable = """
                CREATE TABLE IF NOT EXISTS student_profile (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    name        TEXT NOT NULL,
                    student_id  TEXT NOT NULL,
                    semester    TEXT NOT NULL
                );
                """;

        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement  stmt = conn.createStatement();

            stmt.execute(attendanceTable);
            System.out.println("✅ Attendance table ready!");

            stmt.execute(profileTable);
            System.out.println("✅ Profile table ready!");

            stmt.close();

        } catch (Exception e) {
            System.out.println("❌ DB Init failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}