import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ReceiptsByUsersPanel extends JPanel {
    private JComboBox<String> userDropdown;
    private JTable invoicesTable;
    private String[] columnNames = {"Invoice ID", "Invoice Number", "Invoice Date", "Seller Name", "Buyer Name", "Total Amount", "Currency", "Payment Due Date", "Transaction Type"};
    private String loggedInUserDetails;

    public ReceiptsByUsersPanel(String loggedInUserDetails) {
        this.loggedInUserDetails = loggedInUserDetails;
        initializeComponents();
        loadUsers();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        userDropdown = new JComboBox<>();
        userDropdown.addActionListener(new UserDropdownActionListener());
        topPanel.add(new JLabel("Select User:"));
        topPanel.add(userDropdown);

        add(topPanel, BorderLayout.NORTH);

        invoicesTable = new JTable(new Object[][]{}, columnNames);
        JScrollPane scrollPane = new JScrollPane(invoicesTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadUsers() {
        try {
            List<String> users = InvoiceDatabaseConnection.getAllUsers();
            for (String user : users) {
                userDropdown.addItem(user);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadInvoicesForUser(String user) {
        try {
            List<String[]> invoices = InvoiceDatabaseConnection.getInvoicesByUser(user);
            Object[][] data = new Object[invoices.size()][columnNames.length];
            for (int i = 0; i < invoices.size(); i++) {
                data[i] = invoices.get(i);
            }
            invoicesTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching invoices: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class UserDropdownActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedUser = (String) userDropdown.getSelectedItem();
            if (selectedUser != null) {
                loadInvoicesForUser(selectedUser);
            }
        }
    }
}
