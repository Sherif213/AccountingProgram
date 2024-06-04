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

        JPanel productsPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns, variable rows
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        add(scrollPane, BorderLayout.CENTER);

        try {
            List<Flat> flats = InvoiceDatabaseConnection.getAllFlats(); // Assume you have a method to fetch all flats
            for (Flat flat : flats) {
                productsPanel.add(createProductBox(flat));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createProductBox(Flat flat) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        box.setPreferredSize(new Dimension(200, 150));
        box.setBackground(Color.WHITE);
        box.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel flatNameLabel = new JLabel(flat.getName());
        flatNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        flatNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel flatPriceLabel = new JLabel("Price: $" + flat.getPrice());
        flatPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel flatSizeLabel = new JLabel("Size: " + flat.getSize() + " sq.ft.");
        flatSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton detailsButton = new JButton("Details");
        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a detailed view or perform another action
                showFlatDetails(flat);
            }
        });

        box.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        box.add(flatNameLabel);
        box.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        box.add(flatPriceLabel);
        box.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        box.add(flatSizeLabel);
        box.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        box.add(detailsButton);
        box.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer

        return box;
    }

    private void showFlatDetails(Flat flat) {
        // Implement this method to show detailed information about the flat
        JOptionPane.showMessageDialog(this, "Showing details for " + flat.getName());
    }
}
