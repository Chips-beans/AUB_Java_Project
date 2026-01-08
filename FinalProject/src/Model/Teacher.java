package Model;

import java.sql.Date;

public class Teacher {
    private int teacherId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date hireDate;

    // Constructor matching your 6 DB columns
    public Teacher(int teacherId, String firstName, String lastName, String email, String phone, Date hireDate) {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.hireDate = hireDate;
    }

    // Getters and Setters
    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Date getHireDate() { return hireDate; }
}
