package GUI;

import DAO.GradeDAO;
import DAO.StudentDAO;
import DAO.SubjectDAO;
import Model.Grade;
import Model.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GradeFrm extends JFrame{
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField search_txt;
    private JComboBox searchStudentID_cmb;
    private JComboBox studentID_cmb;
    private JComboBox subjectID_cmb;
    private JTextField score_txt;
    private JTextField examType_txt;
    private JFormattedTextField examDate_txt;
    private JTable grade_table;
    public JPanel Grade_Pan;
    public GradeFrm() {
        this.setContentPane(Grade_Pan);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Grade Management");
        this.pack();

        // 1. Initialize IDs from Database
        fillStudentIdCombo();
        fillSubjectIdCombo();

        // 2. Setup Date Mask
        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(examDate_txt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadTableData("");

        // 3. ADD Button
        addButton.addActionListener(e -> {
            try {
                int studentId = Integer.parseInt(studentID_cmb.getSelectedItem().toString());
                int subjectId = Integer.parseInt(subjectID_cmb.getSelectedItem().toString());
                double marks = Double.parseDouble(score_txt.getText());
                java.sql.Date examDate = parseDate(examDate_txt.getText());
                String type = examType_txt.getText(); // Taking text from teacher's input

                if (examDate == null || type.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
                    return;
                }

                Grade g = new Grade(0, studentId, subjectId, marks, examDate, type);
                if (GradeDAO.insert(g)) {
                    JOptionPane.showMessageDialog(this, "Grade Added Successfully!");
                    loadTableData("");
                    clearFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Marks must be numeric.");
            }
        });
        editButton.addActionListener(e -> {
            int row = grade_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a record to update.");
                return;
            }

            int gradeId = Integer.parseInt(grade_table.getValueAt(row, 0).toString());
            Grade g = new Grade(
                    gradeId,
                    Integer.parseInt(studentID_cmb.getSelectedItem().toString()),
                    Integer.parseInt(subjectID_cmb.getSelectedItem().toString()),
                    Double.parseDouble(score_txt.getText()),
                    parseDate(examDate_txt.getText()),
                    examType_txt.getText()
            );

            if (GradeDAO.update(g)) {
                JOptionPane.showMessageDialog(this, "Grade Updated!");
                loadTableData("");
                clearFields();
            }
        });

        // 5. DELETE Button (Existing logic)
        deleteButton.addActionListener(e -> {
            int row = grade_table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(grade_table.getValueAt(row, 0).toString());
                if (JOptionPane.showConfirmDialog(this, "Delete record?", "Confirm", 0) == 0) {
                    if (GradeDAO.delete(id)) {
                        loadTableData("");
                        clearFields();
                    }
                }
            }
        });
        grade_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = grade_table.getSelectedRow();
                if (row != -1) {
                    studentID_cmb.setSelectedItem(grade_table.getValueAt(row, 1).toString());
                    subjectID_cmb.setSelectedItem(grade_table.getValueAt(row, 2).toString());
                    score_txt.setText(grade_table.getValueAt(row, 3).toString());
                    examDate_txt.setText(grade_table.getValueAt(row, 4).toString());
                    examType_txt.setText(grade_table.getValueAt(row, 5).toString());
                }
            }
        });
        // Add this after fillStudentIdCombo() in your constructor
        searchStudentID_cmb.addActionListener(e -> {
            if (searchStudentID_cmb.getSelectedItem() != null) {
                String selectedId = searchStudentID_cmb.getSelectedItem().toString();
                // Trigger the table reload with the selected Student ID
                loadTableData(selectedId);
            }
        });
    }
    private void fillStudentIdCombo() {
        studentID_cmb.removeAllItems();
        studentID_cmb.addItem("All"); // Add a default option to show everything

        searchStudentID_cmb.removeAllItems();
        searchStudentID_cmb.addItem("All");
        List<Integer> ids = StudentDAO.getAllStudentIDs();
        for (Integer id : ids) {
            searchStudentID_cmb.addItem(id.toString());
            studentID_cmb.addItem(id.toString());
        }
    }

    private void fillSubjectIdCombo() {
        subjectID_cmb.removeAllItems();
        // Assuming SubjectDAO has a search method that returns List<Subject>
        List<Subject> subjects = SubjectDAO.search("");
        for (Subject s : subjects) {
            subjectID_cmb.addItem(String.valueOf(s.getSubjectID()));
        }
    }
    private void clearFields() {
        score_txt.setText("");
        examType_txt.setText("");
        examDate_txt.setValue(null);
        if (studentID_cmb.getItemCount() > 0) studentID_cmb.setSelectedIndex(0);
        if (subjectID_cmb.getItemCount() > 0) subjectID_cmb.setSelectedIndex(0);
        grade_table.clearSelection();
    }

    private java.sql.Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.equals("____-__-__")) return null;
        try {
            return new java.sql.Date(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr).getTime());
        } catch (Exception e) { return null; }
    }

    private void loadTableData(String keyword) {
        String[] columns = {"Grade ID", "Student ID", "Subject ID", "Marks", "Exam Date", "Exam Type"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // This handles searching by Student ID specifically
        int searchId = 0;
        try { searchId = keyword.isEmpty() ? 0 : Integer.parseInt(keyword); } catch (Exception e) {}

        List<Grade> list = GradeDAO.searchByStudent(searchId);

        for (Grade g : list) {
            model.addRow(new Object[]{
                    g.getGradeId(), g.getStudentId(), g.getSubjectId(),
                    g.getMarksObtained(), g.getExamDate(), g.getExamType()
            });
        }
        grade_table.setModel(model);
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GradeFrm().setVisible(true);
        });
    }
}
