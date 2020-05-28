package levels;

import java.util.ArrayList;

import player.Player;

//TODO: add items, enemies, etc.
public class Level {
    
    private Player player;

    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private ArrayList<Metal> metals = new ArrayList<Metal>();
    
    public Level(Player player, ArrayList<Platform> platforms, ArrayList<Metal> metals) {
        setPlayer(player);
        setPlatforms(platforms);
        setMetals(metals);
    }

    //getters and setters
    //---------------------------------------------------------------------------------------------------
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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