package GUI;

import DAO.ChangePasswordDAO;
import DAO.LoginDAO;
import Model.ChangePassword;
import Model.Login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CPasswordFrm extends JFrame {
    private JTextField usernameTxt;
    private JPasswordField oldPasswordTxt;
    private JPasswordField newPasswordTxt;
    private JButton submitBtn;
    public JPanel CPass_Pan;

    public CPasswordFrm() {
        this.setContentPane(CPass_Pan);
        this.setTitle("Change Password");
        // Change to DISPOSE so it doesn't kill the whole app if opened from Main
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add the action listener to the button
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePasswordLogic(); // Renamed to avoid confusion with the Model
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
    }
    private void ChangePasswordLogic() {
        // Fix: Use new String() to get the actual characters from the password fields
        String user = usernameTxt.getText();
        String oldPass = new String(oldPasswordTxt.getPassword());
        String newPass = new String(newPasswordTxt.getPassword());

        // Basic validation
        if (user.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ChangePassword data = new ChangePassword(user, oldPass, newPass);

        // Calling your DAO
        boolean isSuccess = ChangePasswordDAO.ChangePassword(data);

        if (isSuccess) {
            JOptionPane.showMessageDialog(this, "Password changed successfully!");
            usernameTxt.setText("");
            oldPasswordTxt.setText("");
            newPasswordTxt.setText("");

        } else {
            JOptionPane.showMessageDialog(this, "Process failed. Please check your old password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new CPasswordFrm().setVisible(true);
        });
    }
}
