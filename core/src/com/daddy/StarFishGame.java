package com.daddy;

import com.badlogic.gdx.Game;

public class StarFishGame extends Game {
	@Override
	public void create () {
		TurtleLevel tl = new TurtleLevel(this);
		setScreen(tl);
	}
}
