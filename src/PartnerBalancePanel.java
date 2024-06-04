import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class PartnerBalancePanel extends JPanel {
    private JComboBox<String> userDropdown;
    private JTextField balanceField;
    private JTextField sharePercentageField;
    private JButton updateBalanceButton;
    private JTable balancesTable;
    private String[] columnNames = {"User", "Balance", "Share (%)"};
    private String loggedInUserDetails;

    public PartnerBalancePanel(String loggedInUserDetails) {
        this.loggedInUserDetails = loggedInUserDetails;
        initializeComponents();
        loadUsers();
        loadAllUserBalancesAndShares();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        userDropdown = new JComboBox<>();
        userDropdown.addActionListener(new UserDropdownActionListener());
        balanceField = new JTextField(10);
        sharePercentageField = new JTextField(10);
        updateBalanceButton = new JButton("Update Balance");
        updateBalanceButton.addActionListener(new UpdateBalanceButtonListener());
        topPanel.add(new JLabel("Select User:"));
        topPanel.add(userDropdown);
        topPanel.add(new JLabel("Balance:"));
        topPanel.add(balanceField);
        topPanel.add(new JLabel("Share (%):"));
        topPanel.add(sharePercentageField);
        topPanel.add(updateBalanceButton);

        add(topPanel, BorderLayout.NORTH);

        balancesTable = new JTable(new Object[][]{}, columnNames);
        JScrollPane scrollPane = new JScrollPane(balancesTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadUsers() {
        try {
            List<String> users = InvoiceDatabaseConnection.getBalanceUsers();
            for (String user : users) {
                userDropdown.addItem(user);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllUserBalancesAndShares() {
        try {
            List<String[]> balances = InvoiceDatabaseConnection.getAllUserBalancesAndShares();
            Object[][] data = new Object[balances.size()][columnNames.length];
            for (int i = 0; i < balances.size(); i++) {
                data[i] = balances.get(i);
            }
            balancesTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching balances and shares: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBalanceAndShareForUser(String user) {
        try {
            double balance = InvoiceDatabaseConnection.getUserBalance(user);
            double sharePercentage = InvoiceDatabaseConnection.getUserSharePercentage(user);
            balanceField.setText(String.valueOf(balance));
            sharePercentageField.setText(String.valueOf(sharePercentage));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching balance and share: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class UserDropdownActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedUser = (String) userDropdown.getSelectedItem();
            if (selectedUser != null) {
                loadBalanceAndShareForUser(selectedUser);
            }
        }
    }

    private class UpdateBalanceButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedUser = (String) userDropdown.getSelectedItem();
            if (selectedUser != null) {
                try {
                    double newBalance = Double.parseDouble(balanceField.getText());
                    double newSharePercentage = Double.parseDouble(sharePercentageField.getText());

                    // Get the total share percentage of all users
                    double totalSharePercentage = getTotalSharePercentage();

                    // Calculate the new total share percentage after updating
                    double updatedTotalSharePercentage = totalSharePercentage - InvoiceDatabaseConnection.getUserSharePercentage(selectedUser) + newSharePercentage;

                    // Check if the new total share percentage exceeds 100
                    if (updatedTotalSharePercentage > 100) {
                        JOptionPane.showMessageDialog(null, "Total share percentage cannot exceed 100%", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update user balance and share
                    InvoiceDatabaseConnection.updateUserBalanceAndShare(selectedUser, newBalance, newSharePercentage);
                    loadAllUserBalancesAndShares();
                    JOptionPane.showMessageDialog(null, "Balance and share updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating balance and share: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Method to calculate the total share percentage of all users
        private double getTotalSharePercentage() throws SQLException {
            double totalSharePercentage = 0.0;
            List<String[]> balances = InvoiceDatabaseConnection.getAllUserBalancesAndShares();
            for (String[] balance : balances) {
                totalSharePercentage += Double.parseDouble(balance[2]); // Assuming share percentage is at index 2
            }
            return totalSharePercentage;
        }
    }

}
