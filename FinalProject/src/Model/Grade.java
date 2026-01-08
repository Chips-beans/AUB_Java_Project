package Model;

import java.sql.Date;

public class Grade {
    private int gradeId;
    private int studentId;
    private int subjectId;
    private double marksObtained;
    private Date examDate;
    private String examType;

    // Constructor
    public Grade(int gradeId, int studentId, int subjectId, double marksObtained, Date examDate, String examType) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.marksObtained = marksObtained;
        this.examDate = examDate;
        this.examType = examType;
    }

    // Getters and Setters
    public int getGradeId() { return gradeId; }
    public void setGradeId(int gradeId) { this.gradeId = gradeId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(double marksObtained) { this.marksObtained = marksObtained; }

    public Date getExamDate() { return examDate; }
    public void setExamDate(Date examDate) { this.examDate = examDate; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }
}