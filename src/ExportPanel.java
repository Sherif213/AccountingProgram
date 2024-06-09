import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExportPanel extends JPanel {

    public ExportPanel() {
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel headerLabel = new JLabel("Export Invoices");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Export Button
        JButton exportButton = new JButton("Export Invoices to CSV");
        exportButton.setFont(new Font("Arial", Font.BOLD, 16));
        exportButton.setBackground(new Color(46, 204, 113));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExportCSV.exportInvoicesToCSV();
            }
        });

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.CENTER;
        centerPanel.add(exportButton, gbc);

        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

}
