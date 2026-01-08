package GUI;

import DAO.AttendanceDAO;
import DAO.SchoolClassDAO;
import DAO.StudentDAO;
import Model.Attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class AttendanceFrm extends JFrame {
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField search_txt;
    private JButton searchButton;
    private JComboBox studentID_cmb;
    private JComboBox classID_cmb;
    private JComboBox status_cmb;
    private JTable Attendance_table;
    public JPanel Attendance_Pan;
    private JFormattedTextField attendanceDate_txt;

    public AttendanceFrm() {
        this.setContentPane(Attendance_Pan);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Attendance Management");
        this.pack();

        // 1. Initialize Components
        fillStudentIdCombo();
        fillClassIDCombo();
        setupStatusCombo();
        setupDateMask();

        // 2. Set Current Date Automatically
        setCurrentDate();

        // 3. Load Data
        loadTableData("All");

        // 4. ADD Button
        addButton.addActionListener(e -> {
            try {
                int studentId = Integer.parseInt(studentID_cmb.getSelectedItem().toString());
                int classId = Integer.parseInt(classID_cmb.getSelectedItem().toString());
                java.sql.Date date = parseDate(attendanceDate_txt.getText());
                String status = status_cmb.getSelectedItem().toString();

                Attendance a = new Attendance(0, studentId, classId, date, status);
                if (AttendanceDAO.insert(a)) {
                    JOptionPane.showMessageDialog(this, "Attendance Recorded!");
                    loadTableData("All");
                    clearFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // 5. EDIT Button
        editButton.addActionListener(e -> {
            int row = Attendance_table.getSelectedRow();
            if (row == -1) return;

            int id = Integer.parseInt(Attendance_table.getValueAt(row, 0).toString());
            Attendance a = new Attendance(
                    id,
                    Integer.parseInt(studentID_cmb.getSelectedItem().toString()),
                    Integer.parseInt(classID_cmb.getSelectedItem().toString()),
                    parseDate(attendanceDate_txt.getText()),
                    status_cmb.getSelectedItem().toString()
            );

            if (AttendanceDAO.update(a)) {
                loadTableData("All");
                clearFields();
            }
        });

        // 6. DELETE Button
        deleteButton.addActionListener(e -> {
            int row = Attendance_table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(Attendance_table.getValueAt(row, 0).toString());
                if (JOptionPane.showConfirmDialog(this, "Delete record?", "Confirm", 0) == 0) {
                    if (AttendanceDAO.delete(id)) {
                        loadTableData("All");
                        clearFields();
                    }
                }
            }
        });

        // 7. Table Click Listener
        Attendance_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = Attendance_table.getSelectedRow();
                if (row != -1) {
                    studentID_cmb.setSelectedItem(Attendance_table.getValueAt(row, 1).toString());
                    classID_cmb.setSelectedItem(Attendance_table.getValueAt(row, 2).toString());
                    attendanceDate_txt.setText(Attendance_table.getValueAt(row, 3).toString());
                    status_cmb.setSelectedItem(Attendance_table.getValueAt(row, 4).toString());
                }
            }
        });

        // 8. Search/Filter
        searchButton.addActionListener(e -> loadTableData(search_txt.getText()));
    }

    private void setCurrentDate() {
        // Automatically picks up the current system date
        attendanceDate_txt.setText(LocalDate.now().toString());
    }

    private void setupDateMask() {
        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(attendanceDate_txt);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void setupStatusCombo() {
        status_cmb.removeAllItems();
        status_cmb.addItem("Present");
        status_cmb.addItem("Absent");
        status_cmb.addItem("Late");
    }

    private void fillStudentIdCombo() {
        studentID_cmb.removeAllItems();
        List<Integer> ids = StudentDAO.getAllStudentIDs();
        for (Integer id : ids) studentID_cmb.addItem(id.toString());
    }

    private void fillClassIDCombo() {
        classID_cmb.removeAllItems();
        List<Integer> ids = SchoolClassDAO.getAllClassIDs();
        for (Integer id : ids) classID_cmb.addItem(id.toString());
    }

    private void loadTableData(String filter) {
        String[] columns = {"ID", "Student ID", "Class ID", "Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        int searchId = 0;
        if (!filter.equals("All") && !filter.isEmpty()) {
            try { searchId = Integer.parseInt(filter); } catch (Exception e) {}
        }

        List<Attendance> list = AttendanceDAO.searchByStudent(searchId);
        for (Attendance a : list) {
            model.addRow(new Object[]{
                    a.getAttendanceId(), a.getStudentId(), a.getClassId(),
                    a.getAttendanceDate(), a.getStatus()
            });
        }
        Attendance_table.setModel(model);
    }

    private void clearFields() {
        setCurrentDate(); // Reset to current date after clearing
        if (studentID_cmb.getItemCount() > 0) studentID_cmb.setSelectedIndex(0);
        if (classID_cmb.getItemCount() > 0) classID_cmb.setSelectedIndex(0);
        status_cmb.setSelectedIndex(0);
        Attendance_table.clearSelection();
    }

    private java.sql.Date parseDate(String dateStr) {
        try {
            return java.sql.Date.valueOf(dateStr); // Converts YYYY-MM-DD string to SQL Date
        } catch (Exception e) { return null; }
    }
    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrm().setVisible(true);
        });
    }
}
