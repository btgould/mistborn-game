package metals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import java.util.ArrayList;


public class Metal {

    private double xPos;
    private double yPos;

    private static ArrayList<Metal> metals = new ArrayList<Metal>();

    public Metal(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;

        metals.add(this);
    }

    public void drawShape(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.gray);

        AffineTransform at = AffineTransform.getTranslateInstance(this.xPos, this.yPos);

        Rectangle2D rect = new Rectangle2D.Double(0, 0, 5, 5);

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

    public static ArrayList<Metal> getMetals() {
        return metals;
    }

    public static void setMetals(ArrayList<Metal> metals) {
        Metal.metals = metals;
    }
}