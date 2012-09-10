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
	public boolean clean;
	
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
		clean = false;
		texture.bind();
		GL11.glPushMatrix();
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
		GL11.glPopMatrix();
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
				position.y + i < ((currentLevel.mapHeight - 1)* currentLevel.tileDimensions))
		position.y += i;
	}
	
	public void moveX(float f, GameMap currentLevel) {
		if (position.x + f > 0 &&
				position.x + f < ((currentLevel.mapWidth - 1) * currentLevel.tileDimensions))
		position.x += f;
	}

	/* cameraLockNS:
	 * Determines if the camera should lock to the player in the N/S directions. Effectively,
	 * determines if there is any excess height to the map beyond the confines of the window. 
	 * If there is, then the camera should lock onto the player so that the player does not walk
	 * out off of the screen.
	 */
	public boolean cameraLockNS(GameMap map, GraphicsManager graphicsManager) {
		boolean lock = false;
		int mapHeight = map.mapHeight * map.tileDimensions;
		int screenHeight = graphicsManager.getHeight();
		int top = mapHeight - screenHeight + (graphicsManager.border*2); //excess height

		if (position.y > ((mapHeight/2) - top/2) &&
				position.y < ((mapHeight/2) + top/2)) {
			lock = true;
		}
		
		return lock;
	}
	
	/* cameraLockEW:
	 * Determines if the camera should lock to the player in the E/W directions. See
	 * cameraLockNS.
	 */
	public boolean cameraLockEW(GameMap map, GraphicsManager graphicsManager) {
		boolean lock = false;
		int mapWidth = map.mapWidth * map.tileDimensions;
		int screenWidth = graphicsManager.getWidth();
		int right = mapWidth - screenWidth + (graphicsManager.border*2); //excess width

		if (position.x > ((mapWidth/2) - right/2) &&
				position.x < ((mapWidth/2) + right/2)) {
			lock = true;
		}
		
		return lock;
	}

	public void setAnimationFile(String str) {
		animationFile = str;
	}
}
