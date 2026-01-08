package GUI;

import DAO.SchoolClassDAO;
import DAO.TeacherDAO;
import Model.SchoolClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SchoolClassFrm extends JFrame {
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField search_txt;
    private JButton searchButton;
    private JTextField className_txt;
    private JComboBox teacherID_cmb;
    private JTextField roomNumber_txt;
    private JTable class_table;
    public JPanel SchoolClass_Pan;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    public SchoolClassFrm() {
        this.setContentPane(SchoolClass_Pan);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Class Management");
        this.pack();

        // 1. Initialize Teacher IDs
        fillTeacherIdCombo();

        // 2. Load Table Data
        loadTableData("");

        // 3. ADD Button
        addButton.addActionListener(e -> {
            String name = className_txt.getText();
            String room = roomNumber_txt.getText();

            if (name.isEmpty() || teacherID_cmb.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please fill Class Name and Select a Teacher.");
                return;
            }

            int teacherId = Integer.parseInt(teacherID_cmb.getSelectedItem().toString());
            SchoolClass sc = new SchoolClass(0, name, teacherId, room);

            if (SchoolClassDAO.insert(sc)) {
                JOptionPane.showMessageDialog(this, "Class Created Successfully!");
                loadTableData("");
                clearFields();
            }
        });

        // 4. EDIT Button
        editButton.addActionListener(e -> {
            int row = class_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a class to update.");
                return;
            }

            int classId = Integer.parseInt(class_table.getValueAt(row, 0).toString());
            int teacherId = Integer.parseInt(teacherID_cmb.getSelectedItem().toString());

            SchoolClass sc = new SchoolClass(classId, className_txt.getText(), teacherId, roomNumber_txt.getText());

            if (SchoolClassDAO.update(sc)) {
                JOptionPane.showMessageDialog(this, "Class Updated!");
                loadTableData("");
                clearFields();
            }
        });

        // 5. DELETE Button
        deleteButton.addActionListener(e -> {
            int row = class_table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(class_table.getValueAt(row, 0).toString());
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this class?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (SchoolClassDAO.delete(id)) {
                        loadTableData("");
                        clearFields();
                    }
                }
            }
        });

        // 6. Search Functionality
        searchButton.addActionListener(e -> loadTableData(search_txt.getText()));
        search_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loadTableData(search_txt.getText());
            }
        });

        // 7. Table Selection
        class_table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = class_table.getSelectedRow();
                if (row != -1) {
                    className_txt.setText(class_table.getValueAt(row, 1).toString());
                    teacherID_cmb.setSelectedItem(class_table.getValueAt(row, 2).toString());
                    roomNumber_txt.setText(class_table.getValueAt(row, 3).toString());
                }
            }
        });
    }

    private void fillTeacherIdCombo() {
        teacherID_cmb.removeAllItems();
        // Assuming TeacherDAO has a method getAllTeacherIDs similar to StudentDAO
        List<Integer> ids = TeacherDAO.getAllTeacherIDs();
        for (Integer id : ids) {
            teacherID_cmb.addItem(id.toString());
        }
    }

    private void loadTableData(String keyword) {
        String[] columns = {"Class ID", "Class Name", "Teacher ID", "Room No"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        List<SchoolClass> list = SchoolClassDAO.search(keyword);

        for (SchoolClass sc : list) {
            model.addRow(new Object[]{
                    sc.getClassId(),
                    sc.getClassName(),
                    sc.getTeacherId(),
                    sc.getRoomNumber()
            });
        }
        class_table.setModel(model);
    }

    private void clearFields() {
        className_txt.setText("");
        roomNumber_txt.setText("");
        if (teacherID_cmb.getItemCount() > 0) teacherID_cmb.setSelectedIndex(0);
        class_table.clearSelection();
    }
    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrm().setVisible(true);
        });
    }
}

