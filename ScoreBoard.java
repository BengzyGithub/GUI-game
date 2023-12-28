package ScorePanel;

import javax.swing.*;

import balls.BallAnimation;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
/**
 * This class will create a Panel to the left of the animation frame and display
 * the player,timer and enemy score
 */
public class ScoreBoard extends JPanel {
    private JLabel playerScoreJL;
    private JLabel enemyScorelJL;
    private JLabel timerLabel;
    private int playerScore;
    private int enemyScore;
    public int seconds;
    private Timer timer;

    public ScoreBoard() {
        playerScore = 0;
        enemyScore = 0;
        seconds = 0;

        playerScoreJL = new JLabel("Player Score: " + playerScore);
        enemyScorelJL = new JLabel("Enemy Score: " + enemyScore);
        timerLabel = new JLabel("Time: " + seconds + "s");

        setLayout(new GridLayout(3, 1));
        add(playerScoreJL);
        add(enemyScorelJL);
        add(timerLabel);
    }

    public void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                SwingUtilities.invokeLater(() -> timerLabel.setText("Time: " + seconds + "s"));
            }
        }, 1000, 1000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void resetScore() {
        playerScore = 0;
        enemyScore = 0;
        playerScoreJL.setText("Player Score: " + playerScore);
        enemyScorelJL.setText("Enemy Score: " + enemyScore);
    }

    public void resetTimer() {
        stopTimer();
        seconds = 0;
        timerLabel.setText("Time: " + seconds + "s");
    }

    public void UpdatePlayerScore() {
        playerScore++;
        playerScoreJL.setText("Player Score: " + playerScore);
    }

    public void UpdateEnemyScore() {
        enemyScore++;
        enemyScorelJL.setText("Enemy Score: " + enemyScore);
    }

//get the player score when this method is called
public int getPlayerScore()
{
    return playerScore;
}

//get the enemy's score when this method is called 
public int getEnemyScore()
{
    return enemyScore;
}

public javax.swing.Timer getTimer()
{
    return BallAnimation.levelChangeTimer;
}

}
