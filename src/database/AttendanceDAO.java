package database;

import model.AttendanceRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    // ── SAVE ─────────────────────────────────────────────────────
    public boolean saveRecord(AttendanceRecord record) {
        System.out.println("💾 Saving record...");
        String sql =
            "INSERT INTO attendance " +
            "(subject_name, total_classes, attended_classes, " +
            "required_percentage, attendance_percentage, " +
            "risk_level, suggestion) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection       conn  = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, record.getSubjectName());
            pstmt.setInt(2,    record.getTotalClasses());
            pstmt.setInt(3,    record.getAttendedClasses());
            pstmt.setDouble(4, record.getRequiredPercentage());
            pstmt.setDouble(5, record.getAttendancePercentage());
            pstmt.setString(6, record.getRiskLevel());
            pstmt.setString(7, record.getSuggestion());

            int rows = pstmt.executeUpdate();
            pstmt.close();
            System.out.println("✅ Saved! Rows: " + rows);
            return rows > 0;

        } catch (Exception e) {
            System.out.println("❌ Save error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ── LOAD ALL ─────────────────────────────────────────────────
    public List<AttendanceRecord> getAllRecords() {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql =
            "SELECT * FROM attendance ORDER BY created_at DESC";

        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement  stmt = conn.createStatement();
            ResultSet  rs   = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(new AttendanceRecord(
                    rs.getString("subject_name"),
                    rs.getInt("total_classes"),
                    rs.getInt("attended_classes"),
                    rs.getDouble("required_percentage"),
                    rs.getDouble("attendance_percentage"),
                    rs.getString("risk_level"),
                    rs.getString("suggestion")
                ));
            }
            rs.close();
            stmt.close();
            System.out.println("✅ Loaded " + list.size() + " records.");

        } catch (Exception e) {
            System.out.println("❌ Load error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // ── DELETE ALL ───────────────────────────────────────────────
    public boolean deleteAllRecords() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement  stmt = conn.createStatement();
            stmt.execute("DELETE FROM attendance");
            stmt.close();
            System.out.println("✅ History cleared.");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Delete error: " + e.getMessage());
            return false;
        }
    }
}