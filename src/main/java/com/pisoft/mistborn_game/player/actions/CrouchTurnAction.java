package com.pisoft.mistborn_game.player.actions;

import com.pisoft.mistborn_game.player.Side;

public class CrouchTurnAction extends PlayerAction {
	
	private Side direction;
	
	public CrouchTurnAction(Side direction) {
		this.direction = direction;
	}
	
	@Override
	public void resolve() {
		targetPlayer.setWantsToAccelerate(true);
		targetPlayer.setFacingSide(direction);
	}

}
