package com.orbit.core;

import org.lwjgl.util.vector.Vector3f;

public class Camera {
	public Vector3f location;
	public Vector3f target;
	
	private Game gameHandle;
	
	public Camera(Game g) {
		location   = new Vector3f(0f, 0f, 0f);
		target 	   = new Vector3f(0f, 0f, 0f);
		gameHandle = g;
	}

	public void setLocation(Vector3f vector3f) {
		location.set(vector3f);
	}

	public void setTarget(Vector3f vector3f) {
		target.set(vector3f);
	}

	public void translateY(int i) {
		location.y += i;
	}
	
	public void translateX(int i) {
		location.x += i;
	}

	public int getPosition(int i) {
		switch (i) {
			case 0:
				return (int) location.x;
			case 1:
				return (int) location.y;
			case 2:
				return (int) location.z;
		}
		return 0;
	}

	public boolean hitBorder(String op) {
		//temp to support grid map (spacing shouldn't exist with textured levels)
		int width = gameHandle.currentLevel.width * gameHandle.currentLevel.spacing;
		int height = gameHandle.currentLevel.height * gameHandle.currentLevel.spacing;
		
		if (op.equals("N")) {
			if (location.y < (gameHandle.graphicsManager.getHeight()/2) - 30)
				return true;
		}
		else if (op.equals("W")) {
			if (location.x < (gameHandle.graphicsManager.getWidth()/2) - 30)
				return true;
		}
		else if (op.equals("E")) {
			if (location.x > (width - gameHandle.graphicsManager.getWidth()/2) + 30)
				return true;
		}
		else if (op.equals("S")) {
			if (location.y > (height - gameHandle.graphicsManager.getHeight()/2) + 30)
				return true;
		}
		return false;
	}
}
