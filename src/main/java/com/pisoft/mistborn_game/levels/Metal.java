package com.pisoft.mistborn_game.levels;

public class Metal {

    private double xPos;
    private double yPos;

    public Metal(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
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
}