import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.System.*;


public class Main {
    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(LandingPage::new);
    }
}