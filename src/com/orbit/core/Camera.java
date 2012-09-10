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

	public void translateY(float f) {
		location.y += f;
	}
	
	public void translateX(float f) {
		location.x += f;
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
}
