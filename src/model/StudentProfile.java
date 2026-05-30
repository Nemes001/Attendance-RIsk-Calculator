package model;

public class StudentProfile {

    private int    id;
    private String name;
    private String studentId;
    private String semester;

    public StudentProfile(String name, String studentId,
                          String semester) {
        this.name      = name;
        this.studentId = studentId;
        this.semester  = semester;
    }

    // ── Getters ──────────────────────────────────────────────────
    public int    getId()        { return id; }
    public String getName()      { return name; }
    public String getStudentId() { return studentId; }
    public String getSemester()  { return semester; }

    // ── Setters ──────────────────────────────────────────────────
    public void setId(int id)              { this.id = id; }
    public void setName(String name)       { this.name = name; }
    public void setStudentId(String sid)   { this.studentId = sid; }
    public void setSemester(String sem)    { this.semester = sem; }
}