import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class signUp extends JPanel {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField sharePercentField;
    private JPasswordField passwordField;
    private JTextField profilePinField;
    private UserPanel userPanel;

    public signUp(UserPanel userPanel) {
        this.userPanel = userPanel; // Reference to the UserPanel instance
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Share Percentage:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Profile Pin:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        firstNameField = new JTextField();
        firstNameField.setMargin(new Insets(5, 5, 5, 5));
        panel.add(firstNameField, gbc);
        gbc.gridy++;
        lastNameField = new JTextField();
        lastNameField.setMargin(new Insets(5, 5, 5, 5));
        panel.add(lastNameField, gbc);
        gbc.gridy++;
        emailField = new JTextField();
        emailField.setMargin(new Insets(5, 5, 5, 5));
        panel.add(emailField, gbc);
        gbc.gridy++;
        sharePercentField = new JTextField();
        sharePercentField.setMargin(new Insets(5, 5, 5, 5));
        panel.add(sharePercentField, gbc);
        gbc.gridy++;
        passwordField = new JPasswordField();
        passwordField.setMargin(new Insets(5, 5, 5, 5));
        panel.add(passwordField, gbc);
        gbc.gridy++;
        profilePinField = new JTextField();
        profilePinField.setMargin(new Insets(5, 5, 5, 5));
        panel.add(profilePinField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JButton signupButton = new JButton("Signup");
        signupButton.setForeground(Color.WHITE);
        signupButton.setBackground(new Color(53, 152, 219)); // Blue background color
        signupButton.setMargin(new Insets(10, 20, 10, 20));
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpUser();
            }
        });
        panel.add(signupButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void signUpUser() {
        // Retrieve input values
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String sharePercentStr = sharePercentField.getText();
        String password = new String(passwordField.getPassword());
        String profilePinStr = profilePinField.getText();

        // Perform validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || sharePercentStr.isEmpty() || password.isEmpty() || profilePinStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double sharePercent = Double.parseDouble(sharePercentStr);
            int profilePin = Integer.parseInt(profilePinStr);

            if (sharePercent < 0 || sharePercent > 100) {
                JOptionPane.showMessageDialog(this, "Share percentage must be between 0 and 100", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Establish database connection
            databaseConnection dbConnection = new databaseConnection();
            Connection connection = dbConnection.connection();

            // Generate ID (increment by 1 from the last ID in the database)
            int id = generateId(connection);

            // Insert user data into the database
            String insertQuery = "INSERT INTO users (userId, FirstName, LastName, email, sharePercent, password, profilePin) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, email);
            preparedStatement.setDouble(5, sharePercent);
            preparedStatement.setString(6, password);
            preparedStatement.setInt(7, profilePin);
            preparedStatement.executeUpdate();

            // Close resources
            preparedStatement.close();
            connection.close();

            // Show success message
            JOptionPane.showMessageDialog(this, "User signed up successfully!");

            // Switch back to user profiles panel
            CardLayout cardLayout = (CardLayout) userPanel.cardPanel.getLayout();
            cardLayout.show(userPanel.cardPanel, "UserProfiles");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid share percentage or profile pin", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int generateId(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX(userId) FROM users");
        int id = 1; // Default to 1 if no records exist
        if (resultSet.next()) {
            id = resultSet.getInt(1) + 1; // Increment by 1 from the last ID
        }
        resultSet.close();
        statement.close();
        return id;
    }
}
