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
    private JPanel parentPanel; // Reference to the parent panel where the LoginPanel is added

    public LoginPanel(JPanel parentPanel) {
        this.parentPanel = parentPanel;
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
            // Assume the verification logic here
            return true; // Dummy verification for demonstration
        } catch (Exception e) {
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
                // Clear the PIN field
                pinField.setText("");
                // Show the panel with six buttons upon successful login
                parentPanel.removeAll();
                parentPanel.add(new SixButtonPanel(parentPanel));
                parentPanel.revalidate();
                parentPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(LoginPanel.this, "Invalid PIN. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                // Clear the PIN field
                pinField.setText("");
            }
        }
    }
}
