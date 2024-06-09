import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class EnterProduct extends JPanel {
    private JTextField nameField;
    private JTextField priceField;
    private JTextField sizeField;
    private JButton uploadButton;
    private File selectedFile;

    public EnterProduct() {
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Heading Panel
        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headingLabel = new JLabel("Enter Your Product");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headingPanel.add(headingLabel);
        add(headingPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Name:");
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel priceLabel = new JLabel("Price:");
        formPanel.add(priceLabel, gbc);

        gbc.gridx = 1;
        priceField = new JTextField(20);
        formPanel.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel sizeLabel = new JLabel("Size:");
        formPanel.add(sizeLabel, gbc);

        gbc.gridx = 1;
        sizeField = new JTextField(20);
        formPanel.add(sizeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel imageLabel = new JLabel("Image:");
        formPanel.add(imageLabel, gbc);

        gbc.gridx = 1;
        uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(new UploadButtonListener());
        formPanel.add(uploadButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitButtonListener());
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class UploadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(EnterProduct.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                JLabel imageLabel = new JLabel(imageIcon);
                JOptionPane.showMessageDialog(EnterProduct.this, imageLabel, "Selected Image", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int size = Integer.parseInt(sizeField.getText());

            double totalPrice = price * size;

            byte[] imageData = getFileBytes(selectedFile);
            try {

                InvoiceDatabaseConnection.insertFlat(name, totalPrice, size, imageData);
                JOptionPane.showMessageDialog(EnterProduct.this, "Product added successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(EnterProduct.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private byte[] getFileBytes(File file) {
        byte[] imageData = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            imageData = fis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageData;
    }
}
