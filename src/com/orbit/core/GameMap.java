package com.orbit.core;

public class GameMap {

	public int spacing;
	public int width;
	public int height;
	
	public GameMap(GameMap gm) {
		this.width   = gm.width;
		this.height  = gm.height;
		this.spacing = gm.spacing;
	}

	public GameMap() {
		width = height = spacing = 0;
	}

	public void setWidth(int w) {
		width = w;
	}

	public void setHeight(int h) {
		height = h;
	}
	
	public void setSpacing(int s) {
		spacing = s;
	}

}
