import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

public class AdminPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton signUpButton;
    private String loggedInUserDetails;
    private String loggedInUsername;

    public AdminPanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signInButton = new JButton("Sign In");
        signUpButton = new JButton("Sign Up");
        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);

        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check credentials and switch to dashboard panel if valid
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (isValidCredentials(username, password)) {
                    switchToDashboardPanel();
                } else {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Insert new admin credentials into the database upon signing up
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (!username.isEmpty() && !password.isEmpty()) {
                    if (insertNewAdmin(username, password)) {

                        JOptionPane.showMessageDialog(AdminPanel.this, "Admin account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(AdminPanel.this, "Failed to create admin account!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminPanel.this, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private boolean isValidCredentials(String username, String password) {
        // Connect to the database and retrieve the hashed password for the provided username
        try (Connection connection = InvoiceDatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT password_hash FROM admins WHERE username = ?")) {
            loggedInUsername = username;
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password_hash");
                    // Verify the entered password against the hashed password from the database
                    return BCrypt.checkpw(password, hashedPasswordFromDB);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean insertNewAdmin(String username, String password) {
        // Insert new admin credentials into the database
        try (Connection connection = InvoiceDatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO admins (username, password_hash, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)")) {

            // Hash the password before inserting it into the database
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void switchToDashboardPanel() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(new AdminSixButtonPanel(loggedInUsername));
        frame.revalidate();
        frame.repaint();
    }
}
