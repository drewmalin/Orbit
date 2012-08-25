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
}
