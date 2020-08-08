package com.pisoft.mistborn_game.player.intents;

import java.awt.Point;

import com.pisoft.mistborn_game.player.actions.PlayerAction;
import com.pisoft.mistborn_game.player.actions.SteelPushAction;

public class SteelPushIntent extends PlayerIntent {
	
	private Point location;
	
	public SteelPushIntent(Point location) {
		this.location = location;
	}
	
	@Override
	public PlayerAction parseIntent() {		
		return new SteelPushAction(location);
	}
}
