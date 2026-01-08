package Model;

public class Subject {
    private int subjectID;
    private String subjectName;
    private String subjectCode;

    public Subject(String subjectCode, String subjectName, int subjectID) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectID = subjectID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }
}
