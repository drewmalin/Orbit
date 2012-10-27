package com.orbit.core;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class GraphicsManager {

	private int windowWidth			= 100;
	private int windowHeight		= 100;
	private float frust				= 60;
	private float zNear				= 0;
	private float zFar				= 100;
	private boolean fullScreen		= false;
	private boolean vSync			= false;
	private int MSAA				= 0;
	private String title			= "Lezend";
	
	private final Game gameHandle;
	public final int border = 30;
	
	private long lastCheck = 0;
	private float fadeDuration = 1000;
	
	public GraphicsManager(Game g) {
		gameHandle = g;
	}
	
	private void setGraphicsMode(String string) {
		if (string.equals("2D")) {
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);								
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_TEXTURE_2D);	
			
			GL11.glLoadIdentity();
			GL11.glOrtho(0, windowWidth + gameHandle.windowManager.gui.get("storyBox").width, windowHeight, 0, -1, 1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glShadeModel(GL11.GL_SMOOTH);
		}
		
		else if (string.equals("3D")) {
			GL11.glDepthMask(true);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			
			GLU.gluPerspective(frust, (float)windowWidth/(float)windowHeight, zNear, zFar);
			GLU.gluLookAt(gameHandle.camera.position.x, 
						  gameHandle.camera.position.y, 
						  gameHandle.camera.position.z, 
						  gameHandle.camera.target.x, 
						  gameHandle.camera.target.y, 
						  gameHandle.camera.target.z,
						  0, 1, 0);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			
		}
		else
			try {
				throw new Exception("Invalid graphics mode. Mode must be 2D or 3D.");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
	}

	public void setResolution(int requestW, int requestH) {
		windowWidth	 = requestW;
		windowHeight = requestH;
	}

	public void create(String gmode) {		
		try {
			Display.setDisplayMode(new DisplayMode( windowWidth + gameHandle.windowManager.gui.get("storyBox").width, windowHeight ));
			if (fullScreen) {
				for (DisplayMode dm : Display.getAvailableDisplayModes()) {
					if (dm.toString().contains(windowWidth + " x " + windowHeight + " x " + 32)) {
						Display.setDisplayMode(dm);
						Display.setFullscreen(true);
						break;
					}
				}
			}
			
			Display.setTitle(title);
			Display.setVSyncEnabled(vSync);
			
			if (MSAA > 0) {
				PixelFormat format = new PixelFormat(32, 0, 24, 8, MSAA);
				Pbuffer pb = new Pbuffer(windowWidth, windowHeight, format, null);
				pb.makeCurrent();
				Display.create(format);
			}
			else {
				Display.create();
			}
			
			setGraphicsMode(gmode);

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}		
	}

	/**
	 * Draws the current scene. First sets up the texture parameters to ensure proper rendering/
	 * blending of adjacent textures. Next, the scene is translated to the middle of the map, then
	 * to the position of the camera. Finally, each elevation level in the current GameMap is rendered.
	 */
	public void drawGame() {
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(windowWidth/2, windowHeight/2, 0);
		GL11.glTranslatef(-gameHandle.camera.position.x, 
						  -gameHandle.camera.position.y, 
						  -gameHandle.camera.position.z);
		
		if (gameHandle.playerFocusEntity.mapLevel > 0)
			drawBackground();
		
		drawCurrentLevel();
		
		if (gameHandle.playerFocusEntity.mapLevel + 1 < gameHandle.gameMap.mapCanvas.size())
			drawForeground();

		GL11.glPopMatrix();
	}
	
	/**
	 * Draws the current elevation level of the map (that is, the level that the playerFocusEntity
	 * currently sits at). All entities also at this level will be drawn.
	 */
	public void drawCurrentLevel() {
		int playerLevel = gameHandle.playerFocusEntity.mapLevel;
		gameHandle.shaderManager.bind();
		
		for (GameEntity ge : gameHandle.gameEntities) {
			if (ge.mapLevel == playerLevel && ge.lightRadius > 0) {
				gameHandle.shaderManager.parmData.put((windowWidth/2) + ge.position.x - gameHandle.camera.position.x + ge.width/2);
				gameHandle.shaderManager.parmData.put((windowHeight/2) + ge.position.y - gameHandle.camera.position.y + ge.height/2);
				gameHandle.shaderManager.parmData.put(ge.position.z);
				gameHandle.shaderManager.parmData.put(ge.lightRadius);
			}
		}

		gameHandle.shaderManager.persistParmData(playerLevel);
				
		gameHandle.gameMap.drawLevel(playerLevel);
		
		gameHandle.shaderManager.parmData.clear();
		gameHandle.shaderManager.unbind();
		
		for (GameEntity ge : gameHandle.gameEntities) {
			if (ge.mapLevel == playerLevel) ge.draw();
		}
		
	}
	
	/**
	 * Draws all elevation levels that are *above* the level that the playerFocusEntity occupies. This
	 * level will be slightly translated by a factor of its relative distance from the playerFocusEntity's
	 * level. All entities also at this level will be drawn.
	 */
	public void drawForeground() {

		for (int i = gameHandle.playerFocusEntity.mapLevel + 1; i < gameHandle.gameMap.mapCanvas.size(); i++) {
			GL11.glPushMatrix();
			GL11.glTranslatef(-gameHandle.camera.position.x * i * .1f, 
					  	 	  -gameHandle.camera.position.y * i * .1f, 
					  	 	  -gameHandle.camera.position.z * i * .1f);
			
			gameHandle.shaderManager.bind();
			gameHandle.shaderManager.persistParmData(i);
			gameHandle.gameMap.drawLevel(i);
			gameHandle.shaderManager.parmData.clear();
			gameHandle.shaderManager.unbind();
			
			for (GameEntity ge : gameHandle.gameEntities) {
				if (ge.mapLevel == i) ge.draw();
			}
			
			GL11.glPopMatrix();
		}
	}
	
	/**
	 * Draws all elevation levels that are *below* the level that the playerFocusEntity occupies. This
	 * level will be slightly translated (inversely relative to the foreground) by a factor of its relative
	 * distance from the playerFocusEntity's level. All entities also at this level will be drawn.
	 */
	public void drawBackground() {
		int playerLevel = gameHandle.playerFocusEntity.mapLevel;

		for (int i = 0; i < playerLevel; i++) {
			GL11.glPushMatrix();
			GL11.glTranslatef(gameHandle.camera.position.x * (playerLevel - i) * .1f, 
					  	 	  gameHandle.camera.position.y * (playerLevel - i) * .1f, 
					  	 	  gameHandle.camera.position.z * (playerLevel - i) * .1f);
			
			gameHandle.shaderManager.bind();
			gameHandle.shaderManager.persistParmData(i);
			gameHandle.gameMap.drawLevel(i);
			gameHandle.shaderManager.parmData.clear();
			gameHandle.shaderManager.unbind();
			
			for (GameEntity ge : gameHandle.gameEntities) {
				if (ge.mapLevel == i) ge.draw();
			}
			
			GL11.glPopMatrix();
		}
	}
	
	public void fadeToBlack() {
		long currentTime = System.currentTimeMillis();
		
		do {

			lastCheck = System.currentTimeMillis();

			GL11.glColor4f(0f, 0f, 0f, (float)((lastCheck - currentTime) / fadeDuration));
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(gameHandle.graphicsManager.getWidth(), 0);
				GL11.glVertex2f(gameHandle.graphicsManager.getWidth(), gameHandle.graphicsManager.getHeight());
				GL11.glVertex2f(0, gameHandle.graphicsManager.getHeight());
			GL11.glEnd();
			
			Display.update();
			Display.sync(60);
			
		} while (lastCheck - currentTime < fadeDuration);
	}
	
	public void fadeIn() {
		long currentTime = System.currentTimeMillis();
		
		do	{
			lastCheck = System.currentTimeMillis();

			GL11.glColor4f(0f, 0f, 0f, 1 - (float)((lastCheck - currentTime) / fadeDuration));
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(gameHandle.graphicsManager.getWidth(), 0);
				GL11.glVertex2f(gameHandle.graphicsManager.getWidth(), gameHandle.graphicsManager.getHeight());
				GL11.glVertex2f(0, gameHandle.graphicsManager.getHeight());
			GL11.glEnd();
			
			Display.update();
			Display.sync(60);
			
		} while (lastCheck - currentTime < fadeDuration);
	}
	//------------------ Getters/Setters ------------------//
	
	public void setFullscreen(boolean b) { fullScreen = b; }
	public void setVSync(boolean b) 	 { vSync = b; }
	public void setMSAA(int i) 			 { MSAA = i; }
	public void setTitle(String t)     	 { title = t;}
	public void setZNear(float z) 		 { zNear = z; }
	public void setZFar(float z) 		 { zFar = z; }
	public void setFrust(float f) 	  	 { frust = f; }
	
	public int getWidth() 				 { return windowWidth; }
	public int getHeight()  			 { return windowHeight; }
}
