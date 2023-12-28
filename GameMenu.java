package Menus;

import balls.BallAnimation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class GameMenu {
    public static void createAndShowMenu(BallAnimation ballAnimation) {
        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.BLACK);
        ballAnimation.add(menuBar, BorderLayout.NORTH);

        // Create the "Menu" menu
        JMenu menu = new JMenu("Menu");
        menu.setForeground(Color.GREEN);
        menuBar.add(menu);

        // Create the "Help" menu item
        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(helpMenuItem,
                        "Thank you for playing Dragon Balls!!\n\n" +
                                "For starters, you have the options to:\n" +
                                "1) Choose a background image when playing the game. each image represent a level\n" +
                                "2) Play by moving your left and right arrow keys to trigger the rectangle movements\n" +
                                "3) Start, stop, and pause the game for later\n" +
                                "4) View your score and level on the right panel of the screen\n" +
                                "5) Your game will be saved after you log in properly\n\n" +
                                "If you have any questions or run into issues, don't forget to shoot me a message:\n" +
                                "Email: alphonsebengz@gmail.com\n\n" +
                                "Happy Dragonballing!!");
            }
        });
        menu.add(helpMenuItem);

        // Create the "Contact" menu item
        JMenuItem contactMenuItem = new JMenuItem("Contact");
        //contactMenuItem.setText("<html>Email: alphonsebengz@gmail.com<br>Phone #: 847-200-2665<br>LinkedIn: <a href='https://www.linkedin.com/in/mylinkedinlink'>mylinkedinlink</a></html>");
        contactMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = { "OK", "Cancel" }; //0 = ok, 1 = cancel
            JOptionPane.showMessageDialog(helpMenuItem,
                        "You can reach out for more help in the following manner!!\n\n" +
                                "Options:\n" +
                                "1) Email: alphonsebengz@gmail.com\n" +
                                "2) Phone #: 847-200-2667\n" +
                                "3) LinkedIn: <html><a href='https://www.linkedin.com/in/mylinkedinlink'>\n");
            int choice = JOptionPane.showOptionDialog(ballAnimation, "Do you want to open Linkedin?\nClick OK to continue",
                "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

                // Open the LinkedIn profile in a web browser
            if (choice == 0) {
            // Open the Cash App profile in a web browser
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.linkedin.com/in/mylinkedinlink"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            }//end of if statement
        }//end of Action performed
    });
    menu.add(contactMenuItem);

// Create the "Support" menu item
JMenuItem supportMenuItem = new JMenuItem("Support or contribute");
//supportMenuItem.setText("Help developing team: alphonseCashApp");
supportMenuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        //variable names to associate with the object
        Object[] options = { "OK", "Cancel" }; //0 = ok, 1 = cancel

        int choice = JOptionPane.showOptionDialog(ballAnimation, "Like to help the developing team?\nClick OK to continue",
                "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            // Open the Cash App profile in a web browser
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://cash.app/$Phonsie4life"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }//end of if statement
    }//end of Action performed
});
menu.add(supportMenuItem);

// Create the "to be determind" menu item

        JMenuItem upcomingUpdate = new JMenuItem("Coming soon!");
        upcomingUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle setting the background image here
                // ...
            }
        });
        menu.add(upcomingUpdate);

    }//end of creatAndShowMenu 


}
