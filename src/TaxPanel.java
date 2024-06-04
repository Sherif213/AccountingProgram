import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class TaxPanel extends JPanel {
    private JTextField taxRateField;
    private JButton calculateTaxButton;
    private JTable taxTable;
    private DefaultTableModel tableModel;
    private String loggedInUserDetails;

    public TaxPanel(String loggedInUserDetails) {
        this.loggedInUserDetails = loggedInUserDetails;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel taxRateLabel = new JLabel("Tax Rate:");
        taxRateField = new JTextField(10);
        calculateTaxButton = new JButton("Calculate Tax");
        calculateTaxButton.addActionListener(new CalculateTaxButtonListener());
        topPanel.add(taxRateLabel);
        topPanel.add(taxRateField);
        topPanel.add(calculateTaxButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Invoice ID", "Total Amount", "Transaction Type", "Tax Amount"}, 0);
        taxTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taxTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private class CalculateTaxButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double taxRate = Double.parseDouble(taxRateField.getText());

                if (taxRate < 0) {
                    JOptionPane.showMessageDialog(null, "Tax rate cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Clear previous results
                tableModel.setRowCount(0);
                double totalTax = 0.0;

                // Fetch all invoices and calculate tax for each
                List<Invoice> invoices = InvoiceDatabaseConnection.getAllInvoices();

                for (Invoice invoice : invoices) {
                    double totalAmount = invoice.getTotalAmount();
                    String transactionType = invoice.getTransactionType();

                    double taxAmount = 0.0;
                    if (transactionType.equals("Export")) {
                        taxAmount = totalAmount * (taxRate / 100);
                    } else if (transactionType.equals("Import")) {
                        taxAmount = totalAmount * (taxRate / 100) * 1.2; // Assuming a 20% surcharge for imports
                    }

                    totalTax += taxAmount;

                    // Add row to table
                    tableModel.addRow(new Object[]{invoice.getInvoiceID(), totalAmount, transactionType, taxAmount});
                }

                // Add total tax row
                tableModel.addRow(new Object[]{"", "", "Total Tax", totalTax});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid tax rate", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error retrieving invoices: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
