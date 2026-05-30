package model;

public class AttendanceRecord {

    private int    id;
    private String subjectName;
    private int    totalClasses;
    private int    attendedClasses;
    private double requiredPercentage;
    private double attendancePercentage;
    private String riskLevel;
    private String suggestion;

    public AttendanceRecord(String subjectName,
                            int totalClasses,
                            int attendedClasses,
                            double requiredPercentage,
                            double attendancePercentage,
                            String riskLevel,
                            String suggestion) {
        this.subjectName          = subjectName;
        this.totalClasses         = totalClasses;
        this.attendedClasses      = attendedClasses;
        this.requiredPercentage   = requiredPercentage;
        this.attendancePercentage = attendancePercentage;
        this.riskLevel            = riskLevel;
        this.suggestion           = suggestion;
    }

    // ── Getters ──────────────────────────────────────────────────
    public int    getId()                      { return id; }
    public String getSubjectName()             { return subjectName; }
    public int    getTotalClasses()            { return totalClasses; }
    public int    getAttendedClasses()         { return attendedClasses; }
    public double getRequiredPercentage()      { return requiredPercentage; }
    public double getAttendancePercentage()    { return attendancePercentage; }
    public String getRiskLevel()               { return riskLevel; }
    public String getSuggestion()              { return suggestion; }

    // ── Setters ──────────────────────────────────────────────────
    public void setId(int id)                  { this.id = id; }
    public void setSubjectName(String s)       { this.subjectName = s; }
    public void setTotalClasses(int v)         { this.totalClasses = v; }
    public void setAttendedClasses(int v)      { this.attendedClasses = v; }
    public void setRequiredPercentage(double v){ this.requiredPercentage = v; }
    public void setAttendancePercentage(double v){ this.attendancePercentage = v; }
    public void setRiskLevel(String v)         { this.riskLevel = v; }
    public void setSuggestion(String v)        { this.suggestion = v; }
}