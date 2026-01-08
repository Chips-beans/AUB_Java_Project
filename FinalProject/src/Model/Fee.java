package Model;

import java.sql.Date;

public class Fee {
    private int feeId;
    private int studentId;
    private double amountDue;
    private double amountPaid;
    private Date dueDate;
    private String status;

    // Constructor
    public Fee(int feeId, int studentId, double amountDue, double amountPaid, Date dueDate, String status) {
        this.feeId = feeId;
        this.studentId = studentId;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and Setters
    public int getFeeId() { return feeId; }
    public void setFeeId(int feeId) { this.feeId = feeId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public double getAmountDue() { return amountDue; }
    public void setAmountDue(double amountDue) { this.amountDue = amountDue; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
