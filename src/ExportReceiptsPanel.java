import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExportReceiptsPanel extends JPanel {
    private JTable table;

    public ExportReceiptsPanel() {
        initializeComponents();
        loadDataFromDatabase();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Create table with default table model
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadDataFromDatabase() {
        // Define column names for the table
        String[] columnNames = {"Invoice ID", "Invoice Number", "Invoice Date", "Seller Name", "Buyer Name", "Total Amount", "Currency"};

        // Create a default table model with defined column names
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try {
            // Establish database connection
            Connection connection = InvoiceDatabaseConnection.getConnection();

            // Create statement to execute SQL query
            Statement statement = connection.createStatement();

            // Execute query to retrieve export invoices
            String query = "SELECT InvoiceID, InvoiceNumber, InvoiceDate, SellerName, BuyerName, TotalAmount, Currency " +
                    "FROM Invoices WHERE TransactionType = 'Export'";
            ResultSet resultSet = statement.executeQuery(query);

            // Populate table model with data from result set
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getInt("InvoiceID"),
                        resultSet.getString("InvoiceNumber"),
                        resultSet.getDate("InvoiceDate"),
                        resultSet.getString("SellerName"),
                        resultSet.getString("BuyerName"),
                        resultSet.getDouble("TotalAmount"),
                        resultSet.getString("Currency")
                };
                model.addRow(rowData);
            }

            // Close result set, statement, and connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
            JOptionPane.showMessageDialog(this, "Error occurred while fetching data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Set the table model
        table.setModel(model);
    }
}
