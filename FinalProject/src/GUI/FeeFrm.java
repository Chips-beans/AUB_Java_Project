package GUI;

import DAO.FeeDAO;
import DAO.StudentDAO;
import Model.Fee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FeeFrm extends JFrame{
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTextField search_txt;
    private JButton searchButton;
    private JTextField amountDue_txt;
    private JComboBox studentid_cmb;
    private JFormattedTextField dueDate_txt;
    private JComboBox status_cmb;
    private JTable FeeDataTable;
    private JTextField amountPaid_txt;
    private JScrollPane scrollpan;
    public JPanel Fee_Pan;
    private JButton printReceiptButton;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
    public FeeFrm(){
        this.setContentPane(Fee_Pan);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();


        fillStudentIdCombo();

        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');
            dateMask.install(dueDate_txt); // For Teacher Hire Date

        } catch (Exception e) {
            e.printStackTrace();
        }

        loadTableData("");

        addButton.addActionListener(e -> {
            try {
                double due = Double.parseDouble(amountDue_txt.getText());
                double paid = Double.parseDouble(amountPaid_txt.getText());

                // Algorithm to calculate status
                String finalStatus;
                if (paid >= due && due > 0) {
                    finalStatus = "Paid";
                } else if (paid > 0 && paid < due) {
                    finalStatus = "Partial";
                } else {
                    finalStatus = "Unpaid";
                }

                int studentId = Integer.parseInt(studentid_cmb.getSelectedItem().toString());
                java.sql.Date sqlDate = parseDate(dueDate_txt.getText());

                // Pass finalStatus directly to the constructor
                Fee f = new Fee(0, studentId, due, paid, sqlDate, finalStatus);

                if (FeeDAO.insert(f)) {
                    JOptionPane.showMessageDialog(this, "Fee Record Added! Status: " + finalStatus);
                    loadTableData("");
                    clearFields();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric amounts.");
            }
        });

        // 4. Edit Button Logic
        editButton.addActionListener(e -> {
            int row = FeeDataTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a record to update.");
                return;
            }

            try {
                int feeId = Integer.parseInt(FeeDataTable.getValueAt(row, 0).toString());
                double due = Double.parseDouble(amountDue_txt.getText());
                double paid = Double.parseDouble(amountPaid_txt.getText());

                // Recalculate status based on new input
                String status = (paid >= due) ? "Paid" : (paid > 0 ? "Partial" : "Unpaid");

                Fee f = new Fee(feeId, Integer.parseInt(studentid_cmb.getSelectedItem().toString()),
                        due, paid, parseDate(dueDate_txt.getText()), status);

                if (FeeDAO.update(f)) {
                    JOptionPane.showMessageDialog(this, "Record Updated! New Status: " + status);
                    loadTableData("");
                    clearFields();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // 5. Delete Button Logic
        deleteButton.addActionListener(e -> {
            int selectedRow = FeeDataTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a record to delete.");
                return;
            }

            int id = Integer.parseInt(FeeDataTable.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this fee record?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (FeeDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Deleted!");
                    loadTableData("");
                    clearFields();
                }
            }
        });

        // 5. Search Button Listener
        searchButton.addActionListener(e -> loadTableData(search_txt.getText()));
        search_txt.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                loadTableData(search_txt.getText());
            }
        });

        // 6. Table Selection Listener (Click row to fill text fields)
        FeeDataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = FeeDataTable.getSelectedRow();
                if (row != -1) {
                    // Column 1 is Student_ID
                    studentid_cmb.setSelectedItem(FeeDataTable.getValueAt(row, 1).toString());

                    amountDue_txt.setText(FeeDataTable.getValueAt(row, 2).toString());
                    amountPaid_txt.setText(FeeDataTable.getValueAt(row, 3).toString());
                    dueDate_txt.setText(FeeDataTable.getValueAt(row, 4).toString());
                }
            }
        });
        printReceiptButton.addActionListener(e -> {
            // Check if fields are empty before printing
            if (amountDue_txt.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a fee record from the table first.");
                return;
            }
            printReceipt();
        });


    }
    private void printReceipt() {
        int row = FeeDataTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record from the table first.");
            return;
        }

        String studentId = studentid_cmb.getSelectedItem().toString();
        String amountDue = amountDue_txt.getText();
        String amountPaid = amountPaid_txt.getText();
        String dueDate = dueDate_txt.getText();

        // Get status from the table (Column 5) instead of a ComboBox
        String status = FeeDataTable.getValueAt(row, 5).toString();

        // 2. Generate a simple receipt format
        String receiptContent = "-------------------------------------------\n" +
                "            STUDENT FEE RECEIPT            \n" +
                "-------------------------------------------\n" +
                " Student ID:    " + studentId + "\n" +
                " Amount Due:    $" + amountDue + "\n" +
                " Amount Paid:   $" + amountPaid + "\n" +
                " Due Date:      " + dueDate + "\n" +
                " Status:        " + status + "\n" +
                "-------------------------------------------\n" +
                " Date Generated: " + new java.util.Date() + "\n" +
                " Thank you for your payment!               \n" +
                "-------------------------------------------";

        // 3. Save to a file
        try {
            String fileName = "Receipt_Student_" + studentId + ".txt";
            java.io.FileWriter writer = new java.io.FileWriter(fileName);
            writer.write(receiptContent);
            writer.close();

            JOptionPane.showMessageDialog(this, "Receipt saved as: " + fileName);

            // Optional: Automatically open the file
            java.awt.Desktop.getDesktop().open(new java.io.File(fileName));

        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating receipt: " + ex.getMessage());
        }
    }

    private void fillStudentIdCombo() {
        studentid_cmb.removeAllItems();

        // Call the method from StudentDAO
        List<Integer> idList = StudentDAO.getAllStudentIDs();

        for (Integer id : idList) {
            studentid_cmb.addItem(id.toString());
        }
    }

    private java.sql.Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.equals("____-__-__")) return null;
        try {
            java.util.Date utilDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            return new java.sql.Date(utilDate.getTime());
        } catch (Exception e) {
            return null;
        }
    }

    private void loadTableData(String keyword) {
        String[] columns = {"Fee ID", "Student ID", "Amount Due", "Amount Paid", "Due Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        java.util.List<Fee> list = FeeDAO.search(keyword);

        for (Fee f : list) {
            model.addRow(new Object[]{
                    f.getFeeId(),
                    f.getStudentId(),
                    f.getAmountDue(),
                    f.getAmountPaid(),
                    f.getDueDate(),
                    f.getStatus()
            });
        }
        FeeDataTable.setModel(model);
    }

    private void clearFields() {
        amountDue_txt.setText("");
        amountPaid_txt.setText("");
        dueDate_txt.setValue(null);

        // Safety check: only reset index if items exist
        if (studentid_cmb.getItemCount() > 0) {
            studentid_cmb.setSelectedIndex(0);
        }


        FeeDataTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeeFrm().setVisible(true));
    }
}
