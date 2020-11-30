package com.pisoft.mistborn_game.levels;

import java.util.ArrayList;

public class LoadedLevels {
	
	private static ArrayList<Platform> platforms = new ArrayList<Platform>();
    private static ArrayList<Metal> metals = new ArrayList<Metal>();
    
    private static Level level1;
	
	public static void initLevels() {
		initPlatforms();
		initMetals();
		
		setLevel1(new Level(platforms, metals));
	}

	private static void initPlatforms() {
        // new Platform(xPos, yPos, width, height);
        // new Platform(0, 300, 500, 50);
        platforms.add(new Platform(250, 100, 50, 500));
        // platforms.add(new Platform(450, 100, 50, 500));
        // new Platform(0, 100, 500, 50);
        platforms.add(new Platform(0, 500, 1500, 50));
    }

    private static void initMetals() {
        //new Metal(xPos, yPos);
        metals.add(new Metal(350, 525));
    }
    
    //getters and setters
    //---------------------------------------------------------------------------------------------------
	public static Level getLevel1() {
		return level1;
	}

	public static void setLevel1(Level level1) {
		LoadedLevels.level1 = level1;
	}
}
