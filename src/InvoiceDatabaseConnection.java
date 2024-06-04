import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void insertInvoice(String tableName, int invoiceID, String invoiceNumber, java.util.Date invoiceDate,
                                     String sellerName, String buyerName, double totalAmount,
                                     String currency, java.util.Date paymentDueDate, String transactionType, String loggedInUserDetails) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + tableName +
                     "(InvoiceID, InvoiceNumber, InvoiceDate, SellerName, BuyerName, TotalAmount, Currency, PaymentDueDate, TransactionType, userdetails) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            java.sql.Date sqlInvoiceDate = new java.sql.Date(invoiceDate.getTime());
            java.sql.Date sqlPaymentDueDate = new java.sql.Date(paymentDueDate.getTime());

            preparedStatement.setInt(1, invoiceID);
            preparedStatement.setString(2, invoiceNumber);
            preparedStatement.setDate(3, sqlInvoiceDate);
            preparedStatement.setString(4, sellerName);
            preparedStatement.setString(5, buyerName);
            preparedStatement.setDouble(6, totalAmount);
            preparedStatement.setString(7, currency);
            preparedStatement.setDate(8, sqlPaymentDueDate);
            preparedStatement.setString(9, transactionType);
            preparedStatement.setString(10, loggedInUserDetails);

            preparedStatement.executeUpdate();
        }
    }

    public static int getNextInvoiceID() throws SQLException {
        int nextInvoiceID = 1;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(InvoiceID) FROM Invoices")) {

            if (resultSet.next()) {
                nextInvoiceID = resultSet.getInt(1) + 1;
            }
        }

        return nextInvoiceID;
    }

    public static List<String> getAllUsers() throws SQLException {
        List<String> users = new ArrayList<>();
        String query = "SELECT DISTINCT userdetails FROM Invoices";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                users.add(resultSet.getString("userdetails"));
            }
        }
        return users;
    }

    public static List<String> getBalanceUsers() throws SQLException {
        List<String> users = new ArrayList<>();
        String query = "SELECT DISTINCT userdetails FROM partnerbalances";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                users.add(resultSet.getString("userdetails"));
            }
        }
        return users;
    }

    public static List<String[]> getInvoicesByUser(String user) throws SQLException {
        List<String[]> invoices = new ArrayList<>();
        String query = "SELECT * FROM Invoices WHERE userdetails = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String[] invoice = new String[]{
                            String.valueOf(resultSet.getInt("InvoiceID")),
                            resultSet.getString("InvoiceNumber"),
                            resultSet.getString("InvoiceDate"),
                            resultSet.getString("SellerName"),
                            resultSet.getString("BuyerName"),
                            String.valueOf(resultSet.getDouble("TotalAmount")),
                            resultSet.getString("Currency"),
                            resultSet.getString("PaymentDueDate"),
                            resultSet.getString("TransactionType")
                    };
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    public static List<Invoice> getAllInvoices() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT * FROM Invoices";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int invoiceID = resultSet.getInt("InvoiceID");
                String invoiceNumber = resultSet.getString("InvoiceNumber");
                Date invoiceDate = resultSet.getDate("InvoiceDate");
                String sellerName = resultSet.getString("SellerName");
                String buyerName = resultSet.getString("BuyerName");
                double totalAmount = resultSet.getDouble("TotalAmount");
                String currency = resultSet.getString("Currency");
                Date paymentDueDate = resultSet.getDate("PaymentDueDate");
                String transactionType = resultSet.getString("TransactionType");
                String userDetails = resultSet.getString("userdetails");

                Invoice invoice = new Invoice(invoiceID, invoiceNumber, invoiceDate, sellerName, buyerName,
                        totalAmount, currency, paymentDueDate, transactionType, userDetails);
                invoices.add(invoice);
            }
        }
        return invoices;
    }

    public static double getUserBalance(String user) throws SQLException {
        double balance = 0.0;
        String query = "SELECT Balance FROM PartnerBalances WHERE UserDetails = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    balance = resultSet.getDouble("Balance");
                }
            }
        }
        return balance;
    }

    public static double getUserSharePercentage(String user) throws SQLException {
        double sharePercentage = 0.0;
        String query = "SELECT SharePercentage FROM PartnerBalances WHERE UserDetails = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    sharePercentage = resultSet.getDouble("SharePercentage");
                }
            }
        }
        return sharePercentage;
    }

    public static void updateUserBalanceAndShare(String user, double newBalance, double newSharePercentage) throws SQLException {
        String query = "UPDATE PartnerBalances SET Balance = ?, SharePercentage = ? WHERE UserDetails = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setDouble(2, newSharePercentage);
            preparedStatement.setString(3, user);
            preparedStatement.executeUpdate();
        }
    }

    public static List<String[]> getAllUserBalancesAndShares() throws SQLException {
        List<String[]> balances = new ArrayList<>();
        String query = "SELECT UserDetails, Balance, SharePercentage FROM PartnerBalances";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String[] balance = new String[]{
                        resultSet.getString("UserDetails"),
                        String.valueOf(resultSet.getDouble("Balance")),
                        String.valueOf(resultSet.getDouble("SharePercentage"))
                };
                balances.add(balance);
            }
        }
        return balances;
    }
    public static List<Flat> getAllFlats() throws SQLException {
        List<Flat> flats = new ArrayList<>();
        String query = "SELECT name, price, size FROM Flats";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int size = resultSet.getInt("size");

                flats.add(new Flat(name, price, size));
            }
        }
        return flats;
    }
}
