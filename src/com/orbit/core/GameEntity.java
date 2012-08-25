package com.orbit.core;

import org.lwjgl.util.vector.Vector3f;

public class GameEntity {

	private Vector3f position;
	private int width;
	private int height;
	
	public GameEntity() {
		position = new Vector3f(0, 0, 0);
		width = height = 0;
	}
	
	public GameEntity(GameEntity ge) {
		position = new Vector3f(ge.position);
		width    = ge.width;
		height   = ge.height;
	}

	public void setPosition(float[] floatArr) {
		position.set(
				floatArr[0],
				floatArr[1],
				floatArr[2]);
	}
	
	public void setWidth(int w) {
		width = w;
	}
	
	public void setHeight(int h) {
		height = h;
	}

}
