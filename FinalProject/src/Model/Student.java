package Model;

import java.sql.Date;

public class Student {
    private int studentId;
    private String firstName;
    private String lastName;
    private Date dob;
    private String gender;
    private String address;
    private String guardianContact;
    private Date admissionDate;

    public Student(int studentId, String firstName, String lastName, Date dob, String gender, String address, String guardianContact, Date admissionDate) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.guardianContact = guardianContact;
        this.admissionDate = admissionDate;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGuardianContact(String guardianContact) {
        this.guardianContact = guardianContact;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public String getGuardianContact() {
        return guardianContact;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public Date getDob() {
        return dob;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getStudentId() {
        return studentId;
    }
}
