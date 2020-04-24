package levels;

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