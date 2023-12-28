package firstgame;

import java.awt.Color;
import javax.swing.*;

public class WelcomeUser {
    /**
     * Method to show the user a welcome dialog box for
     * Logging in and creating an account
     */
    public static void showWelcomeDialog() {
        // Store the options to display in an array
        String[] options = {"Create Account", "Custom Sign In", "sign in with Google"};
        int choice = JOptionPane.showOptionDialog(null,
                ""+"\t\t                    Welcome to the Game App!\n\t                   Please select an option:",
                "Welcome",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        // User clicked "Create Account"
        if (choice == 0) {

            // Integrate the account class
            CreateAcct createAcctFrame = new CreateAcct();
            createAcctFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            createAcctFrame.setBackground(Color.BLUE);
            createAcctFrame.setVisible(true);
            JOptionPane.getRootFrame().dispose(); // Close the JOptionPane after selection

        } else if (choice == 1) {

            return;
        }
        // User clicked "sign in with Goolge Account"
       else if (choice == 2) {
            //call the loginwithgoolge class to innitiate the frame
            new LogInWithGoogle();
            JOptionPane.getRootFrame().dispose(); // Close the JOptionPane after selection

        }
    }
}
