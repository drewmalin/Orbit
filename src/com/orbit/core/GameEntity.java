package com.orbit.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

public class GameEntity {

	private Vector3f position;
	private Vector3f rotation;
	
	private int width;
	private int height;
	private String file;
	
	public int id;
	private Texture texture;
	public String animationFile;
	
	public GameEntity() {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		width = height = 0;
		animationFile = "";
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

	public void setTexture(Texture tex) {
		texture = tex;
	}
	
	public void draw() {
		texture.bind();	
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		GL11.glColor3d(1, 1, 1);
		GL11.glTranslatef(position.x, position.y, position.z);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);
			
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(width, 0);
			
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(width, height);
			
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0, height);
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

	public String getAnimationFile() {
		return animationFile;
	}
	
	public void moveY(float i, GameMap currentLevel) {
		if (position.y + i > 0 &&
				position.y + i < (currentLevel.height * currentLevel.spacing))
		position.y += i;
	}
	
	public void moveX(float f, GameMap currentLevel) {
		if (position.x + f > 0 &&
				position.x + f < (currentLevel.width * currentLevel.spacing))
		position.x += f;
	}

	/*
	 * Locked N/S/E/W: return 3
	 * Locked N/S: return 2
	 * Locked E/W: return 1
	 * Unlocked: return 0
	 */
	public int cameraLockBox(GameMap map, GraphicsManager graphicsManager) {
		int border = 30;
		int mapWidth = map.width * map.spacing;
		int mapHeight = map.height * map.spacing;
		int screenWidth = graphicsManager.getWidth();
		int screenHeight = graphicsManager.getHeight();
		
		int right = mapWidth - screenWidth + (border*2); //excess width
		int top = mapHeight - screenHeight + (border*2); //excess height

		int lock = 0; // assume outside
		
		//check inside top/bot
		if (position.y > ((mapHeight/2) - top/2) &&
				position.y < ((mapHeight/2) + top/2)) {
			lock += 2;
		}
		//check inside left/right
		if (position.x > ((mapWidth/2) - right/2) &&
				position.x < ((mapWidth/2) + right/2)) {
			lock += 1;
		}
		
		return lock;
	}

	public void setAnimationFile(String str) {
		animationFile = str;
	}
}
