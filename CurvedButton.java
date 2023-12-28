package OtherJTools;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * This class will set up a custom Jbutton that has a curved 
 * side to resemble the newer Windows 11 buttons 
 */
public class CurvedButton extends JButton {
    //Data members to tweak the button appearances
    private static final int ARC_WIDTH = 30;
    private static final int ARC_HEIGHT = 30;
    private Color buttonColor;
    private static final Color BUTTON_HOVER_COLOR = new Color(31, 123, 205);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    /**
     * Constructor for the Curved button class
     * @param text text to display in button
     * @param buttonColor color of the button
     */
    public CurvedButton(String text, Color buttonColor) {
        super(text);
        this.buttonColor = buttonColor;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(BUTTON_TEXT_COLOR);
        setFont(new Font("Arial", Font.BOLD, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double(0, 0, width, height, ARC_WIDTH, ARC_HEIGHT);

        if (getModel().isPressed()) {
            g2.setColor(buttonColor.darker());
        } else if (getModel().isRollover()) {
            g2.setColor(BUTTON_HOVER_COLOR);
        } else {
            g2.setColor(buttonColor);
        }

        g2.fill(roundRect);
        g2.setColor(BUTTON_TEXT_COLOR);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getHeight();

        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();

        g2.drawString(getText(), x, y);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border painting for curved button
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension superPreferredSize = super.getPreferredSize();
        int width = superPreferredSize.width + ARC_WIDTH;
        int height = superPreferredSize.height + ARC_HEIGHT;
        return new Dimension(width, height);
    }
}
