package balls;
//importing other classes and their packages
import Menus.GameMenu;
import ScorePanel.ScoreBoard;
import firstgame.App;
import rectangles.EnemyRectangle;
//other swing and awt imports
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.border.LineBorder;

import javax.imageio.ImageIO;
import javax.swing.*;

public class BallAnimation extends JFrame implements ActionListener, KeyListener {
    // A pair of buttons to start and stop the animation
    JButton stopB, startB, pauseB;
    JButton[] backgroundBtn;
    JButton[] imageBtn;
    JToolBar toolBar;
    JToolBar imageToolBar = new JToolBar();
    public String[] imagename;
    public String[] ballName;
    Image[] backgroundImage;
    Image[] ballImages;
    int GAMELEVEL = 8;
    final int BALLCOLOR = 2;
    //instances of other classes
    // Create an object of the AnimationPanel class
    // Rectangle figure to be added
    Rectangle rectangle = new Rectangle(100, 50, 100, 10);
    private int rectangleSpeed = 50;
    AnimationPanel animationPanel;
    ScoreBoard scoreBoard;
    private LevelChange levelChange;
    // timer that checks the player's score every second 
    public static Timer levelChangeTimer;


// Constructor for BallAnimation class.
public BallAnimation() {
    animationPanel = new AnimationPanel(this);
    setLayout(new BorderLayout());

    //innitialize or create ScoreBoard and enemy rectangle
    scoreBoard = new ScoreBoard();

    // Initialize LevelChange
    levelChange = new LevelChange(animationPanel,this);
    //change the background color of the scoreboard panel
    scoreBoard.setBackground(Color.CYAN);


    //setting up the application Icon image and Loading it onto the app*************
    InputStream iconStream = App.class.getResourceAsStream("/ImagesToLoad/app_icon.jpg");
    Image iconImage = null;
    try {//update the image 
        iconImage = ImageIO.read(iconStream);
    } catch (IOException e) {//print the error message if the image was not properly extracted from path
        e.printStackTrace();
    }

    // Set the icon image for the frame
    setIconImage(iconImage);//******************************************************/
    // Create and populate images bar
    toolBar = new JToolBar();

    // Create and initialize the buttons and images
    backgroundBtn = new JButton[GAMELEVEL];
    backgroundImage = new Image[GAMELEVEL];
    //allocate the length of the strings
    imagename = new String[GAMELEVEL];
    imagename[0] = "dragon_ball_Image.gif";
    imagename[1] = "goku_super.gif";
    imagename[2] = "freeza_level_three.gif";
    imagename[3] = "broli_level_4.gif";
    imagename[4] = "Jiren.gif";
    imagename[5] = "Buu_level_6.gif";
    imagename[6] = "majin_android21.gif";
    imagename[7] = "majin_buu.gif";

// Loop through the number of images backgrounds and display images
for (int i = 0; i < GAMELEVEL; i++) {
    final int index = i; // Create a final copy of i
    // Obtain the URL of the image file using getResource()
    URL imageURL = getClass().getResource("/ImagesToLoad/" + imagename[i]);
    // Create an ImageIcon using the URL
    ImageIcon icon = new ImageIcon(imageURL);
    // Get the Image from the ImageIcon
    Image image = icon.getImage();
    // Resize the image to a desired width and height
    int desiredWidth = 30;
    int desiredHeight = 30;
    Image resizedImage = image.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_DEFAULT);
    // Create a new ImageIcon with the resized image
    ImageIcon resizedIcon = new ImageIcon(resizedImage);
    backgroundBtn[i] = new JButton(resizedIcon);
    backgroundBtn[i].addActionListener(e -> setBackgroundImage(backgroundImage[index]));

    // Add buttons to toolbars
    toolBar.add(backgroundBtn[i]);

    // Store the image in the array
    backgroundImage[i] = image;
}//end of loop

// sets up a timer that checks the player's score every second
levelChangeTimer = new Timer(1000, new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        final int PLAYER_WINS = 2;
        final int ENEMY_WINS = 2;

        if (scoreBoard.getEnemyScore() == ENEMY_WINS) {
            // Stop the animation thread and reset scores/timer
            animationPanel.stopAnimation();
            scoreBoard.resetTimer();
            // Enemy has won, show a confirmation dialog
            int choice = JOptionPane.showConfirmDialog(BallAnimation.this, "The enemy has won. Game over!\nStart again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            scoreBoard.resetScore(); // Reset the score
            if (choice == JOptionPane.YES_OPTION) {
                animationPanel.startAnimation(); // Start the animation
                // Player chose "Yes," refresh the game and start over from that level
                scoreBoard.startTimer();
            } else {
                // Player chose "No," do nothing or return
            }
        } else if (scoreBoard.getPlayerScore() == PLAYER_WINS) {
            if (levelChange.changeLevel()) {
                // Player has won the level
                animationPanel.stopAnimation(); // Pause the animation
                // Check if the current level is the maximum (7)
                if (levelChange.getCurrentLevel() < 7) {
                    // Display a message for advancing to a new level
                    JOptionPane.showMessageDialog(BallAnimation.this, "You advanced to level " + levelChange.getCurrentLevel(), "Notice", JOptionPane.INFORMATION_MESSAGE);
                    // Start back the animation
                    animationPanel.startAnimation();
                } else {
                    if (scoreBoard.getPlayerScore() != PLAYER_WINS && scoreBoard.getEnemyScore() != ENEMY_WINS) {
                        // Player has reached the final boss level
                        JOptionPane.showMessageDialog(BallAnimation.this, "FINAL BOSS LEVEL! You reached the final stage, brave soldier!", "Notice", JOptionPane.INFORMATION_MESSAGE);
                        // Continue the game; you can add additional logic here if needed
                    } else if (scoreBoard.getPlayerScore() == PLAYER_WINS) {
                        // Player has completed the game
                        JOptionPane.showMessageDialog(BallAnimation.this, "Congratulations! You completed the game and your wish has been granted.\n\nFeel free to start a new journey.\n\nYour completion time is: " + scoreBoard.getTimer(), "Winner", JOptionPane.INFORMATION_MESSAGE, null);
                        scoreBoard.resetScore();
                        scoreBoard.resetTimer();
                        animationPanel.stopAnimation(); // Stop the thread
                        levelChangeTimer.stop(); // Stop the timer
                    } else if (scoreBoard.getEnemyScore() == ENEMY_WINS) {
                        // Stop the animation thread and reset scores/timer
                        animationPanel.stopAnimation();
                        scoreBoard.resetTimer();
                        // Enemy has won, show a confirmation dialog
                        int choice = JOptionPane.showConfirmDialog(BallAnimation.this, "The enemy has won. Game over!\nStart again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                        scoreBoard.resetScore(); // Reset the score
                        if (choice == JOptionPane.YES_OPTION) {
                            animationPanel.startAnimation(); // Start the animation
                            // Player chose "Yes," refresh the game and start over from that level
                            scoreBoard.startTimer();
                        } else {
                            // Player chose "No," do nothing or return
                        }
                    }
                }
            } else {
                // Player has not won and the enemy has not won, continue the game
                // You can add any additional logic or handling here as needed
            }
        }
    }
});
// end of levelChangeTimer
levelChangeTimer.start(); // start the timer


    // Create the panel to add buttons
    JPanel startStopPanel;
    startStopPanel = new JPanel();
    startStopPanel.setBorder(new LineBorder(Color.PINK));

    /**Create buttons to stop, pause and start the thread**/
    stopB = new JButton("Stop");
    stopB.setBackground(Color.RED);
    stopB.addActionListener(this);
    //start
    startB = new JButton("Play");
    startB.setBackground(Color.GREEN);
    startB.addActionListener(this);
    //pause
    pauseB = new JButton("Pause game");
    pauseB.setBackground(Color.ORANGE);
    pauseB.addActionListener(this);

    // Add the stop and start buttons to the south Panel
    //startStopPanel.add(imageToolBar);
    startStopPanel.add(startB);
    startStopPanel.add(pauseB);
    startStopPanel.add(stopB);
    // Add the toolbar to the panel
    startStopPanel.add(toolBar);

    // A container  to add all the sub-Panels to the Frame
    Container container = getContentPane();
    container.setLayout(new BorderLayout(0, 0));
    container.add(animationPanel, BorderLayout.CENTER);
    container.add(startStopPanel, BorderLayout.SOUTH);
    container.add(scoreBoard, BorderLayout.EAST);

    // Call createAndShowMenu method from GameMenu and pass the BallAnimation panel
    GameMenu.createAndShowMenu(this);
}//end of BallAnimation Constructor 

public void setBackgroundImage(Image image) {
    animationPanel.setBackgroundImage(image);
}
    
       

/**
 * Method to connect the listeners for the buttons when clicked
 */
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == startB) {
        // Check to see if the button was clicked
        // If yes, then disable the button
        animationPanel.startAnimation();
        scoreBoard.startTimer();  // Start the timer
        startB.setEnabled(false);
        // Enable stop & pause button
        stopB.setEnabled(true);
        pauseB.setEnabled(true);
    } else if (e.getSource() == stopB) {
        // Stop the thread and disable it afterwards
        animationPanel.stopAnimation();
        scoreBoard.stopTimer();     //stop the timer
        scoreBoard.resetScore();    //reset the scores
        scoreBoard.resetTimer();    //reset the timer
        stopB.setEnabled(false);
        // Enable start & pause button
        startB.setEnabled(true);
        pauseB.setEnabled(false);
    } else if(e.getSource() == pauseB){
        //pause the animation thread 
        animationPanel.PauseAnimation();
        scoreBoard.stopTimer();
        //scoreBoard.resetTimer();
        pauseB.setEnabled(false);
        stopB.setEnabled(false);
        startB.setEnabled(true);
    }else{
        // Get the index of the clicked image button
        int index = Integer.parseInt(((JButton) e.getSource()).getActionCommand());

    // Convert the image to BufferedImage
    BufferedImage bufferedImage = toBufferedImage(backgroundImage[index]);

    // Get the RGB value of the pixel at (0, 0)
    int rgb = bufferedImage.getRGB(0,0);

    // Set the ball color to the selected image
    Color ballColor = new Color(rgb);
    animationPanel.setBallColor(ballColor);
    //set the background color to selected images   
    animationPanel.setBackgroundImage(backgroundImage[index]);
    animationPanel.repaint();
    }        
}

// Method to convert Image to BufferedImage
/**
 * 
 * @param image the image to convert to buffer
 * @return the image that has been buffered
 */
private BufferedImage toBufferedImage(Image image) {
if (image instanceof BufferedImage) {
    return (BufferedImage) image;
}

    // Create a new BufferedImage and draw the Image onto it
    BufferedImage bufferedImage = new BufferedImage(
            image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = bufferedImage.createGraphics();
    g2d.drawImage(image, 0, 0, null);
    g2d.dispose();

    return bufferedImage;
}

public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            BallAnimation ballAnimation = new BallAnimation();
            ballAnimation.createAndShowGUI();
        }
    });
}

public void createAndShowGUI() {
    // Set the focusable and request focus on the animationPanel
    animationPanel.setFocusable(true);
    animationPanel.requestFocus();
    setFocusable(true);
    // Add key listener to the JFrame to listen to keyboard events
    addKeyListener(this);

    setTitle("Dragon Balls Game");
    setSize(700, 650);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(true);
}

    @Override
    /**
     * KeyListener methods
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (animationPanel != null){
        if (key == KeyEvent.VK_LEFT) {
            rectangle.x -= rectangleSpeed; // Move to the left
            animationPanel.setRectangleX(rectangle.x); // Update the rectangle's x-coordinate
            animationPanel.repaint(); // Draw the balls images again when moving rectangle (note: level change)
        } else if (key == KeyEvent.VK_RIGHT) {
            rectangle.x += rectangleSpeed; // Move to the right
            animationPanel.setRectangleX(rectangle.x); // Update the rectangle's x-coordinate
            animationPanel.repaint();
        } else if (key == KeyEvent.VK_UP) {
            rectangle.y += rectangleSpeed;
        }

    }
}//end of keypressed 
    //to do/ completed
    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}

/**
 * Contains implementation to draw the custom panel objects
 * AnimationPanel class will propagate the background image to the Ball objects
 */
class AnimationPanel extends JPanel implements Runnable {
    private Image currentBackgroundImage;
    private ArrayList<Ball> balls;
    private Thread animThread;
    private boolean moveBalls;
    private BallAnimation ballAnimation;  // takes an instance of Ball animation
    private int rectangleX;
    private Color ballColor;
    public EnemyRectangle enemyRectangle; //an instance of Enemy rectangle
    //private LevelChange levelChange;

    public AnimationPanel(BallAnimation ballAnimation) {
        this.ballAnimation = ballAnimation;
        rectangleX = 10;
        //initialize the levelchage field
        //levelChange = new LevelChange(ballAnimation);
        //create new balls to be drawn on panel 
        balls = new ArrayList<>();
        balls.add(new Ball(Color.black, 20, 200, 200, 5, 3));
        //addNewEnemyRectangle();
        //balls.add(new Ball(Color.YELLOW, 30, 300, 300, -3, -2));
        animThread = null;  // set the thread reference to null
        moveBalls = false;
        //ballColor = Color.BLACK; // Default ball color
    }

public void setImage(Image image) {
    currentBackgroundImage = image;
    repaint();
}//end of setImage
/**
* Set the balls balls image to chosen image from screen
* @param image
*/
public void setBackgroundImage(Image image) {
        this.currentBackgroundImage = image;
        //set the background image for every balls with the method in Ball class
        for (Ball ball : balls) {
            ball.setBackgroundImage(image);
        }
        repaint();
    }
    /**
     * mutator for setting the ball color
     * @param color
     */
    public void setBallColor(Color color) {
    ballColor = color;
    for (Ball ball : balls) {
        ball.setBallColor(ballColor);
    }
    repaint();
}


/**
 * Method to start the thread 
 */
    public void startAnimation() {
        if (animThread == null) {
            animThread = new Thread(this);
            moveBalls = true;
            animThread.start();
        }
    }
/**
 * This method is used to stop the thread
 */
    public void stopAnimation() {
        if (animThread != null) {
            moveBalls = false;
            //stop the thread from running
            animThread = null;
        }
    }

    public void PauseAnimation() {
        if (animThread != null) {
            moveBalls = false;
            //stop the thread from running
            animThread = null;
        }
    }
/**
* runnable method to pass the bounds of the playerRec and 
* enemyRectangle to the move method of each ball.
*/
public void run() {
    while (Thread.currentThread() == animThread) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //check if balls are moving and then bounce off any using the collision detection
        if (moveBalls) {
            Rectangle rectangleBounds = new Rectangle(ballAnimation.rectangle.x,
                    getHeight() - ballAnimation.rectangle.height,
                    ballAnimation.rectangle.width,
                    ballAnimation.rectangle.height);
            Rectangle enemyRectangleBounds = new Rectangle(enemyRectangle.x, enemyRectangle.y, enemyRectangle.width, enemyRectangle.height);
            //loop through all the balls and move them accordingly
            for (Ball ball : balls) {
                ball.move(getBounds(), rectangleBounds, enemyRectangleBounds, ballAnimation.scoreBoard);
            }
            //move the enemy rectangle
            enemyRectangle.move(getBounds());

            repaint();
        }
    }
}


/**
 * Method to get the rectangle x axis
 * @return
 */
public int getRectangleX() {
    return rectangleX;
}
/**
 * Method to set the rectangle x axis
 * @param x
 */
public void setRectangleX(int x) {
    rectangleX = x;
}

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    //render the two dimentional shape and images (Rectangles, balls and Animation panel)
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    g2d.drawImage(currentBackgroundImage, 0, 0, getWidth(), getHeight(), this);
    // Initialize the enemy rectangle
    if (enemyRectangle == null) {
        enemyRectangle = new EnemyRectangle(100, getHeight() - 515, 100, 10, 2, Color.ORANGE);
    }

    g2d.setColor(Color.GREEN); //render/set player rectangle color
    g2d.fillRect(ballAnimation.rectangle.x, getHeight() - ballAnimation.rectangle.height,
    ballAnimation.rectangle.width, ballAnimation.rectangle.height);

    for (Ball ball : balls) {
        g2d.setClip(new Ellipse2D.Double(ball.x, ball.y, ball.radius * 2, ball.radius * 2));
        ball.draw(g2d);
        g2d.setClip(null);
    }

    g2d.dispose();

    // Draw the enemy rectangle
if (enemyRectangle != null) {
    enemyRectangle.draw(g);
}
}//end of PaintComponent 

public void resetBalls() {
    balls.clear(); // Clear the existing balls
    balls.add(new Ball(ballColor, 20, 200, 200, 5, 3)); // Add a new ball
}

/**
 * Method to add a new enemy rectangle and set a new x axis postion 
 */
public void addNewEnemyRectangle() {
  if (enemyRectangle != null) {
    // Calculate a random X-axis position within the bounds of the game screen
    int randomX = (int) (Math.random() * (getWidth() - enemyRectangle.width));
    
    // Create a new enemy rectangle at the random X-axis position
    enemyRectangle = new EnemyRectangle(randomX, getHeight() - 415, 100, 10, 2, Color.RED);
  }
}


}//end of Animation class

/**
 * ball class that implements the type of ball to draw
 */
class Ball {
    private Color ballColor;
    int radius;
    int x;
    int y;
    private int dx, dy;
    private Image backgroundImage;
/**
 * contructor of the ball class to initialize the data members
 * @param color ball color 
 * @param radius the radious that will shape the ball 
 * @param x      the x axis of the ball (left and right movement)
 * @param y      the y axis of teh ball (up and down movement)  
 * @param dx     diameter of the x axis
 * @param dy     diameter of the y axis
 */
    public Ball(Color color, int radius, int x, int y, int dx, int dy) {
        this.ballColor = color;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void setBallColor(Color color) {
        ballColor = color;
    }

    public void setBackgroundImage(Image image) {
        backgroundImage = image;
    }

    public void move(Rectangle bounds, Rectangle rectangleBounds, Rectangle enemyRectangleBounds, ScoreBoard scoreBoard) {
    x += dx;
    y += dy;
// When hitting the left border
    if (x < bounds.x){
        dx = -dx;                       // Reverse the Horizontal direction
        x = bounds.x;                   // Set y position to the top border
        //scoreBoard.UpdateEnemyScore();  // Call to Increase the Enemy's score
    }else if(x + radius * 2 > bounds.x + bounds.width ){//when hitting the right border
        dx = -dx;
        x = bounds.x + bounds.width - radius * 2;// Set x position to just inside the bottom border
    }
// When hitting the top border
    if (y < bounds.y) {  
        dy = -dy;                       // Reverse the vertical direction
        y = bounds.y;                   // Set y position to the top border
        scoreBoard.UpdatePlayerScore();  // Call to Increase the player's score
    } else if (y + radius * 2 > bounds.y + bounds.height) {  // When hitting the bottom border
        dy = -dy;
        y = bounds.y + bounds.height - radius * 2;  // Set y position to just inside the bottom border
        scoreBoard.UpdateEnemyScore();  // Call to Increase the Enemy's score
    }
// check for collision of the palyer rectangle and the balls 
    if (getBounds().intersects(rectangleBounds)) {
        if (x <= rectangleBounds.x || x >= rectangleBounds.x + rectangleBounds.width - radius) {
            dx = -dx;
        } else {
            dx = +dx;
        }
        dy = -dy;
    }
// check for collision of the enemey rectangle and the balls 
    if (getBounds().intersects(enemyRectangleBounds)) {
        dx = -dx;
        dy = -dy;
    }
}//end of move method

    public Rectangle getBounds() {
        return new Rectangle(x, y, radius * 2, radius * 2);
    }

    /**
     *  method to draw the ball as an ellipse using the Graphics2D object. 
     * @param g graphics interface to use
     */
    public void draw(Graphics g) {
        //create a 2d graphics to maintain the shape operation
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(ballColor);
        g2d.fillOval(x, y, radius * 2, radius * 2);

        if (backgroundImage != null) {
            g2d.setClip(new Ellipse2D.Double(x, y, radius * 2, radius * 2));
            g2d.drawImage(backgroundImage, x, y, radius * 2, radius * 2, null);
            //clear the current clip.
            g2d.setClip(null);
        }

        g2d.dispose();
    }
}


