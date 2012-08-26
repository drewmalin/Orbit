package com.orbit.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class GameEntity {

	private Vector3f position;
	private Vector3f rotation;
	
	private int width;
	private int height;
	private String file;
	
	public int id;
	
	public GameEntity() {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
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
	
	public void setRotation(float[] floatArr) {
		rotation.set(
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

	public void draw() {
		GL11.glColor3d(0, 1, 0);
		GL11.glTranslatef(position.x, position.y, position.z);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(0, 0);
		GL11.glVertex2f(0, height);
		GL11.glVertex2f(width, height);
		GL11.glVertex2f(width, 0);
		GL11.glEnd();
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setFile(String f) {
		file = f;
	}
	
	public String getFile() {
		return file;
	}
}
