import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class databaseConnection {

    private static final String url="jdbc:postgresql://localhost:5433/Accounting";
    private static final String username="postgres";
    private static final String password="Ahmed213@";

    public static Connection connection() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

    public static PreparedStatement gettingQuery(String query) throws SQLException {
        return connection().prepareStatement(query);
    }

    public static void insertingQuery(String TableName, int id, String firstName, String lastName, String email, double Percent, String passcode , int profilePin) throws SQLException {
        // Using PreparedStatement to handle parameters securely
        PreparedStatement preparedStatement = connection().prepareStatement("INSERT INTO " + TableName + " VALUES (?, ?, ?, ?, ?,?,?)");

        // Setting parameters
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, email);
        preparedStatement.setDouble(5, Percent);
        preparedStatement.setString(6, passcode);
        preparedStatement.setInt(7, profilePin);

        // Executing the update
        preparedStatement.executeUpdate();
    }

}
