import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DeletePartner extends JPanel {
    private JComboBox<String> userDropdown;
    private JPasswordField adminPasswordField;
    private JButton deleteButton;
    private String loggedInUserDetails;

    public DeletePartner(String loggedInUserDetails) {
        this.loggedInUserDetails = loggedInUserDetails;
        initializeComponents();
        populateUserDropdown(); // Load registered users into the dropdown
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel headerLabel = new JLabel("Delete User");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Select User:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userDropdown = new JComboBox<>();
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(userDropdown, gbc);

        JLabel adminPasswordLabel = new JLabel("Admin Password:");
        adminPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        adminPasswordField = new JPasswordField();
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(adminPasswordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(adminPasswordField, gbc);

        deleteButton = new JButton("Delete User");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedUser();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(deleteButton, gbc);

        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    private void populateUserDropdown() {
        try {
            Connection connection = InvoiceDatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT firstname FROM users");

            while (resultSet.next()) {
                String firstName = resultSet.getString("firstname");
                userDropdown.addItem(firstName);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedUser() {
        String selectedUser = userDropdown.getSelectedItem().toString();
        String adminPassword = new String(adminPasswordField.getPassword());

        // Validate admin password
        if (!validateAdminPassword(adminPassword)) {
            JOptionPane.showMessageDialog(this, "Invalid admin password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Perform deletion
        try {
            Connection connection = InvoiceDatabaseConnection.getConnection();
            String query = "DELETE FROM users WHERE firstname = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedUser);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "User '" + selectedUser + "' deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the user dropdown after deletion
                userDropdown.removeItem(selectedUser);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user '" + selectedUser + "'", "Error", JOptionPane.ERROR_MESSAGE);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateAdminPassword(String password) {
        String adminUsername = loggedInUserDetails; // Use the logged-in admin username
        try {
            Connection connection = InvoiceDatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT password_hash FROM admins WHERE username = ?");
            preparedStatement.setString(1, adminUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password_hash");
                System.out.println("Stored hash for user " + adminUsername + ": " + storedPassword); // Debugging line
                // Compare the entered password with the stored password
                boolean isValid = BCrypt.checkpw(password, storedPassword);
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return isValid;
            } else {
                // Admin username not found
                System.out.println("Admin username not found: " + adminUsername); // Debugging line
                resultSet.close();
                preparedStatement.close();
                connection.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred
        }
    }
}
