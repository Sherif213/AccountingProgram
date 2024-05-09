import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SixButtonPanel extends JPanel {
    private JPanel parentPanel;

    public SixButtonPanel(JPanel parentPanel) {
        this.parentPanel = parentPanel;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new GridLayout(2, 3));

        JButton button1 = new JButton("Report Invoice");
        JButton button2 = new JButton("Partner Balance");
        JButton button3 = new JButton("Products");
        JButton button4 = new JButton("Import receipts");
        JButton button5 = new JButton("Export receipts");
        JButton button6 = new JButton("Settings");

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
                    parentPanel.add(new ReportingInvoicePanel());
                    parentPanel.revalidate();
                    parentPanel.repaint();
                    break;
                // Add cases for other buttons to switch to their respective panels
                // For now, let's print the button text for demonstration
                default:
                    System.out.println("Button clicked: " + buttonText);
            }
        }
    }
}
