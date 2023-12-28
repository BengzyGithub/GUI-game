package balls;
/*
 * This class will handle level changes and the addition of the enemy rectangle.
 * 
 */
public class LevelChange {
    //adding instances of class
    private BallAnimation ballAnimation;
    private AnimationPanel animationPanel;
    private int currentLevel = 0;

    //contructor of the LevelChange class
    public LevelChange(AnimationPanel animationPanel, BallAnimation ballAnimation) {
        this.animationPanel = animationPanel;
        this.ballAnimation = ballAnimation;
    }
/**
 * Mehtod to change the level of the player by updating the background image 
 * to the sequence that they appear in (index of..) 
 */
public boolean changeLevel() {
    if (currentLevel < ballAnimation.GAMELEVEL - 1) {
        currentLevel++;
        //update background image to new level
        ballAnimation.setBackgroundImage(ballAnimation.backgroundImage[currentLevel]);
        ballAnimation.animationPanel.setBackgroundImage(ballAnimation.backgroundImage[currentLevel]);
        animationPanel.repaint();
        //animationPanel.resetBalls(); // Reset balls
        ballAnimation.scoreBoard.resetScore(); // Reset the player's score
        if(currentLevel >= 1 && currentLevel <= 3){ //only add up to 4 new enemies
        animationPanel.addNewEnemyRectangle(); // Add a new enemy rectangle
        }
    }
    return true;
}

public int getCurrentLevel()
{
    return currentLevel;
}
}
