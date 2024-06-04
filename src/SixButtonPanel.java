import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SixButtonPanel extends JPanel {
    private String loggedInUserDetails;
    private JPanel parentPanel;

    public SixButtonPanel(JPanel parentPanel,String loggedInUserDetails) {
        this.parentPanel = parentPanel;
        this.loggedInUserDetails = loggedInUserDetails;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new GridLayout(2, 3));

        JButton button1 = new JButton("Report Invoice");
        JButton button2 = new JButton("Partner Balance");
        JButton button3 = new JButton("Products");
        JButton button4 = new JButton("Import receipts");
        JButton button5 = new JButton("Export receipts");
        JButton button6 = new JButton("Tax Calculations");

        button1.addActionListener(new ButtonActionListener());
        button2.addActionListener(new ButtonActionListener());
        button3.addActionListener(new ButtonActionListener());
        button4.addActionListener(new ButtonActionListener());
        button5.addActionListener(new ButtonActionListener());
        button6.addActionListener(new ButtonActionListener());

        add(button1);
        add(button2);
        add(button3);
        add(button4);
        add(button5);
        add(button6);
    }

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();

            switch (buttonText) {
                case "Report Invoice":
                    // Switch to Reporting Invoice Panel
                    parentPanel.removeAll();
                    parentPanel.add(new ReportingInvoicePanel(loggedInUserDetails));
                    parentPanel.revalidate();
                    parentPanel.repaint();
                    break;
                case "Import receipts":
                    // Switch to Import Receipts Panel
                    parentPanel.removeAll();
                    parentPanel.add(new ImportReceiptsPanel());
                    parentPanel.revalidate();
                    parentPanel.repaint();
                    break;
                case "Export receipts":
                    // Switch to Export Receipts Panel
                    parentPanel.removeAll();
                    parentPanel.add(new ExportReceiptsPanel());
                    parentPanel.revalidate();
                    parentPanel.repaint();
                    break;
                case "Partner Balance":
                    parentPanel.removeAll();
                    parentPanel.add(new PartnerBalancePanel(loggedInUserDetails));
                    parentPanel.revalidate();
                    parentPanel.repaint();
                    break;
                case "Tax Calculations":
                    parentPanel.removeAll();
                    parentPanel.add(new TaxPanel(loggedInUserDetails));
                    parentPanel.revalidate();
                    parentPanel.repaint();
                    break;
                case "Products":
                    parentPanel.removeAll();
                    parentPanel.add(new ProductsPanel(loggedInUserDetails));
                    parentPanel.revalidate();
                    parentPanel.repaint();
                    break;

                default:
                    System.out.println("Button clicked: " + buttonText);
            }
        }
    }
}
