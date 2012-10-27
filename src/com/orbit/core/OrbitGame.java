package com.orbit.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.orbit.ui.MessageBox;
import com.orbit.ui.Window;

public class OrbitGame extends Game {
		
	boolean temp = false;
	
	public static void main(String[] args) {
		OrbitGame game = new OrbitGame();
		game.start();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.orbit.core.Game#initGame()
	 * Initialization of the game. Setup here includes new keyboard and mouse listeners,
	 * key triggers, the player entity, the camera, the first level, and graphics.
	 */
	@Override
	public void initGame() {
				
		// Load input listeners
		inputManager.setKeyboardListener(keyboardListener);
		inputManager.setMouseListener(mouseListener);
		
		
		
		// Create constant GUI
		
		Window w = windowManager.createWindow("storyBox", 800, 0).setWidth(350).setHeight(600);
		w.setColor(new float[] {0, 0, 0, 1});
		MessageBox mb = new MessageBox(windowManager, w);

		mb.message = "Mr. and Mrs. Dursley, of number four, Privet Drive, were proud to say that they were perfectly normal, thank you very much. They were the last people you'd expect to be involved in anything strange or mysterious, because they just didn't hold with such nonsense.\n\nMr. Dursley was the director of a firm called Grunnings, which made drills. He was a big, beefy man with hardly any neck, although he did have a very large mustache. Mrs. Dursley was thin and blonde and had nearly twice the usual amount of neck, which came in very useful as she spent so much of her time craning over garden fences, spying on the neighbors. The Dursleys had a small son called Dudley and in their opinion there was no finer boy anywhere.";
		mb.height = 600;
		mb.width = 350;
		mb.x = 800;
		mb.y = 0;
		mb.fontName = "Times New Roman";
		mb.fontSize = 16;
		mb.setFontColor("white");
		mb.setColor(new float[] {0, 0, 0, 1});
		
		// Load GraphicsManager
		graphicsManager.setResolution(800, 600);
		graphicsManager.create("2D");
		
		windowManager.gui.get("storyBox").messageBoxes.add(mb);
		mb.load();
		mb.fixMessage();
		
		// Load game menus
		windowManager.loadMenu("res/menu/pause.xml");
		windowManager.createClickListeners();
		
		// Load Shaders (must be done after loading of graphicsmanager)
		shaderManager.init("res/shaders/orbit.frag", "res/shaders/orbit.vert");
		
		// Load Main character
		GameEntity player = resourceManager.loadEntity("res/entities/Player.xml");
		addEntity(player);
		setFocusEntity(player);
		
		try {
			textureManager.loadCycle(playerFocusEntity, playerFocusEntity.getAnimationFile());
			player.setTexture(textureManager.setFrame(playerFocusEntity.id, "idle_south"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load first map
		GameMap map = resourceManager.loadMap("res/maps/Level1.xml");
		setLevel(map);
		camera.findPlayer();
	}
	
	private InputListener keyboardListener = new InputListener() {
		public void onEvent() {
						
			float mult = 1;
			float playerDeltaX = 0, camDeltaX = 0;
			float playerDeltaY = 0, camDeltaY = 0;
			boolean lockNS = playerFocusEntity.cameraLockNS(gameMap, graphicsManager);
			boolean lockEW = playerFocusEntity.cameraLockEW(gameMap, graphicsManager);
			
			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
						windowManager.pushMenuStack("pause");
				}
			}
			
			if (PhysicsUtilities.tileTest(playerFocusEntity, -2, gameMap)) {
				//System.out.println(playerFocusEntity.lastMovement);
				if (playerFocusEntity.lastMovement.x != 0) {
					playerDeltaX = 2 * playerFocusEntity.lastMovement.x;
					if (lockEW) camDeltaX = playerDeltaX;
					mult = playerFocusEntity.translateX(playerDeltaX);
					camera.translateX(camDeltaX * mult);
					return;
				}
				else if (playerFocusEntity.lastMovement.y != 0) {
				
					playerDeltaY = 2 * playerFocusEntity.lastMovement.y;
					if (lockNS) camDeltaY = playerDeltaY;
					mult = playerFocusEntity.translateY(playerDeltaY);
					camera.translateY(camDeltaY * mult);
					return;
				}
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
				playerDeltaY = -1 * diagonal;
				playerDeltaX = -1 * diagonal;
				if (lockNS) camDeltaY = playerDeltaY;
				if (lockEW) camDeltaX = playerDeltaX;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_D)) {
				playerDeltaY = -1 * diagonal;
				playerDeltaX =  1 * diagonal;
				if (lockNS) camDeltaY = playerDeltaY;
				if (lockEW) camDeltaX = playerDeltaX;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
				playerDeltaY =  1 * diagonal;
				playerDeltaX = -1 * diagonal;
				if (lockNS) camDeltaY = playerDeltaY;
				if (lockEW) camDeltaX = playerDeltaX;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_D)) {
				playerDeltaY = 1 * diagonal;
				playerDeltaX = 1 * diagonal;
				if (lockNS) camDeltaY = playerDeltaY;
				if (lockEW) camDeltaX = playerDeltaX;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				playerDeltaY = -1;
				if (lockNS) camDeltaY = playerDeltaY;
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_north"));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				playerDeltaX = -1;
				if (lockEW) camDeltaX = playerDeltaX;
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_west"));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				playerDeltaY = 1;
				if (lockNS) camDeltaY = playerDeltaY;
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_south"));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				playerDeltaX = 1;
				if (lockEW) camDeltaX = playerDeltaX;
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_east"));
			}
			else {
				playerFocusEntity.idle();
			}
			
			if (playerDeltaX != 0) mult = playerFocusEntity.translateX(playerDeltaX);
			if (playerDeltaY != 0) mult = playerFocusEntity.translateY(playerDeltaY);
			if (camDeltaX != 0) camera.translateX(camDeltaX * mult);
			if (camDeltaY != 0) camera.translateY(camDeltaY * mult);

		}
	};
	
	private InputListener mouseListener = new InputListener() {
		public void onEvent(int key, int x, int y) {
			if (key == 0) {
				System.out.println("Left click");
			}
			else if (key == 1) {
				System.out.println("Right click");
			}
		}
	};
}