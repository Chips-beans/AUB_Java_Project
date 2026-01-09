package GUI;

import DAO.TeacherDAO;
import Model.Teacher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TeacherFrm extends JFrame {
    private JButton addBtn;
    private JButton editBtn;
    private JButton deleteBtn;
    private JTextField searchTxt;
    private JButton searchBtn;
    private JTextField firstNameTxt;
    private JTextField lastNameTxt;
    private JFormattedTextField emailTxt;
    private JTextField phoneNumberTxt;
    private JFormattedTextField hireDateTxt;
    public JPanel Teacher_Pan;
    private JTable tableTeachers;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    public TeacherFrm(){
        this.setContentPane(Teacher_Pan);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        loadTableData("");

        try {
            // 1. Correct Mask for Date (YYYY-MM-DD)
            // '#' represents a number, '-' is a literal character
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(hireDateTxt); // For Teacher Hire Date

            // 2. For Email, just use a regular JTextField or a Validator
            // MaskFormatter is too rigid for emails because usernames have different lengths.
            emailTxt.setToolTipText("example@gmail.com");

        } catch (Exception e) {
            e.printStackTrace();
        }


        addBtn.addActionListener(e -> {
            String fName = firstNameTxt.getText();
            String lName = lastNameTxt.getText();
            String email = emailTxt.getText();
            String phone = phoneNumberTxt.getText();

            // Use your parseDate helper to get java.sql.Date
            java.sql.Date sqlHireDate = parseDate(hireDateTxt.getText());

            if (sqlHireDate == null) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Hire Date (YYYY-MM-DD)");
                return;
            }

            // Create teacher with 0 for ID (Auto-increment)
            Teacher t = new Teacher(0, fName, lName, email, phone, sqlHireDate);

            if (TeacherDAO.insert(t)) {
                JOptionPane.showMessageDialog(this, "Teacher Added!");
                loadTableData(""); // Refresh table
                clearTeacherFields();
            }
        });

            // 3. Edit Button Listener
        editBtn.addActionListener(e -> {
            int selectedRow = tableTeachers.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a teacher to edit");
                return;
            }

            int id = Integer.parseInt(tableTeachers.getValueAt(selectedRow, 0).toString());
            java.sql.Date sqlHireDate = parseDate(hireDateTxt.getText());

            Teacher t = new Teacher(
                    id,
                    firstNameTxt.getText(),
                    lastNameTxt.getText(),
                    emailTxt.getText(),
                    phoneNumberTxt.getText(),
                    sqlHireDate
            );

            if (TeacherDAO.update(t)) {
                JOptionPane.showMessageDialog(this, "Teacher Updated!");
                loadTableData("");
            }
        });

            // 4. Delete Button Listener
        deleteBtn.addActionListener(e -> {
            int selectedRow = tableTeachers.getSelectedRow();

            // 1. Check if a row is selected
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a teacher from the table to delete.");
                return;
            }

            // 2. Get the Teacher_ID from the first column (index 0)
            int id = Integer.parseInt(tableTeachers.getValueAt(selectedRow, 0).toString());

            // 3. Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this teacher record?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // 4. Execute delete via DAO
                if (TeacherDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Teacher deleted successfully.");
                    loadTableData(""); // Refresh table with empty keyword to show all
                    clearTeacherFields(); // Reset the text boxes
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Could not delete teacher record.");
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
        tableTeachers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableTeachers.getSelectedRow();
                if (row != -1) {
                    // Column 0 is Teacher_ID (usually kept for reference but not in a text box)

                    // Column 1: First_Name
                    firstNameTxt.setText(tableTeachers.getValueAt(row, 1).toString());

                    // Column 2: Last_Name
                    lastNameTxt.setText(tableTeachers.getValueAt(row, 2).toString());

                    // Column 3: Email
                    emailTxt.setText(tableTeachers.getValueAt(row, 3).toString());

                    // Column 4: Phone
                    phoneNumberTxt.setText(tableTeachers.getValueAt(row, 4).toString());

                    // Column 5: Hire_Date (Ensure this matches your ####-##-## mask)
                    hireDateTxt.setText(tableTeachers.getValueAt(row, 5).toString());
                }
            }
        });


    }
    private java.sql.Date parseDate(String dateStr) {
        // Check if the input is empty or just the mask placeholders
        if (dateStr == null || dateStr.trim().isEmpty() || dateStr.equals("____-__-__")) {
            return null;
        }

        try {
            // 1. Define the format matching your MaskFormatter (YYYY-MM-DD)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Ensures the date is real (e.g., prevents Feb 30)

            // 2. Parse the string into a utility Date
            java.util.Date utilDate = sdf.parse(dateStr);

            // 3. Convert to java.sql.Date for the Database/Model
            return new java.sql.Date(utilDate.getTime());

        } catch (java.text.ParseException e) {
            // If the user typed an invalid date, return null
            return null;
        }
    }
    private void loadTableData(String keyword) {
        // 1. Define 6 columns matching your DB schema
        String[] columns = {"ID", "First Name", "Last Name", "Email", "Phone", "Hire Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // 2. Fetch data from DAO
        java.util.List<Teacher> list = TeacherDAO.search(keyword);

        // 3. Populate rows
        for (Teacher t : list) {
            model.addRow(new Object[]{
                    t.getTeacherId(),
                    t.getFirstName(),
                    t.getLastName(),
                    t.getEmail(),
                    t.getPhone(),
                    t.getHireDate() // This is java.sql.Date from your model
            });
        }

        // 4. Set model to table
        tableTeachers.setModel(model);

        // 5. Optional: Set Column Widths
        tableTeachers.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
        tableTeachers.getColumnModel().getColumn(3).setPreferredWidth(150); // Email
    }

    private void clearTeacherFields() {
        // 1. Clear the standard text fields
        firstNameTxt.setText("");
        lastNameTxt.setText("");
        emailTxt.setText("");
        phoneNumberTxt.setText("");

        // 2. Reset the Hire Date field
        // If using a JFormattedTextField, setValue(null) clears the content while keeping the mask
        hireDateTxt.setValue(null);
        // If you are using a standard JTextField with a mask, use:
        // hireDateTxt.setText("");

        // 3. Clear the selection on the table
        // This prevents the user from accidentally editing the previous record
        tableTeachers.clearSelection();

        // 4. Optional: Set focus back to the first name field for the next entry
        firstNameTxt.requestFocus();
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TeacherFrm().setVisible(true);
        });
    }
}
