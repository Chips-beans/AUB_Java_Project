package GUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrm extends JFrame {
    private JButton studentBtn;
    private JButton changePasswordBtn;
    private JButton logoutBtn;
    private JPanel leftPan;
    private JPanel centerPan;
    private JPanel rightPan;
    private JPanel footerPan;
    private JLabel headerPan;
    private JButton teacherBtn;
    private JButton subjectBtn;
    private JButton feeButton;
    private JButton gradeButton;
    private JButton classButton;
    private JButton enrollmentButton;
    private JButton attendanceButton;
    private JDesktopPane contPan;

    public MainFrm() {

        contPan = new JDesktopPane();

        contPan.setLayout(new BorderLayout());

        //add header, footer, left and right
        add(headerPan, BorderLayout.NORTH);
        add(footerPan, BorderLayout.SOUTH);
        add(leftPan, BorderLayout.WEST);
        add(rightPan, BorderLayout.EAST);
        add(centerPan, BorderLayout.CENTER);
        JInternalFrame frm = new JInternalFrame();

        add(contPan);

        setTitle("Main From");
        setFocusableWindowState(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        leftPan.setPreferredSize(new Dimension(160, 1080));
        Dimension btnSize = new Dimension(150, 33);

        for (Component Btn : leftPan.getComponents()){
            JButton b = (JButton) Btn;
            b.setPreferredSize(btnSize);
        }

        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Exit", 1) == JOptionPane.YES_OPTION) {
                    MainFrm.this.dispose();

                    LoginFrm login = new LoginFrm();

                    login.pack();
                    login.setLocationRelativeTo(null);

                    login.setVisible(true);
                }

            }
        });
        changePasswordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChildForm(frm,new CPasswordFrm().CPass_Pan);
            }
        });
        studentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChildForm(frm, new StudentFrm().Student_Pan);
            }
        });
        teacherBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChildForm(frm, new TeacherFrm().Teacher_Pan);
            }
        });
        subjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChildForm(frm, new SubjectFrm().Subject_Pan);
            }
        });
        feeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChildForm(frm, new FeeFrm().Fee_Pan);
            }
        });
        gradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChildForm(frm, new GradeFrm().Grade_Pan);
            }
        });
        enrollmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChildForm(frm, new EnrollmentFrm().Enrollment_Pan);
            }
        });
        classButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChildForm(frm, new SchoolClassFrm().SchoolClass_Pan);
            }
        });
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openChildForm(frm, new AttendanceFrm().Attendance_Pan);
            }
        });

    }
    private void openChildForm(JInternalFrame frm, JPanel mainPan) {
        // 1. Clear previous content
        contPan.removeAll();

        // 2. Setup the Internal Frame
        frm.setContentPane(mainPan);
        frm.setBorder(null); // Removes the border for a full-screen look

        // 3. Remove the title bar/header
        BasicInternalFrameUI ui = (BasicInternalFrameUI) frm.getUI();
        ui.setNorthPane(null);

        // 4. Add to the container
        contPan.add(frm);

        // 5. THE FIX: Wait for the layout to settle, then maximize
        SwingUtilities.invokeLater(() -> {
            try {
                // This forces the frame to fill the entire contPan area
                frm.setMaximum(true);
                frm.setSize(contPan.getSize());
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
            frm.setVisible(true);
            contPan.revalidate();
            contPan.repaint();
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrm().setVisible(true);
        });

    }

}
