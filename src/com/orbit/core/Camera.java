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
}
