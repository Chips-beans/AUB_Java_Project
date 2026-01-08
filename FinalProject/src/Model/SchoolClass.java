package Model;

public class SchoolClass {
    private int classId;
    private String className;
    private int teacherId;
    private String roomNumber;

    // Constructor for creating new records (ID 0 for auto-increment)
    public SchoolClass(int classId, String className, int teacherId, String roomNumber) {
        this.classId = classId;
        this.className = className;
        this.teacherId = teacherId;
        this.roomNumber = roomNumber;
    }

    // Getters and Setters
    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}