package com.pisoft.mistborn_game.player.actions;

import java.awt.Point;

import com.pisoft.mistborn_game.Game;
import com.pisoft.mistborn_game.levels.Metal;

public class SteelPushAction extends PlayerAction {
	
	private Point location;
	
	public SteelPushAction(Point location) {
		this.location = location;
	}
	
	@Override
	public void resolve() {
		for (Metal metal : Game.getActiveLevel().getMetals()) {
            if (Math.hypot(location.x - metal.getxPos(), location.y - metal.getyPos()) < 80) {
                targetPlayer.setTargetMetal(metal);
                targetPlayer.setSteelPushing(true);
                return;
            } else {
                targetPlayer.setTargetMetal(null);
                targetPlayer.setSteelPushing(false);
            }
        }
	}
}
