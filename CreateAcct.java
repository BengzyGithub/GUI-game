//continue by separating the create account frame and the login frame

//create a package named login in src
package firstgame;

import javax.swing.*;
//import org.apache.commons.codec.binary.Base32;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Main class to run the application with a login screen.
 *
 * When run, the App will open and ask the user to create an account.
 * Upon successful creation, the user can then log in with the same
 * credentials they created and play the game.
 *
 * The app will authenticate the user upon login or creation of an account.
 */
public class CreateAcct extends JFrame implements ActionListener {
    private static JFrame frame;
    private JTextField usernameField;
    private JTextField usrEmailTF;
    private JPasswordField passwordField;
    //private JButton loginButton;
    private JButton createAccountButton;
    private Connection connection;
    private JTextField otpField;


    public CreateAcct() {
        // GUI Setup
        setTitle("Create your Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        // Add the rows and cols to the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        // Add the username fields to the panel
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        // Add the password fields to the panel
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Add the email fields to the panel
        panel.add(new JLabel("Email:"));
        usrEmailTF = new JTextField();
        panel.add(usrEmailTF);

        //label and text field for the one-time code 
        panel.add(new JLabel("One-Time Code:"));
        otpField = new JTextField();
        panel.add(otpField);

        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(this);
        panel.add(createAccountButton);

        add(panel);
        pack();//fit the prefered size and layout in window

        // Database Connection Setup
        setupDatabaseConnection();

         // Show the Welcome dialog when the application starts
         //WelcomeUser.showWelcomeDialog();
    }
    
//set the database connection 
    private void setupDatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/gamesdb\", \"root\", \"Bengz2byZ*");
        } catch (SQLException e) {
            e.printStackTrace();
        }//end of the try and catch phrase
    }

    /**
     * Method called when a specific button or event is clicked on.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createAccountButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = usrEmailTF.getText();
            String OTP_Code = otpField.getText();
            boolean created = false;

            // Check if all fields have been properly filled password is valid
            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !OTP_Code.isEmpty()) {
                if (isPasswordValid(password)) {
                    created = createUser(username, password, email, OTP_Code);
                } else {
                    JOptionPane.showMessageDialog(this, "Password must have a minimum length of 8 characters and include a combination of uppercase and lowercase letters, numbers, and special characters.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }

            if (created) {
                JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create an account. Please try again.");
            }
        }
    }//end if Action performed
    

    /**
     * Method to create a new user account.
     *
     * @param username Name of the player
     * @param password Protected password of the player
     * @param email    Email address of the player
     * @param OTP_Code One-Time Code of the player
     * @return True if the account creation is successful, False otherwise
     */
    private boolean createUser(String username, String password, String email, String OTP_Code) {
        // Check if the username or email already exists
        if (isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different one.");
            return false;
        }

        if (isEmailExists(email)) {
            JOptionPane.showMessageDialog(this, "Email already exists. Please use a different one.");
            return false;
        }

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password, email, OTP_Code) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, OTP_Code);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        System.out.println("User ID: " + userId);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method to check if the username already exists in the database.
     *
     * @param username Username to check
     * @return True if the username exists, False otherwise
     */
    private boolean isUsernameExists(String username) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method to check if the email already exists in the database.
     *
     * @param email Email to check
     * @return True if the email exists, False otherwise
     */
    private boolean isEmailExists(String email) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?")) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            //variable to keep track of # of reeated emails
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Method to validate the password against the secure password policy.
     *
     * @param password Password to validate
     * @return True if the password is valid, False otherwise
     */
    private boolean isPasswordValid(String password) {
        // Password must have a minimum length of 8 characters and not already used
        if (password.length() < 8 ) {
            return false;
        }

        // Password must include a combination of uppercase and lowercase letters, numbers, and special characters
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;
        //check each character in password to see if it matches the all combination 
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }//end of for each loop

        return hasUppercase && hasLowercase && hasNumber && hasSpecialChar;
    }

    public static void main(String[] args) {
        // Create and display the login or create account frame
        frame = new CreateAcct();
        frame.setSize(300,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true);
        frame.setForeground(Color.BLUE); 
    }

}
