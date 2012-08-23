package com.orbit.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
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
	
	private void setGraphicsMode(String string) {
		if (string.equals("2D")) {
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);								
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			
			GL11.glLoadIdentity();
			GL11.glOrtho(0, windowWidth, windowHeight, 0, -1, 1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		}
		
		else if (string.equals("3D")) {
			GL11.glDepthMask(true);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			
			GLU.gluPerspective(frust, (float)windowWidth/(float)windowHeight, zNear, zFar);
			GLU.gluLookAt(0, 0, 0, 0, 0, 0, 0, 1, 0);
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

	public void setFullscreen(boolean b) {
		fullScreen = b;
	}

	public void setVSync(boolean b) {
		vSync = b;
	}

	public void setMSAA(int i) {
		MSAA = i;
	}

	public void setTitle(String t) {
		title = t;
	}
	
	public void setZNear(float z) {
		zNear = z;
	}
	
	public void setZFar(float z) {
		zFar = z;
	}
	
	public void setFrust(float f) {
		frust = f;
	}
	
	public void create(String gmode) {		
		try {
			Display.setDisplayMode(new DisplayMode( windowWidth, windowHeight ));
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

}
