package GUI;

import DAO.LoginDAO;
import Model.Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CreateUser extends JFrame {
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField search_txt;
    private JButton searchButton;
    private JTextField username_txt;
    private JPasswordField password_txt;
    private JTextField role_txt;
    private JTable user_tbl;
    public JPanel CreateUser_Pan;

    public CreateUser() {
        // 1. Load Table
        loadTable();

        // 2. ADD Button Logic
        addButton.addActionListener(e -> {
            String user = username_txt.getText();
            String pass = new String(password_txt.getPassword());
            String role = role_txt.getText(); // Better as a JComboBox

            if(user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty");
                return;
            }

            Login newLogin = new Login(user, pass, role);
            if(LoginDAO.insert(newLogin)) {
                JOptionPane.showMessageDialog(this, "User Created!");
                loadTable();
            }
        });

        // 3. DELETE Button Logic
        deleteButton.addActionListener(e -> {
            int row = user_tbl.getSelectedRow();
            if(row != -1) {
                String targetUser = user_tbl.getValueAt(row, 0).toString();
                if(LoginDAO.delete(targetUser)) {
                    loadTable();
                }
            }
        });
        // Inside your CreateUser class constructor:

        // 1. Fill fields when a row is clicked
        user_tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = user_tbl.getSelectedRow();
                if (row != -1) {
                    username_txt.setText(user_tbl.getValueAt(row, 0).toString());
                    role_txt.setText(user_tbl.getValueAt(row, 1).toString());
                    // We leave password empty for security; the admin types a new one to change it
                    password_txt.setText("");
                    username_txt.setEditable(false); // Username shouldn't be changed as it is our ID
                }
            }
        });

        // 2. Edit Button Logic
        editButton.addActionListener(e -> {
            String user = username_txt.getText();
            String pass = new String(password_txt.getPassword());
            String role = role_txt.getText();

            if (pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a new or existing password to confirm changes.");
                return;
            }

            Login updatedUser = new Login(user, pass, role);
            if (LoginDAO.update(updatedUser)) {
                JOptionPane.showMessageDialog(this, "User updated successfully!");
                loadTable();
                clearFields();
            }
        });
    }
    private void clearFields() {
        username_txt.setText("");
        password_txt.setText("");
        role_txt.setText("");
        username_txt.setEditable(true); // Re-enable for new entries
        user_tbl.clearSelection();
    }

    private void loadTable() {
        String[] cols = {"Username", "Role"}; // Don't show passwords in the table!
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for(Login l : LoginDAO.getAll()) {
            model.addRow(new Object[]{l.getUsername(), l.getRole()});
        }
        user_tbl.setModel(model);
    }
    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CreateUser().setVisible(true);
        });
    }
}
