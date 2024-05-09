import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportingInvoicePanel extends JPanel {
    private JTextField invoiceNumberField;
    private JTextField invoiceDateField;
    private JTextField sellerNameField;
    private JTextField buyerNameField;
    private JTextField totalAmountField;
    private JTextField currencyField;
    private JTextField paymentDueDateField;

    public ReportingInvoicePanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        inputPanel.add(createLabelWithPadding("Invoice Number:"));
        invoiceNumberField = new JTextField();
        inputPanel.add(invoiceNumberField);

        inputPanel.add(createLabelWithPadding("Invoice Date (YYYY-MM-DD):"));
        invoiceDateField = new JTextField();
        inputPanel.add(invoiceDateField);

        inputPanel.add(createLabelWithPadding("Seller Name:"));
        sellerNameField = new JTextField();
        inputPanel.add(sellerNameField);

        inputPanel.add(createLabelWithPadding("Buyer Name:"));
        buyerNameField = new JTextField();
        inputPanel.add(buyerNameField);

        inputPanel.add(createLabelWithPadding("Total Amount:"));
        totalAmountField = new JTextField();
        inputPanel.add(totalAmountField);

        inputPanel.add(createLabelWithPadding("Currency:"));
        currencyField = new JTextField();
        inputPanel.add(currencyField);

        inputPanel.add(createLabelWithPadding("Payment Due Date (YYYY-MM-DD):"));
        paymentDueDateField = new JTextField();
        inputPanel.add(paymentDueDateField);

        add(inputPanel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save Invoice");
        saveButton.addActionListener(new SaveButtonListener());
        add(saveButton, BorderLayout.SOUTH);
    }

    private JLabel createLabelWithPadding(String labelText) {
        JLabel label = new JLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Adding left padding
        return label;
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Retrieve invoice details from input fields
            String invoiceNumber = invoiceNumberField.getText();
            String invoiceDateStr = invoiceDateField.getText();
            String sellerName = sellerNameField.getText();
            String buyerName = buyerNameField.getText();
            double totalAmount = Double.parseDouble(totalAmountField.getText());
            String currency = currencyField.getText();
            String paymentDueDateStr = paymentDueDateField.getText();

            // Validate invoice date and payment due date formats using regex
            if (!isValidDateFormat(invoiceDateStr) || !isValidDateFormat(paymentDueDateStr)) {
                JOptionPane.showMessageDialog(ReportingInvoicePanel.this,
                        "Invalid date format. Please enter dates in YYYY-MM-DD format.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convert string dates to java.sql.Date
            Date invoiceDate = Date.valueOf(invoiceDateStr);
            Date paymentDueDate = Date.valueOf(paymentDueDateStr);

            // Get the highest existing InvoiceID from the database and increment it by 1
            int nextInvoiceID = getNextInvoiceID();

            // Insert invoice details into the database
            try {
                InvoiceDatabaseConnection.insertInvoice("Invoices", nextInvoiceID, invoiceNumber, invoiceDate, sellerName,
                        buyerName, totalAmount, currency, paymentDueDate);
                JOptionPane.showMessageDialog(ReportingInvoicePanel.this, "Invoice saved successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ReportingInvoicePanel.this,
                        "Error occurred while saving invoice: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Method to get the next available InvoiceID by incrementing the highest existing ID
        private int getNextInvoiceID() {
            int nextInvoiceID = 1; // Default value if no records exist

            try {
                // Query the database to get the highest existing InvoiceID
                nextInvoiceID = InvoiceDatabaseConnection.getNextInvoiceID();
            } catch (SQLException ex) {
                // Handle any SQL exception
                ex.printStackTrace();
            }

            return nextInvoiceID;
        }

        // Method to validate date format using regex
        private boolean isValidDateFormat(String date) {
            Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            Matcher matcher = pattern.matcher(date);
            return matcher.matches();
        }
    }
}
