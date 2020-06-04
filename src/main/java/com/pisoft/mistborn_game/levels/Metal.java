package com.pisoft.mistborn_game.levels;

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