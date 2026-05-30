package database;

import model.StudentProfile;
import java.sql.*;

public class StudentDAO {

    // ── SAVE profile ─────────────────────────────────────────────
    public boolean saveProfile(StudentProfile profile) {
        try {
            // Clear existing profile first
            Connection conn = DatabaseConnection.getConnection();
            Statement  stmt = conn.createStatement();
            stmt.execute("DELETE FROM student_profile");
            stmt.close();

            String sql =
                "INSERT INTO student_profile " +
                "(name, student_id, semester) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, profile.getName());
            pstmt.setString(2, profile.getStudentId());
            pstmt.setString(3, profile.getSemester());

            int rows = pstmt.executeUpdate();
            pstmt.close();
            System.out.println("✅ Profile saved!");
            return rows > 0;

        } catch (Exception e) {
            System.out.println("❌ Profile save error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ── LOAD profile ─────────────────────────────────────────────
    public StudentProfile getProfile() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement  stmt = conn.createStatement();
            ResultSet  rs   = stmt.executeQuery(
                "SELECT * FROM student_profile LIMIT 1");

            if (rs.next()) {
                StudentProfile profile = new StudentProfile(
                    rs.getString("name"),
                    rs.getString("student_id"),
                    rs.getString("semester")
                );
                profile.setId(rs.getInt("id"));
                rs.close();
                stmt.close();
                return profile;
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("❌ Profile load error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}