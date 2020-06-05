package com.pisoft.mistborn_game;

import java.util.ArrayList;

import com.pisoft.mistborn_game.display.Display;
import com.pisoft.mistborn_game.levels.Level;
import com.pisoft.mistborn_game.levels.Metal;
import com.pisoft.mistborn_game.levels.Platform;
import com.pisoft.mistborn_game.player.Player;

public class Game implements Runnable{
	private Display display;
	
	private boolean running = false;
	private GameState state = GameState.PLAYING;
	
	private Thread thread;
	
	private static Level activeLevel;
	private Player player = new Player();
	private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private ArrayList<Metal> metals = new ArrayList<Metal>();
	
    //for debugging: increase delay between frames to read stat menu
	private final int INTERVAL_DELAY = 1000 / 30;
	
	public Game() {
		initResources();
		initPlatforms();
        initMetals();
        
        activeLevel = new Level(player, platforms, metals);
        
		display = new Display();
	}

	private void initResources() {
		
	}
	
	private void initPlatforms() {
        // new Platform(xPos, yPos, width, height);
        // new Platform(0, 300, 500, 50);
        //this.platforms.add(new Platform(250, 100, 50, 500));
        //this.platforms.add(new Platform(450, 100, 50, 500));
        // new Platform(0, 100, 500, 50);
        this.platforms.add(new Platform(0, 500, 1500, 50));
    }

    private void initMetals() {
        //new Metal(xPos, yPos);
        this.metals.add(new Metal(350, 525));
    }
	
	public void run() {
		initResources();
		
		while (running) {
			switch (state) {
				case PLAYING:
					long beforeTime, timeDiff, sleep;
				    
		            beforeTime = System.currentTimeMillis();
		            
		            tick();
		            render();
		    
		            timeDiff = System.currentTimeMillis() - beforeTime;
		            sleep = INTERVAL_DELAY - timeDiff;
		    
		            if (sleep < 0) {
		                sleep = 2;
		            }
		    
		            try {
		                Thread.sleep(sleep);
		            } catch (InterruptedException e) {};
		            
		            beforeTime = System.currentTimeMillis();
				break;
				
				default:	
				break;
			}
		}
		
		stop();
	}
	
	//playing methods
    //---------------------------------------------------------------------------------------------------
	private void tick() {
		activeLevel.getPlayer().tick();
	}
	
	private void render() {
		display.getBoard().repaint();
	}
	
	//thread methods
    //---------------------------------------------------------------------------------------------------
	public void start() {
		if (running) {
			return;
		}
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() { 
		if (!running) {
			return;
		}
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {}
	}
	
	//getters and setters
    //---------------------------------------------------------------------------------------------------
	public static Level getActiveLevel() {
		return activeLevel;
	}

	public static void setActiveLevel(Level activeLevel) {
		Game.activeLevel = activeLevel;
	}
}
