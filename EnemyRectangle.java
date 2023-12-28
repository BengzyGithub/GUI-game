package rectangles;
import java.awt.*;

//import balls.BallAnimation;

public class EnemyRectangle {
    public int x;
    public int y;
    public int width;
    public int height;
    private int speed;
    private int direction;
    private Color color;

    public EnemyRectangle(int x, int y, int width, int height, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.direction = 1; // Start moving to the right
        this.color = color;
    }

    public void move(Rectangle bounds) {
        x += direction * speed;

        // Reverse direction if hitting the boundaries
        if (x <= bounds.x || x + width >= bounds.x + bounds.width) {
            direction = -direction;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        g.setColor(color); // Set the color before drawing
        g.fillRect(x, y, width, height);
    }
}

