package firstgame;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;


import javax.swing.*;

import java.awt.EventQueue;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.Image;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.oauth2.Oauth2;
//import com.google.api.services.oauth2.model.Userinfo;
import com.google.api.services.oauth2.model.Userinfo;
import balls.BallAnimation;

public class LogInWithGoogle extends JFrame {
    private static JFrame frame;
    private static final String CLIENT_SECRET_FILE = "/Myclient_Info/client_secret.json"; // Path to your client secret file
    private static final String REDIRECT_URI = "[\"http://localhost\"]"; // my redirect URI
    private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/gmail.insert" ; // Scopes for accessing user's email and profile

    private GoogleAuthorizationCodeFlow flow;
    private Credential credential;
    //variables to get users password and name
    String clientId;
    String clientSecret;

   // InputStream iconStream = LogInWithGoogle.class.getResourceAsStream("/resources/app_icon.png");
    //ImageIcon icon = new ImageIcon(iconStream);


    /**
     * Constructor for the the Loginwithgoogle class
     */
    public LogInWithGoogle() {

        // Load the icon image
        InputStream iconStream = LogInWithGoogle.class.getResourceAsStream("/ImagesToLoad/app_icon.jpg");
        Image iconImage = null;
        try {
            iconImage = ImageIO.read(iconStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the icon image for the frame
        setIconImage(iconImage);
        //a call to the google oath2 flow
        setupGoogleOAuthFlow();
        try {
            authenticateWithGoogle();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setupGoogleOAuthFlow() {
        HttpTransport httpTransport;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            // Read client secret JSON file
            InputStream in = LogInWithGoogle.class.getResourceAsStream(CLIENT_SECRET_FILE);
            InputStreamReader reader = new InputStreamReader(in);

            // Parse JSON using Gson
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, reader);

            clientId = clientSecrets.getDetails().getClientId();
            clientSecret = clientSecrets.getDetails().getClientSecret();

        // Use these values when building the authorization flow
        flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Collections.singleton(SCOPE))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }//end of catch
    }//end of setting up google-oath-flow

    private void authenticateWithGoogle() throws IOException {
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(clientId);

        getUserInfoFromGoogle();
    }

    private void getUserInfoFromGoogle() {
        try {
            Oauth2 oauth2 = new Oauth2.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName("DragonBalls")
                    .build();
    
            Userinfo userInfo = oauth2.userinfo().get().execute();
            String userEmail = userInfo.getEmail();
            String userName = userInfo.getName();
    
            // Do something with the user's email and name
            // For example, display a welcome message
            JOptionPane.showMessageDialog(null,
                    "Welcome, " + userName + " (" + userEmail + ")!" ,
                    "Sign In with Google",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Send the greetings email to the user's email
            //sendGreetingsEmail(userEmail);
    
            // Launch the game if the verification was successful
            if (userInfo.getVerifiedEmail()) {
                launchGame();
            } else {
                JOptionPane.showMessageDialog(null, "Sorry, this account does not seem to exist with Google. Please try again.");
            }
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to launch the game 
     */
    public void launchGame() {
        EventQueue.invokeLater(() -> {
            BallAnimation ballAnimation = new BallAnimation();
            ballAnimation.createAndShowGUI();
            this.dispose(); // Dispose the current instance
        });
    }
    
/**
 * Main method to run java instances
 * @param args
 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new LogInWithGoogle();
            frame.setSize(500, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); // Center the window on the screen
            frame.setVisible(true);
            frame.setForeground(Color.ORANGE);
        });
    }
}
