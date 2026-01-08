package Model;

import java.sql.Date;

public class Attendance {
    private int attendanceId;
    private int studentId;
    private int classId;
    private Date attendanceDate;
    private String status;

    // Constructor
    public Attendance(int attendanceId, int studentId, int classId, Date attendanceDate, String status) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.classId = classId;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    // Getters and Setters
    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public Date getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(Date attendanceDate) { this.attendanceDate = attendanceDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}