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
        userBtn.setPreferredSize(new Dimension(200, 150));
        userBtn.setBackground(new Color(102, 153, 204));
        userBtn.setForeground(Color.WHITE);
        buttonPanel.add(userBtn, gbc);

        gbc.gridx = 1; // Move to the next column
        JButton adminBtn = new JButton("Proceed as Admin");
        adminBtn.addActionListener(this);
        adminBtn.setPreferredSize(new Dimension(200, 150));
        adminBtn.setBackground(new Color(255, 153, 51));
        adminBtn.setForeground(Color.WHITE);
        buttonPanel.add(adminBtn, gbc);

        add(buttonPanel, BorderLayout.CENTER);
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
            replaceContent(new UserPanel());
        } else if (e.getActionCommand().equals("Proceed as Admin")) {
            replaceContent(new AdminPanel());
        }
    }

    private void replaceContent(JPanel newContent) {
        getContentPane().removeAll();
        getContentPane().add(newContent, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LandingPage::new);
    }
}
