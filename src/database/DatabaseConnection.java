package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;

public class DatabaseConnection {

    private static Connection connection = null;

    // ── Hardcoded to D:\Java_pro — guaranteed to work ────────────
    private static final String DB_PATH = "D:\\Java_pro\\attendance.db";
    private static final String URL     = "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() throws Exception {
        try {
            // Load SQLite driver explicitly
            Class.forName("org.sqlite.JDBC");

            if (connection == null || connection.isClosed()) {
                System.out.println("🔌 Connecting to: " + URL);

                // Check if file exists
                File dbFile = new File(DB_PATH);
                System.out.println("📁 DB exists: " + dbFile.exists());
                System.out.println("📁 Parent writable: "
                    + dbFile.getParentFile().canWrite());

                connection = DriverManager.getConnection(URL);
                System.out.println("✅ DB Connected!");
            }
            return connection;

        } catch (ClassNotFoundException e) {
            System.out.println("❌ SQLite driver not found!");
            System.out.println("   Make sure sqlite-jdbc jar is in lib folder");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ DB Connection closed.");
            }
        } catch (Exception e) {
            System.out.println("❌ Close error: " + e.getMessage());
        }
    }
}