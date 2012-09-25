package com.orbit.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

public class GameEntity extends Moveable {
	
	public int mapLevel = 0;
	public int width;
	public int height;
	private float mass;
	private String file;
	String scriptFile;
	private Game gameHandle;
	
	public int id;
	private Texture texture;
	public String animationFile;
	
	public GameEntity(Game g) {
		gameHandle = g;
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		width = height = 0;
		animationFile = null;
		scriptFile = null;
		mass = -1;
	}
		
	/**
	 * Draws the GameEntity object to the game screen. Uses the 
	 * top-left corner of the entity as the local origin.
	 */
	public void draw() {
		texture.bind();
		
		GL11.glPushMatrix();
		GL11.glColor4f(1f, 1f, 1f, 1f);
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
	
	/**
	 * Attempts to translate the GameEntity object in the y-direction by 'delta'
	 * units. The method returns the actual translated amount, allowing the method
	 * to be recursed upon to attenuate the overall translation value.
	 * 
	 * The method will return 0 if the movement of delta would push the entity off
	 * the map or into a 'collidable' MapTile.
	 */
	@Override
	public float translateY(float delta) {

		float multiplier = 1;
		
		if (PhysicsUtilities.onMapY(this, delta, gameHandle.gameMap) && 
				!PhysicsUtilities.tileCollision(this, 0, delta, gameHandle.gameMap)) {
			for (GameEntity e : gameHandle.gameEntities) {
				if (PhysicsUtilities.entityCollision(this, 0, delta, e)) {
					if (e.mass >= 0) {
						multiplier -= e.mass;
						multiplier *= e.translateY(delta * multiplier);
					}
					if (e.scriptFile != null && this == gameHandle.playerFocusEntity) {
						gameHandle.onInteract(e);
						break;
					}
				}
			}
			position.y += delta * multiplier;
			return multiplier;
		}
		
		return 0;
	}
	
	/**
	 * Attempts to translate the GameEntity object in the x-direction by 'delta'
	 * units. The method returns the actual translated amount, allowing the method
	 * to be recursed upon to attenuate the overall translation value.
	 * 
	 * The method will return 0 if the movement of delta would push the entity off
	 * the map or into a 'collidable' MapTile.
	 */
	@Override
	public float translateX(float delta) {

		float multiplier = 1;

		if (PhysicsUtilities.onMapX(this, delta, gameHandle.gameMap) &&
				!PhysicsUtilities.tileCollision(this, delta, 0, gameHandle.gameMap)) {
			for (GameEntity e : gameHandle.gameEntities){ 
				if (PhysicsUtilities.entityCollision(this, delta, 0, e)) {
					if (e.mass >= 0) { 
						multiplier -= e.mass;
						multiplier *= e.translateX(delta * multiplier);
					}
					if (e.scriptFile != null && this == gameHandle.playerFocusEntity) {
						gameHandle.onInteract(e);
						break;
					}
				}
			}
			position.x += delta * multiplier;

			return multiplier;
		}
		
		return 0;
	}

	/**
	 * Determines if the camera should lock to the player in the N/S directions. Effectively,
	 * determines if there is any excess height to the map beyond the confines of the window. 
	 * If there is, then the camera should lock onto the player so that the player does not walk
	 * out off of the screen.
	 */
	public boolean cameraLockNS(GameMap map, GraphicsManager graphicsManager) {
		boolean lock = false;
		int mapHeight = map.mapCanvas.get(mapLevel).mapHeight * map.tileDimensions;
		int screenHeight = graphicsManager.getHeight();
		int top = mapHeight - screenHeight + (graphicsManager.border*2); //excess height

		if (position.y > ((mapHeight/2) - top/2) &&
				position.y < ((mapHeight/2) + top/2)) {
			lock = true;
		}
		
		return lock;
	}
	
	/**
	 * Determines if the camera should lock to the player in the E/W directions. See
	 * cameraLockNS.
	 */
	public boolean cameraLockEW(GameMap map, GraphicsManager graphicsManager) {
		boolean lock = false;
		int mapWidth = map.mapCanvas.get(mapLevel).mapWidth * map.tileDimensions;
		int screenWidth = graphicsManager.getWidth();
		int right = mapWidth - screenWidth + (graphicsManager.border*2); //excess width

		if (position.x > ((mapWidth/2) - right/2) &&
				position.x < ((mapWidth/2) + right/2)) {
			lock = true;
		}
		
		return lock;
	}

	/**
	 * Loads the default idle animation for the GameEntity object. This is expected to
	 * be the animation frame designated in the resource file as 'idle_south'.
	 */
	public void loadFirstTimeAnimation() {
		try {
			gameHandle.textureManager.loadCycle(this, this.getAnimationFile());
			setTexture(gameHandle.textureManager.setFrame(this.id, "idle_south"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Will load the appropriate idle animation frame for the GameEntity object. Meant to
	 * be used after the user lets go of a movement key. For example, if the last animation
	 * played included the word 'north' in its title, then this method will select the
	 * 'idle_north' animation frame as the appropriate idle animation.
	 */
	public void idle() {
		String last = gameHandle.textureManager.lastAnimation(this.id);
		String idleAnim = "idle_south";
		
		if (last.contains("south"))
			idleAnim = "idle_south";
		else if (last.contains("north"))
			idleAnim = "idle_north";
		else if (last.contains("east"))
			idleAnim = "idle_east";
		else if (last.contains("west"))
			idleAnim = "idle_west";
		
		this.setTexture(gameHandle.textureManager.setFrame(this.id, idleAnim));		
	}
	
	//------------------ Getters/Setters ------------------//
	
	public void setScriptFile(String str) {
		str = str.substring(str.lastIndexOf("/")+1, str.indexOf("."));
		scriptFile = str;
	}
	
	public void setAnimationFile(String str) {
		animationFile = str;
		loadFirstTimeAnimation();
	}

	public void setPosition(float[] floatArr) {
		position.set( floatArr[0], floatArr[1], floatArr[2]);
	}
	
	public void setRotation(float[] floatArr) {
		rotation.set(floatArr[0], floatArr[1], floatArr[2]);
	}
	
	public void setFile(String f) 	 	{ file = f; }
	public void setWidth(int w) 		{ width = w; }
	public void setHeight(int h) 	    { height = h; }
	public void setTexture(Texture tex) { texture = tex; }
	public void setMass(float m) 		{ mass = m; }

	public int getWidth()            { return width; }
	public int getHeight() 			 { return height; }
	public Vector3f getPosition() 	 { return position; }
	public Vector3f getRotation() 	 { return rotation; }
	public String getFile() 		 { return file; }
	public String getAnimationFile() { return animationFile; }
}
