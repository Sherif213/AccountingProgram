import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ProductsPanel extends JPanel {
    private String loggedInUserDetails;

    public ProductsPanel(String loggedInUserDetails) {
        this.loggedInUserDetails = loggedInUserDetails;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel titleLabel = new JLabel("Products");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);

        add(topPanel, BorderLayout.NORTH);

        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        try {
            List<Flat> flats = InvoiceDatabaseConnection.getAllFlats();
            for (Flat flat : flats) {
                JPanel productBox = createProductBox(flat);
                productsPanel.add(productBox, gbc);
                gbc.gridy++;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createProductBox(Flat flat) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        box.setPreferredSize(new Dimension(300, 250));
        box.setBackground(Color.WHITE);
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setIcon(new ImageIcon(new ImageIcon(flat.getImage()).getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH))); // Scaled image

        JLabel flatNameLabel = new JLabel(flat.getName());
        flatNameLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        flatNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel flatPriceLabel = new JLabel("Price: $" + flat.getPrice());
        flatPriceLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        flatPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel flatSizeLabel = new JLabel("Size: " + flat.getSize() + " square meters");
        flatSizeLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        flatSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(Box.createVerticalStrut(10));
        box.add(imageLabel);
        box.add(Box.createVerticalStrut(10));
        box.add(flatNameLabel);
        box.add(Box.createVerticalStrut(5));
        box.add(flatPriceLabel);
        box.add(flatSizeLabel);
        box.add(Box.createVerticalStrut(10));

        return box;
    }

    private void showFlatDetails(Flat flat) {
        // Implement this method to show detailed information about the flat
        JOptionPane.showMessageDialog(this, "Showing details for " + flat.getName());
    }
}
