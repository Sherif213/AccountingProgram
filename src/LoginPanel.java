// LoginPanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginPanel extends JPanel {
    private JLabel userDetailsLabel;
    private JTextField pinField;
    private JButton loginButton;
    private UserPanel userPanel;

    public LoginPanel(UserPanel userPanel) {
        this.userPanel = userPanel;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        userDetailsLabel = new JLabel("", SwingConstants.CENTER);
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pinField = new JTextField(10);
        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());
        loginPanel.add(new JLabel("PIN:"));
        loginPanel.add(pinField);
        loginPanel.add(loginButton);

        add(userDetailsLabel, BorderLayout.CENTER);
        add(loginPanel, BorderLayout.SOUTH);
    }

    public void setUserDetails(String userDetails) {
        userDetailsLabel.setText(userDetails);
    }
    public boolean verifyPin(String userDetails, String pin) {
        try {
            // Create a database connection
            databaseConnection dbConnection = new databaseConnection();
            Connection connection = dbConnection.connection();

            // Create a statement
            Statement statement = connection.createStatement();

            // Extract first name from userDetails
            String[] userDetailsParts = userDetails.split(", "); // Assuming userDetails is in the format "FirstName, OtherDetails"
            String firstName = userDetailsParts[0]; // Extracting the first part which is the first name

            // Execute a query to fetch user data based on the extracted first name
            ResultSet resultSet = statement.executeQuery("SELECT profilepin FROM users WHERE firstname = '" + firstName + "'");

            // Check if the result set has any rows
            if (resultSet.next()) {
                // Get the PIN from the result set
                String storedPin = resultSet.getString("profilepin");

                // Compare the entered PIN with the stored PIN
                if (pin.equals(storedPin)) {
                    return true; // PINs match, login successful
                } else {
                    return false; // PINs don't match, login failed
                }
            }

            // Close the resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Error occurred or user not found
    }



    // Inner class to handle login button click
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredPin = pinField.getText();
            String userDetails = userDetailsLabel.getText(); // Fetch userDetails from userDetailsLabel
            // Check if the entered PIN matches the expected PIN (Dummy check for demonstration)
            if (verifyPin(userDetails, enteredPin)) { // Pass userDetails and enteredPin to verifyPin method
                JOptionPane.showMessageDialog(LoginPanel.this, "Login successful!");
                // Clear the PIN field
                pinField.setText("");
            } else {
                JOptionPane.showMessageDialog(LoginPanel.this, "Invalid PIN. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                // Clear the PIN field
                pinField.setText("");
            }
        }
    }
}
