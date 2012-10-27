package com.orbit.core;

import org.lwjgl.util.vector.Vector3f;

public class Camera extends Moveable {
	public Vector3f target;
	
	private final Game gameHandle;
	
	public Camera(Game g) {
		target 	   = new Vector3f(0f, 0f, 0f);
		gameHandle = g;
	}

	public int getPosition(int i) {
		switch (i) {
			case 0:
				return (int) position.x;
			case 1:
				return (int) position.y;
			case 2:
				return (int) position.z;
		}
		return 0;
	}
	
	public void setTarget(Vector3f vector3f)   { target.set(vector3f); }

	/*
	 * A super messy way to initiate the camera position without gameplay-input (meant to be run exactly once,
	 * at the beginning of the loading of a level). 
	 */
	public void findPlayer() {
		boolean lockNS = gameHandle.playerFocusEntity.cameraLockNS(gameHandle.gameMap, gameHandle.graphicsManager);
		boolean lockEW = gameHandle.playerFocusEntity.cameraLockEW(gameHandle.gameMap, gameHandle.graphicsManager);
		
		int mapHeight = gameHandle.gameMap.mapCanvas.get(gameHandle.playerFocusEntity.mapLevel).mapHeight *  gameHandle.gameMap.tileDimensions;
		int mapWidth = gameHandle.gameMap.mapCanvas.get(gameHandle.playerFocusEntity.mapLevel).mapWidth * gameHandle.gameMap.tileDimensions;

		int screenHeight = gameHandle.graphicsManager.getHeight();
		int screenWidth = gameHandle.graphicsManager.getWidth();

		int top = mapHeight - screenHeight + (gameHandle.graphicsManager.border*2); //excess height
		int right = mapWidth - screenWidth + (gameHandle.graphicsManager.border*2);
		
		setPosition(0, 0, 0);
		
		if (lockNS)
			position.y = gameHandle.playerFocusEntity.position.y;
		else {
			if (gameHandle.playerFocusEntity.position.y > ((mapHeight/2) - top/2))
				position.y = mapHeight/2 + top/2;
			else
				position.y = mapHeight/2 - top/2;
		}
		
		if (lockEW)
			position.x = gameHandle.playerFocusEntity.position.x;
		else {
			if (gameHandle.playerFocusEntity.position.x > ((mapWidth/2) - right/2))
				position.x = mapWidth/2 + right/2;
			else
				position.x = mapWidth/2 - right/2;
		}
	}
}
