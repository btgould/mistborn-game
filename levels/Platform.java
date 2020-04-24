package levels;

//graphics imports
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;

//import to scale drawings
//import display.Display;

public class Platform {

    private double xPos;
    private double yPos;

    private double width;
    private double height;

    private static ArrayList<Platform> platforms = new ArrayList<Platform>();

    public Platform(double xPos, double yPos, double width, double height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

        platforms.add(this);
    }

    public void drawShape(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.black);

        AffineTransform at = AffineTransform.getTranslateInstance(this.xPos, this.yPos);

        Rectangle2D rect = new Rectangle2D.Double(0, 0, this.width, this.height);

        g2d.draw(at.createTransformedShape(rect));
    }

    //getters and setters
    //---------------------------------------------------------------------------------------------------
    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public static ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public static void setPlatforms(ArrayList<Platform> platforms) {
        Platform.platforms = platforms;
    }
}