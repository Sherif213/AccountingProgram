// UserPanel.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserPanel extends JPanel {
    private List<String> usersData;
    JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel userProfilePanel;
    private LoginPanel loginPanel;

    public UserPanel() {
        usersData = fetchUsersDataFromDatabase(); // Fetch user data from the database
        initializeComponents();
        refreshUserData();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        userProfilePanel = createUserProfilePanel();
        loginPanel = new LoginPanel(this); // Pass UserPanel instance to login panel
        signUp signUP = new signUp(this); // Create a signup panel

        cardPanel.add(userProfilePanel, "UserProfiles");
        cardPanel.add(loginPanel, "Login");
        cardPanel.add(signUP, "Signup"); // Add the signup panel to the cardPanel

        add(cardPanel, BorderLayout.CENTER);

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addUserButton = new JButton("+");
        addUserButton.setFont(new Font("Arial", Font.BOLD, 24));
        addUserButton.setPreferredSize(new Dimension(50, 50)); // Set button size
        addUserButton.addActionListener(e -> cardLayout.show(cardPanel, "Signup")); // Show signup panel
        addPanel.add(addUserButton);
        add(addPanel, BorderLayout.SOUTH);
    }


    private JPanel createUserProfilePanel() {
        JPanel userProfilePanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 2 rows, 3 columns, with 10px horizontal and vertical gap
        userProfilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Display user profiles from the fetched user data
        for (String userData : usersData) {
            JLabel userProfile = new JLabel(userData, SwingConstants.CENTER);
            userProfile.setFont(new Font("Arial", Font.PLAIN, 16));
            userProfile.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border for visualization

            // Add mouse listener to the user profile label
            userProfile.addMouseListener(new UserProfileClickListener(userData));
            userProfilePanel.add(userProfile);
        }
        return userProfilePanel;
    }

    // Method to fetch user data from the database
    private List<String> fetchUsersDataFromDatabase() {
        List<String> userDataList = new ArrayList<>();
        try {
            // Create a database connection
            databaseConnection dbConnection = new databaseConnection();
            Connection connection = dbConnection.connection();

            // Create a statement
            Statement statement = connection.createStatement();

            // Execute a query to fetch user data
            ResultSet resultSet = statement.executeQuery("SELECT firstname FROM users");

            // Process the results and add user data to the list
            while (resultSet.next()) {
                // Assuming the "users" table has a column named "firstname"
                String firstName = resultSet.getString("firstname");
                userDataList.add(firstName);
            }

            // Close the resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userDataList;
    }

    public void refreshUserData() {
        usersData = fetchUsersDataFromDatabase();
        userProfilePanel = createUserProfilePanel();
        cardPanel.add(userProfilePanel, "UserProfiles");
        cardLayout.show(cardPanel, "UserProfiles"); // Show the user profiles panel
        revalidate(); // Refresh the layout
        repaint(); // Redraw the panel
    }

    // Inner class to handle user profile click events
    private class UserProfileClickListener extends MouseAdapter {
        private String userData;

        public UserProfileClickListener(String userData) {
            this.userData = userData;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            loginPanel.setUserDetails(userData);
            cardLayout.show(cardPanel, "Login");
        }
    }
}
