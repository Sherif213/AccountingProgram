import java.sql.*;

public class InvoiceDatabaseConnection {
    private static final String url = "jdbc:postgresql://localhost:5433/Accounting";
    private static final String username = "postgres";
    private static final String password = "Ahmed213@";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static PreparedStatement getPreparedStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    public static void insertInvoice(String tableName, int invoiceID, String invoiceNumber, Date invoiceDate,
                                     String sellerName, String buyerName, double totalAmount,
                                     String currency, Date paymentDueDate) throws SQLException {
        // Using try-with-resources to ensure the PreparedStatement is closed properly
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + tableName +
                     "(InvoiceID, InvoiceNumber, InvoiceDate, SellerName, BuyerName, TotalAmount, Currency, PaymentDueDate) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlInvoiceDate = new java.sql.Date(invoiceDate.getTime());
            java.sql.Date sqlPaymentDueDate = new java.sql.Date(paymentDueDate.getTime());

            // Setting parameters
            preparedStatement.setInt(1, invoiceID);
            preparedStatement.setString(2, invoiceNumber);
            preparedStatement.setDate(3, sqlInvoiceDate);
            preparedStatement.setString(4, sellerName);
            preparedStatement.setString(5, buyerName);
            preparedStatement.setDouble(6, totalAmount);
            preparedStatement.setString(7, currency);
            preparedStatement.setDate(8, sqlPaymentDueDate);

            // Executing the update
            preparedStatement.executeUpdate();
        }
    }

    // Method to get the next available InvoiceID
    public static int getNextInvoiceID() throws SQLException {
        int nextInvoiceID = 1; // Default value if no records exist

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(InvoiceID) FROM Invoices")) {

            // If there are existing records, retrieve the highest InvoiceID and increment it
            if (resultSet.next()) {
                nextInvoiceID = resultSet.getInt(1) + 1;
            }
        }

        return nextInvoiceID;
    }
}
