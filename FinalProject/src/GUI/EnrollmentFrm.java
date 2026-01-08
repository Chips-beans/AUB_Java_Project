package GUI;

import DAO.SchoolClassDAO;
import DAO.EnrollmentDAO;
import DAO.StudentDAO;
import Model.Enrollment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EnrollmentFrm extends JFrame {
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField search_txt;
    private JButton searchButton;
    private JComboBox studentid_cmb;
    private JComboBox classid_cmb;
    private JFormattedTextField enrDate_txt;
    private JComboBox status_cmb;
    private JTable enr_table;
    public JPanel Enrollment_Pan;
    private JComboBox searchStudentID_cmb;

    public EnrollmentFrm(){
        this.setContentPane(Enrollment_Pan);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Enrollment Management");
        this.pack();

        // 1. Initialize IDs and Status
        fillStudentIdCombo();
        fillClassIDCombo();
        setupStatusCombo();

        // 2. Setup Date Mask
        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(enrDate_txt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. Initial Load (Display All)
        loadTableData("All");

        // 4. Filter Listener
        searchStudentID_cmb.addActionListener(e -> {
            if (searchStudentID_cmb.getSelectedItem() != null) {
                loadTableData(searchStudentID_cmb.getSelectedItem().toString());
            }
        });

        // 5. ADD Button
        addButton.addActionListener(e -> {
            try {
                if(studentid_cmb.getSelectedItem().equals("All")){
                    JOptionPane.showMessageDialog(this, "Select a specific Student ID");
                    return;
                }

                int studentId = Integer.parseInt(studentid_cmb.getSelectedItem().toString());
                int classId = Integer.parseInt(classid_cmb.getSelectedItem().toString());
                java.sql.Date enrDate = parseDate(enrDate_txt.getText());
                String status = status_cmb.getSelectedItem().toString();

                Enrollment enr = new Enrollment(0, studentId, classId, enrDate, status);
                if (EnrollmentDAO.insert(enr)) {
                    JOptionPane.showMessageDialog(this, "Enrollment Added!");
                    loadTableData("All");
                    clearFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding enrollment: " + ex.getMessage());
            }
        });

        // 6. EDIT Button
        editButton.addActionListener(e -> {
            int row = enr_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a record to update.");
                return;
            }

            int id = Integer.parseInt(enr_table.getValueAt(row, 0).toString());
            Enrollment enr = new Enrollment(
                    id,
                    Integer.parseInt(studentid_cmb.getSelectedItem().toString()),
                    Integer.parseInt(classid_cmb.getSelectedItem().toString()),
                    parseDate(enrDate_txt.getText()),
                    status_cmb.getSelectedItem().toString()
            );

            if (EnrollmentDAO.update(enr)) {
                JOptionPane.showMessageDialog(this, "Updated successfully!");
                loadTableData("All");
                clearFields();
            }
        });

        // 7. DELETE Button
        deleteButton.addActionListener(e -> {
            int row = enr_table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(enr_table.getValueAt(row, 0).toString());
                if (JOptionPane.showConfirmDialog(this, "Delete record?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                    if (EnrollmentDAO.delete(id)) {
                        loadTableData("All");
                        clearFields();
                    }
                }
            }
        });

        // 8. Table Mouse Listener
        enr_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = enr_table.getSelectedRow();
                if (row != -1) {
                    studentid_cmb.setSelectedItem(enr_table.getValueAt(row, 1).toString());
                    classid_cmb.setSelectedItem(enr_table.getValueAt(row, 2).toString());
                    enrDate_txt.setText(enr_table.getValueAt(row, 3).toString());
                    status_cmb.setSelectedItem(enr_table.getValueAt(row, 4).toString());
                }
            }
        });
    }
    private void setupStatusCombo() {
        status_cmb.removeAllItems();
        status_cmb.addItem("Active");
        status_cmb.addItem("Graduated");
        status_cmb.addItem("Dropped");
    }

    private void fillStudentIdCombo() {
        studentid_cmb.removeAllItems();
        searchStudentID_cmb.removeAllItems();

        studentid_cmb.addItem("All");
        searchStudentID_cmb.addItem("All");

        List<Integer> ids = StudentDAO.getAllStudentIDs();
        for (Integer id : ids) {
            studentid_cmb.addItem(id.toString());
            searchStudentID_cmb.addItem(id.toString());
        }
    }

    private void fillClassIDCombo() {
        classid_cmb.removeAllItems();
        // You need ClassDAO.getAllClassIDs() similar to StudentDAO
        List<Integer> ids = SchoolClassDAO.getAllClassIDs();
        for (Integer id : ids) {
            classid_cmb.addItem(id.toString());
        }
    }

    private void loadTableData(String filterId) {
        String[] columns = {"Enr ID", "Student ID", "Class ID", "Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        int searchId = 0;
        if (!filterId.equals("All")) {
            try { searchId = Integer.parseInt(filterId); } catch (Exception e) {}
        }

        List<Enrollment> list = EnrollmentDAO.searchByStudent(searchId);
        for (Enrollment en : list) {
            model.addRow(new Object[]{
                    en.getEnrollmentId(), en.getStudentId(), en.getClassId(),
                    en.getEnrollmentDate(), en.getStatus()
            });
        }
        enr_table.setModel(model);
    }

    private void clearFields() {
        enrDate_txt.setValue(null);
        if (studentid_cmb.getItemCount() > 0) studentid_cmb.setSelectedIndex(0);
        if (classid_cmb.getItemCount() > 0) classid_cmb.setSelectedIndex(0);
        if (status_cmb.getItemCount() > 0) status_cmb.setSelectedIndex(0);
        enr_table.clearSelection();
    }

    private java.sql.Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.equals("____-__-__")) return null;
        try {
            return new java.sql.Date(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr).getTime());
        } catch (Exception e) { return null; }
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrm().setVisible(true);
        });
    }
}

