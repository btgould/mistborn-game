package com.pisoft.mistborn_game.player.intents;

import java.awt.Point;

import com.pisoft.mistborn_game.player.actions.SteelPushAction;

public class SteelPushIntent extends PlayerIntent {
	
	private Point location;
	
	public SteelPushIntent(Point location) {
		super();
		
		this.location = location;
	}
	
	@Override
	public void resolve() {		
		dispatchEvent(new SteelPushAction(location));
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		SteelPushIntent clone = (SteelPushIntent) super.clone();
		
		clone.location = this.location;
		
		return clone;
	}
}
