package com.orbit.core;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Game {
	
	//------  Component Managers  -------//
	public InputManager inputManager;
	public GraphicsManager graphicsManager;
	public ResourceManager resourceManager;
	public NetworkManager networkManager;
	//-----------------------------------//
	
	public Camera camera;
	public ArrayList<GameEntity> gameEntities;
	public GameEntity playerFocusEntity;
	public GameMap currentLevel;
	
	public Game() {
		
		inputManager 	= new InputManager(this);
		graphicsManager = new GraphicsManager(this);
		resourceManager = new ResourceManager(this);
		networkManager 	= new NetworkManager(this);
		camera			= new Camera(this);
		gameEntities 	= new ArrayList<GameEntity>();
	}
	
	public void initGame() {

		inputManager.setKeyboardListener(keyboardListener);
		inputManager.setMouseListener(mouseListener);
		
		graphicsManager.setResolution(800, 600);
		graphicsManager.setFrust(60f);
		graphicsManager.setZNear(0.1f);
		graphicsManager.setZFar(100f);
		graphicsManager.create("3D");
		
		camera.setLocation(new Vector3f(0f, 0f, 0f));
		camera.setTarget(new Vector3f(0f, 0f, 0f));
		
	}

	
	private InputListener keyboardListener = new InputListener() {
		public void onEvent(int key) {
			if (key == Keyboard.KEY_W) {
				System.out.println("Pressed 'W'!");
			}
			else if (key == Keyboard.KEY_A) {
				System.out.println("Pressed 'A'!");
			}
			else if (key == Keyboard.KEY_S) {
				System.out.println("Pressed 'S'!");
			}
			else if (key == Keyboard.KEY_D) {
				System.out.println("Pressed 'D'!");
			}
			else if (key == Keyboard.KEY_ESCAPE) {
				System.exit(0);
			}
		}
	};
	
	private InputListener mouseListener = new InputListener() {
		public void onEvent(int key, int x, int y) {
			if (key == 0) {
				System.out.println("Left click!");
			}
			else if (key == 1) {
				System.out.println("Right click!");
			}
		}
	};
	
	public void setFocusEntity(GameEntity ge) {
		playerFocusEntity = ge;
	}
	
	public void setLevel(GameMap map) {
		currentLevel = map;
	}
	
	public void start() {
		
		initGame();
		
		while (!Display.isCloseRequested()) {
			
			inputManager.pollKeyboard();
			inputManager.pollMouse();
			
			graphicsManager.drawGame();
			
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
}
