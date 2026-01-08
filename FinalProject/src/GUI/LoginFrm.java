package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.Login;
import DAO.LoginDAO;
import com.sun.tools.javac.Main;

public class LoginFrm extends JFrame{
    private JTextField txt_Username;
    private JPasswordField txt_Password;
    private JButton Btn_Submit;
    private JPanel Login_Pan;

    public LoginFrm() {
        this.setContentPane(Login_Pan);
        this.setTitle("Login Form");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Btn_Submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Authentication();
            }
        });

        this.pack();
        this.setLocationRelativeTo(null);
    }

    private void Authentication() {
        String username = txt_Username.getText();
        String password = txt_Password.getText();
        boolean loginSuccess = false;

        for (Login login : LoginDAO.getAll()) {
            // IMPORTANT: Use .equals() for passwords to maintain case sensitivity
            // Ensure LoginDAO.getAll() is calling the decrypt() method we made!
            if (login.getUsername().equalsIgnoreCase(username) && login.getPassword().equals(password)) {
                loginSuccess = true;
                break; // Stop looking once we find the user
            }
        }

        if (loginSuccess) {

            this.dispose();
            MainFrm main = new MainFrm();
            main.setVisible(true);
        } else {
            // Always provide feedback if the loop finishes without a match
            JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginFrm().setVisible(true);
        });
    }
}
