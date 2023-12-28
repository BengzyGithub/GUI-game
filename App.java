package firstgame;

//continue by separating the create account frame and the login frame

//create a package named login in src
// reference the classes
import balls.BallAnimation; //import BallAnimation
//import login.CreateAcct; //import the Create account class
import OtherJTools.CurvedButton;

import javax.swing.*;

//import org.apache.commons.codec.binary.Base32;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.awt.EventQueue;
import java.awt.event.*;
import java.sql.*;

import javax.imageio.ImageIO;



/**
 * Main class to run the application with a login screen.
 *
 * When run, the App will open and ask the user to create an account.
 * Upon successful creation, the user can then log in with the same
 * credentials they created and play the game.
 *
 * The app will authenticate the user upon login or creation of an account.
 */
public class App extends JFrame implements ActionListener {
    private static JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;
    private Connection connection;
    
    //private JTextField otpField;


    public App() {
        // GUI Setup
        setTitle("Game Login");
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //setting up the application Icon image and Loading it onto the app
        InputStream iconStream = App.class.getResourceAsStream("/ImagesToLoad/app_icon.jpg");
        Image iconImage = null;
        try {//update the image 
            iconImage = ImageIO.read(iconStream);
        } catch (IOException e) {//print the error message if the image was not properly extracted from path
            e.printStackTrace();
        }

        // Set the icon image for the frame
        setIconImage(iconImage);


        // Add the rows and cols to the main panel (center panel)
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,0,10,10));

        //Panel to hold the login and create account buttons
        JPanel logCreatePan = new JPanel();
        logCreatePan.setLayout(new GridLayout(1, 0));
        //welcome message to put in the center panel while wrapped on SCP
        JTextPane WelcmTextPane = new JTextPane();
        WelcmTextPane.setContentType("text/html");
        WelcmTextPane.setText("<html>Welcome to Dragon Balls!<br><br>Embark on an epic journey to collect the mystical Dragon Balls and unlock their incredible powers. Create your account or log in to continue your adventure.<br><br>May your quest be filled with excitement, challenges, and unforgettable moments. Gather your strength and let the adventure begin!<br><br>Good luck, brave warrior!</html>");
        WelcmTextPane.setEditable(false);
        WelcmTextPane.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 10));
        WelcmTextPane.setForeground(Color.ORANGE);

        // Create a scrollPane to store the message
        JScrollPane scrollPane = new JScrollPane(WelcmTextPane); // Pass the label to the constructor
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Wrap the label in a scroll pane
        scrollPane.setViewportView(WelcmTextPane);

        // Set the preferred size of the scroll pane to control its appearance
        scrollPane.setPreferredSize(new Dimension(400, 200)); // Adjust the dimensions as needed

        //Center Panel to hold the intro Message of the App
        JPanel MessagePanel = new JPanel(new GridBagLayout());
        //MessagePanel.add(WelcmMessg);
        MessagePanel.setBackground(Color.DARK_GRAY);
        MessagePanel.add(scrollPane); // Add the scroll pane to the panel

        // Add the username fields to the panel
        JLabel userNameLabel = new JLabel("Username:");
        userNameLabel.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 14)); // Set the font and size
        userNameLabel.setForeground(Color.WHITE);
        panel.add(userNameLabel);
        usernameField = new JTextField();
        panel.add(usernameField);

        // Add the password fields to the panel
        JLabel passWLabel = new JLabel("Password:");
        passWLabel.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 14)); // Set the font and size
        passWLabel.setForeground(Color.WHITE);
        panel.add(passWLabel);
        passwordField = new JPasswordField();
        panel.add(passwordField);

        //ADD color to the center panel
        panel.setBackground(Color.BLACK);

        //Create the buttons for logging in and signing up account
        loginButton = new CurvedButton("Log in", Color.GREEN);
        loginButton.addActionListener(this);
        logCreatePan.add(loginButton);

        createAccountButton = new CurvedButton("Sign Up", Color.ORANGE);
        createAccountButton.setSize(23,23);
        createAccountButton.addActionListener(this);
        logCreatePan.add(createAccountButton);

        //Container to hold the two panels together
        Container container = getContentPane();
        container.setLayout(new BorderLayout(10, 0));
        container.add(panel, BorderLayout.NORTH);
        container.add(logCreatePan, BorderLayout.SOUTH);
        container.add(MessagePanel, BorderLayout.CENTER);

        //add(panel);

        // Database Connection Setup
        setupDatabaseConnection();

         // Show the Welcome dialog when the application starts
         WelcomeUser.showWelcomeDialog();
    }
    
//set the database connection 
    private void setupDatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/gamesdb", "root", "Bengz2byZ*");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called when a specific button or event is clicked on.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            //String OTP_Code = otpField.getText();      //variable to get the text code entered by user 
            boolean loggedIn = false;

            if (!username.isEmpty() && !password.isEmpty()) {
                loggedIn = authenticateUser(username, password);
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
            //launch the game application if the login was succesfull 
            if (loggedIn) {
                launchGame();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username, password, or one-time code. Please try again.");
            }
        } else if (e.getSource() == createAccountButton) {
            //integrate the createAcct class to use its implimentation
            JFrame creatACT = new CreateAcct();
            creatACT.setSize(500,300);
            creatACT.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            creatACT.setLocationRelativeTo(null);
            creatACT.setVisible(true); 

        }
    }//end if Action performed

    /**
     * Method to authenticate the user when logging into an account.
     *
     * @param username Name of the player
     * @param password Protected password of the player
     * @param otp One time passcode to enter
     * @return True if the login is successful, False otherwise
     */
   private boolean authenticateUser(String username, String password) {
       try {
           String query = "SELECT id, OTP_Code, email FROM users WHERE username = ? AND password = ?";
           PreparedStatement statement = connection.prepareStatement(query);
           statement.setString(1, username);
           statement.setString(2, password);
           ResultSet resultSet = statement.executeQuery();
           if (resultSet.next()) {
               // User authenticated, fetch the OTP_Code for the user
               String otpCodeFromDB = resultSet.getString("OTP_Code");
               String enteredOTP = showOneTimeDialog(); // Display OTP dialog and get user input
               //testing 
               if (otpCodeFromDB.equals(enteredOTP)) {
               resultSet.getString("email");
               //sendOneTimeCodeEmail(userEmail, otpCodeFromDB); // Send the OTP via email
               return true;
               }//end testing
               //return otpCodeFromDB.equals(enteredOTP); // Compare the OTP from DB and user input
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
   }//end of authenticator for user
    

    public String showOneTimeDialog() {
        JTextField otpField = new JTextField(); // JTextField to take OTP input
        // Store the OTP input field and label in a JPanel for layout purposes
        JPanel otpPanel = new JPanel();
        otpPanel.setLayout(new GridLayout(1, 2));
        otpPanel.add(new JLabel("One-Time Code:"));
        otpPanel.add(otpField);

        // Display the dialog
        int choice = JOptionPane.showOptionDialog(null,
                otpPanel, // Use otpPanel containing JTextField for OTP input
                "Verification",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Verify Now"},
                "Verify Now");

        // User clicked "Verify Now"
        if (choice == 0) {
            return otpField.getText(); // Return the entered OTP code
        }

        return ""; // Return empty string if the user cancels or closes the dialog
    }//end of show oneTimeDialog




    public static void main(String[] args) {
        // Create and display the login or create account frame
        frame = new App();
        frame.setSize(700,500);
        frame.setResizable(isDefaultLookAndFeelDecorated());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true);
        frame.setForeground(Color.BLUE); 
    }

    public void launchGame() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                BallAnimation ballAnimation = new BallAnimation();
                ballAnimation.createAndShowGUI();
                frame.dispose();
            }
        });
    }
}