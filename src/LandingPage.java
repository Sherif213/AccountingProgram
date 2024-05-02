import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandingPage extends JFrame implements ActionListener {
    private JLabel greetingSign;

    public LandingPage() {
        initializeComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout()); // Use BorderLayout for main frame

        greetingSign = new JLabel("Welcome to KatrElnada", SwingConstants.CENTER);
        greetingSign.setFont(new Font("Arial", Font.BOLD, 24));
        add(greetingSign, BorderLayout.NORTH); // Add greeting label to the top
        greetingSign.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));

        JPanel buttonPanel = new JPanel(new GridBagLayout()); // Create a panel for the buttons
        GridBagConstraints gbc = createGridBagConstraints(0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10)); // Set constraints for buttons

        JButton userBtn = new JButton("Proceed as User");
        userBtn.addActionListener(this);
        userBtn.setPreferredSize(new Dimension(200, 150)); // Set preferred size for user button
        userBtn.setBackground(new Color(102, 153, 204)); // Set background color to a professional blue
        userBtn.setForeground(Color.WHITE); // Set text color to white
        buttonPanel.add(userBtn, gbc); // Add user button to the panel

        gbc.gridx = 1; // Move to the next column
        JButton adminBtn = new JButton("Proceed as Admin");
        adminBtn.addActionListener(this);
        adminBtn.setPreferredSize(new Dimension(200, 150)); // Set preferred size for admin button
        adminBtn.setBackground(new Color(255, 153, 51)); // Set background color to a professional orange
        adminBtn.setForeground(Color.WHITE); // Set text color to white
        buttonPanel.add(adminBtn, gbc); // Add admin button to the panel

        add(buttonPanel, BorderLayout.CENTER); // Add button panel to the center
    }

    private GridBagConstraints createGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.insets = insets;
        return gbc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Proceed as User")) {
            replaceContent(new UserPanel()); // Replace the content with the UserPanel
        } else if (e.getActionCommand().equals("Proceed as Admin")) {
            // Implement action for admin button if needed
        }
    }

    private void replaceContent(JPanel newContent) {
        getContentPane().removeAll(); // Remove all components from the content pane
        getContentPane().add(newContent, BorderLayout.CENTER); // Add the new content to the content pane
        revalidate(); // Revalidate the frame to update the layout
        repaint(); // Repaint the frame to update the appearance
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LandingPage::new);
    }
}
