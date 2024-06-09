import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class ChangePasswordPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmNewPasswordField;
    private JTextField currentPinField;
    private JTextField newPinField;
    private JTextField confirmNewPinField;
    private JButton changePasswordButton;
    private JButton changePinButton;

    public ChangePasswordPanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel usernameLabel = new JLabel("First Name:");
        usernameField = new JTextField();
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        JLabel currentPasswordLabel = new JLabel("Current Password:");
        currentPasswordField = new JPasswordField();
        formPanel.add(currentPasswordLabel);
        formPanel.add(currentPasswordField);

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordField = new JPasswordField();
        formPanel.add(newPasswordLabel);
        formPanel.add(newPasswordField);

        JLabel confirmNewPasswordLabel = new JLabel("Confirm New Password:");
        confirmNewPasswordField = new JPasswordField();
        formPanel.add(confirmNewPasswordLabel);
        formPanel.add(confirmNewPasswordField);

        JLabel currentPinLabel = new JLabel("Current PIN:");
        currentPinField = new JTextField();
        formPanel.add(currentPinLabel);
        formPanel.add(currentPinField);

        JLabel newPinLabel = new JLabel("New PIN:");
        newPinField = new JTextField();
        formPanel.add(newPinLabel);
        formPanel.add(newPinField);

        JLabel confirmNewPinLabel = new JLabel("Confirm New PIN:");
        confirmNewPinField = new JTextField();
        formPanel.add(confirmNewPinLabel);
        formPanel.add(confirmNewPinField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        changePasswordButton = new JButton("Change Password");
        changePinButton = new JButton("Change PIN");
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(changePinButton);

        // Add components to main panel
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });

        changePinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePin();
            }
        });
    }

    private void changePassword() {
        String username = usernameField.getText();
        String currentPassword = new String(currentPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmNewPassword = new String(confirmNewPasswordField.getPassword());

        // Check if new password matches the confirm password
        if (!newPassword.equals(confirmNewPassword)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Connect to the database and verify current password
        try (Connection connection = InvoiceDatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE firstname = ?")) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password");
                    if (BCrypt.checkpw(currentPassword, hashedPasswordFromDB)) {
                        // Update password
                        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                        try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET password = ? WHERE firstname = ?")) {
                            updateStatement.setString(1, hashedNewPassword);
                            updateStatement.setString(2, username);
                            updateStatement.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect current password!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changePin() {
        String username = usernameField.getText();
        String currentPin = currentPinField.getText();
        String newPin = newPinField.getText();
        String confirmNewPin = confirmNewPinField.getText();

        // Check if new PIN matches the confirm PIN
        if (!newPin.equals(confirmNewPin)) {
            JOptionPane.showMessageDialog(this, "New PIN and confirm PIN do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert pin values to integers
        int currentPinInt = Integer.parseInt(currentPin);
        int newPinInt = Integer.parseInt(newPin);

        // Connect to the database and verify current PIN
        try (Connection connection = InvoiceDatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT profilepin FROM users WHERE firstname = ?")) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int pinFromDB = resultSet.getInt("profilepin");
                    if (currentPinInt == pinFromDB) {
                        // Update PIN
                        try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET profilepin = ? WHERE firstname = ?")) {
                            updateStatement.setInt(1, newPinInt);
                            updateStatement.setString(2, username);
                            updateStatement.executeUpdate();
                            JOptionPane.showMessageDialog(this, "PIN changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect current PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error or invalid PIN format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
