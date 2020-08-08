package com.pisoft.mistborn_game.levels;

import java.util.ArrayList;

import com.pisoft.mistborn_game.player.PlayerController;

//TODO: add items, enemies, etc.
public class Level {

	private PlayerController playerController;
	
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private ArrayList<Metal> metals = new ArrayList<Metal>();
    
    public Level(ArrayList<Platform> platforms, ArrayList<Metal> metals) {
        setPlayerController(new PlayerController());
    	setPlatforms(platforms);
        setMetals(metals);
    }

    //getters and setters
    //---------------------------------------------------------------------------------------------------
    public PlayerController getPlayerController() {
		return playerController;
	}

	public void setPlayerController(PlayerController playerController) {
		this.playerController = playerController;
	}
    
    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<Platform> platforms) {
        this.platforms = platforms;
    }

    public ArrayList<Metal> getMetals() {
        return metals;
    }

    public void setMetals(ArrayList<Metal> metals) {
        this.metals = metals;
    }
}