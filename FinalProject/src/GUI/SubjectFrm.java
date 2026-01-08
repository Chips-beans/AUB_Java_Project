package GUI;

import DAO.SubjectDAO;
import DAO.TeacherDAO;
import Model.Subject;
import Model.Teacher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SubjectFrm extends JFrame{
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField searchTxt;
    private JButton searchButton;
    private JTextField subNameTxt;
    private JTextField subCodeTxt;
    private JTable subjectTable;
    public JPanel Subject_Pan;
    public SubjectFrm(){
        this.setContentPane(Subject_Pan);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        loadTableData("");

        addButton.addActionListener(e -> {
            String SubName = subNameTxt.getText();
            String SubCode = subCodeTxt.getText();

            // Create teacher with 0 for ID (Auto-increment)
            Subject s = new Subject(SubCode,SubName,  0);

            if (SubjectDAO.insert(s)) {
                JOptionPane.showMessageDialog(this, "Subject Added!");
                loadTableData(""); // Refresh table
                clearTeacherFields();
            }
        });

        // 3. Edit Button Listener
        editButton.addActionListener(e -> {
            int selectedRow = subjectTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a teacher to edit");
                return;
            }

            int id = Integer.parseInt(subjectTable.getValueAt(selectedRow, 0).toString());

            Subject s = new Subject(
                    subCodeTxt.getText(),
                    subNameTxt.getText(),
                    id
            );

            if (SubjectDAO.update(s)) {
                JOptionPane.showMessageDialog(this, "Subject Updated!");
                loadTableData("");
            }
        });

        // 4. Delete Button Listener
        deleteButton.addActionListener(e -> {
            int selectedRow = subjectTable.getSelectedRow();

            // 1. Check if a row is selected
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a teacher from the table to delete.");
                return;
            }

            // 2. Get the Teacher_ID from the first column (index 0)
            int id = Integer.parseInt(subjectTable.getValueAt(selectedRow, 0).toString());

            // 3. Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this teacher record?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // 4. Execute delete via DAO
                if (SubjectDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Subject deleted successfully.");
                    loadTableData(""); // Refresh table with empty keyword to show all
                    clearTeacherFields(); // Reset the text boxes
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Could not delete subject record.");
                }
            }
        });

        // 5. Search Button Listener
        searchButton.addActionListener(e -> loadTableData(searchTxt.getText()));
        searchTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loadTableData(searchTxt.getText());
            }
        });

        // 6. Table Selection Listener (Click row to fill text fields)
        subjectTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = subjectTable.getSelectedRow();
                if (row != -1) {


                    // Column 1: Subject Name
                    subNameTxt.setText(subjectTable.getValueAt(row, 1).toString());

                    // Column 2: Subject Code
                    subCodeTxt.setText(subjectTable.getValueAt(row, 2).toString());
                }
            }
        });

    }
    private void loadTableData(String keyword) {
        // 1. Define 6 columns matching your DB schema
        String[] columns = {"ID", "Subject Name", "Subject Code"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // 2. Fetch data from DAO
        java.util.List<Subject> list = SubjectDAO.search(keyword);

        // 3. Populate rows
        for (Subject s : list) {
            model.addRow(new Object[]{
                    s.getSubjectID(),
                    s.getSubjectName(),
                    s.getSubjectCode()
            });
        }

        // 4. Set model to table
        subjectTable.setModel(model);
    }

    private void clearTeacherFields() {
        // 1. Clear the standard text fields
        subNameTxt.setText("");
        subCodeTxt.setText("");
        subjectTable.clearSelection();
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrm().setVisible(true);
        });
    }
}
