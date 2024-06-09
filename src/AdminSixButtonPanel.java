import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminSixButtonPanel extends JPanel {
    private String loggedInUserDetails;

    public AdminSixButtonPanel(String loggedInUserDetails) {
        this.loggedInUserDetails = loggedInUserDetails;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new GridLayout(2, 2));

        String[] buttonLabels = {"Insert Product", "Change User Password", "Delete Partner", "Export Invoices as CSV"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonActionListener());
            add(button);
        }
    }

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();

            switch (buttonText) {
                case "Insert Product":
                    switchToPanel(new EnterProduct());
                    break;
                case "Change User Password":
                    switchToPanel(new ChangePasswordPanel());
                    break;
                case "Delete Partner":
                    switchToPanel(new DeletePartner(loggedInUserDetails));
                    break;
                case "Export Invoices as CSV":
                    switchToPanel(new ExportPanel());
                    break;
                default:
                    System.out.println("Button clicked: " + buttonText);
            }
        }

        private void switchToPanel(JPanel panel) {
            Container parent = getParent();
            if (parent instanceof JPanel) {
                JPanel parentPanel = (JPanel) parent;
                parentPanel.removeAll();
                parentPanel.add(panel);
                parentPanel.revalidate();
                parentPanel.repaint();
            } else {
                System.err.println("Error: AdminFourButtonPanel must be added to a JPanel.");
            }
        }
    }
}
