package GUI;

import DAO.StudentDAO;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

public class StudentFrm extends JFrame {
    private JTextField firstnameTxt;
    private JTextField lastNameTxt;
    private JFormattedTextField dobTxt;
    private JComboBox genderCmb;
    private JTextArea addressTxt;
    private JTextField gardianContactTxt;
    private JFormattedTextField admissionDateTxt;
    private JButton addBtn;
    private JButton editBtn;
    private JButton deleteBtn;
    private JButton searchBtn;
    private JTextField searchTxt;
    public JPanel Student_Pan;
    private JTable table1;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    public StudentFrm() {
        this.setContentPane(Student_Pan);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();

        // 1. Initial Load of Data
        loadTableData("");
        try {
            // Sets format to YYYY-MM-DD
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('*');
            dateMask.install(dobTxt);

            // Do the same for admission date if needed
            dateMask.install(admissionDateTxt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        genderCmb.removeAllItems();

        // Add your specific items
        genderCmb.addItem("Male");
        genderCmb.addItem("Female");
        genderCmb.addItem("Other");
        // 2. Add Button Listener
        addBtn.addActionListener(e -> {
            // 1. Collect String data
            String fName = firstnameTxt.getText();
            String lName = lastNameTxt.getText();
            String gender = genderCmb.getSelectedItem().toString();
            String address = addressTxt.getText();
            String contact = gardianContactTxt.getText();

            // 2. Parse Dates from Formatted Text Boxes
            java.util.Date utilDob = parseDate(dobTxt.getText());
            java.util.Date utilAdmission = parseDate(admissionDateTxt.getText());

            // Validation: Ensure dates are not empty or invalid
            if (utilDob == null || utilAdmission == null) {
                JOptionPane.showMessageDialog(this, "Please enter valid dates (YYYY-MM-DD)");
                return;
            }

            // 3. Conversion to java.sql.Date (Required for your Model/Database)
            java.sql.Date sqlDob = new java.sql.Date(utilDob.getTime());
            java.sql.Date sqlAdmission = new java.sql.Date(utilAdmission.getTime());

            // 4. Create the Student object using your 8-parameter constructor
            // Note: We pass '0' for studentId as the DB handles auto-increment
            Student s = new Student(
                    0,             // studentId
                    fName,         // firstName
                    lName,         // lastName
                    sqlDob,        // dob (sql.Date)
                    gender,        // gender
                    address,       // address
                    contact,       // guardianContact
                    sqlAdmission   // admissionDate (sql.Date)
            );

            if (StudentDAO.insert(s)) {
                JOptionPane.showMessageDialog(this, "Student Added Successfully!");
                loadTableData("");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Error saving to database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 3. Edit Button Listener
        editBtn.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();

            // 1. Check if a row is actually selected
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student from the table to edit.");
                return;
            }

            // 2. Get the ID from the selected row
            int id = Integer.parseInt(table1.getValueAt(selectedRow, 0).toString());

            // 3. Collect and Parse data from fields
            java.util.Date utilDob = parseDate(dobTxt.getText());
            java.util.Date utilAdmission = parseDate(admissionDateTxt.getText());

            if (utilDob == null || utilAdmission == null) {
                JOptionPane.showMessageDialog(this, "Please enter valid dates (YYYY-MM-DD)");
                return;
            }

            // 4. Convert to java.sql.Date
            java.sql.Date sqlDob = new java.sql.Date(utilDob.getTime());
            java.sql.Date sqlAdmission = new java.sql.Date(utilAdmission.getTime());

            // 5. Create the Student object using your 8-parameter constructor
            // Use the 'id' we got from the table here
            Student s = new Student(
                    id,
                    firstnameTxt.getText(),
                    lastNameTxt.getText(),
                    sqlDob,
                    genderCmb.getSelectedItem().toString(),
                    addressTxt.getText(),
                    gardianContactTxt.getText(),
                    sqlAdmission
            );

            // 6. Call the DAO
            if (StudentDAO.update(s)) {
                JOptionPane.showMessageDialog(this, "Student Updated Successfully!");
                loadTableData(""); // Refresh table to show changes
                clearFields();     // Optional: clear form after update
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 4. Delete Button Listener
        deleteBtn.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();

            // 1. Check if a row is selected before proceeding
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student from the table to delete.");
                return;
            }

            // 2. Get the Student_ID from the first column of the selected row
            int id = Integer.parseInt(table1.getValueAt(selectedRow, 0).toString());

            // 3. Ask for confirmation to prevent accidental deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this student record?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // 4. Call the DAO to execute the DELETE query
                if (StudentDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                    loadTableData(""); // Refresh the JTable from the database
                    clearFields();     // Reset the input fields
                } else {
                    // 5. Handle potential SQL errors (like foreign key constraints)
                    JOptionPane.showMessageDialog(this,
                            "Could not delete student. They might be linked to attendance or grades.",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 5. Search Button Listener
        searchBtn.addActionListener(e -> loadTableData(searchTxt.getText()));
        searchTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loadTableData(searchTxt.getText());
            }
        });

        // 6. Table Selection Listener (Click row to fill text fields)
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                if (row != -1) {
                    // Mapping columns to TextFields
                    // Column 0: ID (usually not editable, or hidden)
                    firstnameTxt.setText(table1.getValueAt(row, 1).toString());
                    lastNameTxt.setText(table1.getValueAt(row, 2).toString());

                    // Dates (Column 3 & 7) - Ensure these are strings in the table model
                    dobTxt.setText(table1.getValueAt(row, 3).toString());

                    genderCmb.setSelectedItem(table1.getValueAt(row, 4).toString());

                    // Address and Contact
                    // If address is not in the table, you might need to fetch it from the DAO by ID
                    addressTxt.setText(table1.getValueAt(row, 5).toString());
                    gardianContactTxt.setText(table1.getValueAt(row, 6).toString());

                    admissionDateTxt.setText(table1.getValueAt(row, 7).toString());
                }
            }
        });
    }
    private java.sql.Date parseDate(String dateStr) {
        try {
            // Parse the String into a utility Date
            java.util.Date utilDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            // Convert utility Date to SQL Date and RETURN it
            return new java.sql.Date(utilDate.getTime());
        } catch (java.text.ParseException e) {
            // If the date is invalid, the code won't crash
            return null;
        }
    }

    private void loadTableData(String keyword) {
        // 1. Define your 8 column headers
        String[] columns = {"ID", "First Name", "Last Name", "DOB", "Gender", "Address", "Contact", "Admission"};

        // 2. Create a model with these headers
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // 3. Fetch data from DAO
        java.util.List<Student> list = StudentDAO.search(keyword);

        for (Student s : list) {
            model.addRow(new Object[]{
                    s.getStudentId(),        // Col 0
                    s.getFirstName(),        // Col 1
                    s.getLastName(),         // Col 2
                    s.getDob(),              // Col 3
                    s.getGender(),           // Col 4
                    s.getAddress(),          // Col 5
                    s.getGuardianContact(),  // Col 6
                    s.getAdmissionDate()     // Col 7
            });
        }

        // 4. Set the model to the table
        table1.setModel(model);
        table1.getTableHeader().setFont(new Font("Sans Serif", Font.BOLD, 12));
        javax.swing.table.TableColumnModel columnModel = table1.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(5);  // ID
        columnModel.getColumn(1).setPreferredWidth(100); // First Name
        columnModel.getColumn(2).setPreferredWidth(100); // Last Name
        columnModel.getColumn(3).setPreferredWidth(90);  // DOB
        columnModel.getColumn(4).setPreferredWidth(60);  // Gender
        columnModel.getColumn(5).setPreferredWidth(200); // Address (Larger)
        columnModel.getColumn(6).setPreferredWidth(100); // Contact
        columnModel.getColumn(7).setPreferredWidth(90);
    }

    private Student getStudentFromFields() {
        // 1. Parse both dates using our helper method
        java.sql.Date sqlDob = parseDate(dobTxt.getText());
        java.sql.Date sqlAdmission = parseDate(admissionDateTxt.getText());

        // 2. Return the full Student object matching your 8-parameter constructor
        return new Student(
                0,                          // studentId (ignored during INSERT)
                firstnameTxt.getText(),     // firstName
                lastNameTxt.getText(),      // lastName
                sqlDob,                     // dob (java.sql.Date)
                genderCmb.getSelectedItem().toString(),
                addressTxt.getText(),
                gardianContactTxt.getText(),
                sqlAdmission                // admissionDate (java.sql.Date)
        );
    }

    private void clearFields() {
        firstnameTxt.setText("");
        lastNameTxt.setText("");
        addressTxt.setText("");
        gardianContactTxt.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentFrm().setVisible(true);
        });

    }
}
