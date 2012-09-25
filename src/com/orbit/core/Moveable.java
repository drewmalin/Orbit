package com.orbit.core;

import org.lwjgl.util.vector.Vector3f;

public class Moveable {
	Vector3f position;
	Vector3f rotation;
	
	public void setPosition(float x, float y, float z) {
		if (position == null) position = new Vector3f(x, y, z);
		else position.set(x, y, z);
	}
	
	public void setRotation(float x, float y, float z) {
		if (rotation == null) rotation = new Vector3f(x, y, z);
		else rotation.set(x, y, z);
	}
	
	/**
	 * translateX
	 * @param delta
	 * @return Returns the amount translated.
	 */
	public float translateX(float delta) {
		position.x += delta;
		return delta;
	}
	
	/**
	 * translateY
	 * @param delta
	 * @return Returns the amount translated.
	 */
	public float translateY(float delta) {
		position.y += delta;
		return delta;
	}
	
	public Vector3f getPosition() { return position; }
	public Vector3f getRotation() { return rotation; }

}
