package com.orbit.core;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Game {
	
	public InputManager inputManager;
	public GraphicsManager graphicsManager;
	public ResourceManager resourceManager;
	public NetworkManager networkManager;
	
	public Game() {
		inputManager 	= new InputManager();
		graphicsManager = new GraphicsManager();
		resourceManager = new ResourceManager();
		networkManager 	= new NetworkManager();
	}
	
	public void initGame() {

		inputManager.setKeyboardListener(keyboardListener);
		inputManager.setMouseListener(mouseListener);
		
		graphicsManager.setResolution(800, 600);
		graphicsManager.setFrust(60f);
		graphicsManager.setZNear(0.1f);
		graphicsManager.setZFar(100f);
		graphicsManager.create("2D");
		
		networkManager.setServerURL("http://lezendstudios.net/OrbitServer");
		networkManager.connect();
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
	
	public void start() {
		
		initGame();
		
		while (!Display.isCloseRequested()) {
			
			inputManager.pollKeyboard();
			inputManager.pollMouse();
			Display.update();
		}
		
		Display.destroy();
	}
}
