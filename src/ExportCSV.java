import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExportCSV {

    public static void exportInvoicesToCSV() {
        String csvFilePath = "invoices.csv";

        try (Connection connection = InvoiceDatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM invoices");
             ResultSet resultSet = preparedStatement.executeQuery();
             FileWriter writer = new FileWriter(csvFilePath)) {

            // Write CSV header
            writer.append("Invoice ID,Invoice Number,Invoice Date,Seller Name,Buyer Name,Total Amount,Currency,Payment Due Date,Transaction Type,User Details\n");

            // Write CSV rows
            while (resultSet.next()) {
                writer.append(resultSet.getString("invoiceid")).append(",");
                writer.append(resultSet.getString("invoicenumber")).append(",");
                writer.append(resultSet.getDate("invoicedate").toString()).append(",");
                writer.append(resultSet.getString("sellername")).append(",");
                writer.append(resultSet.getString("buyername")).append(",");
                writer.append(String.format("%.2f", resultSet.getDouble("totalamount"))).append(",");
                writer.append(resultSet.getString("currency")).append(",");
                writer.append(resultSet.getDate("paymentduedate").toString()).append(",");
                writer.append(resultSet.getString("transactionType")).append(",");
                writer.append(resultSet.getString("userdetails")).append("\n");
            }

            JOptionPane.showMessageDialog(null, "CSV file has been created successfully!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to export data to CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
