package com.orbit.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

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
		
		multiplayer = false;
		
		inputManager.setKeyboardListener(keyboardListener);
		inputManager.setMouseListener(mouseListener);
		
		graphicsManager.setResolution(800, 600);
		graphicsManager.create("2D");
				
		GameEntity player = resourceManager.loadEntity("res/entities/Player.xml");
		addEntity(player);
		setFocusEntity(player);
		
		try {
			textureManager.loadCycle(playerFocusEntity, playerFocusEntity.getAnimationFile());
			player.setTexture(textureManager.setFrame(playerFocusEntity.id, "idle_south"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		GameMap map = resourceManager.loadMap("res/maps/Level1.xml");
		setLevel(map);
		
		int startX = (currentLevel.mapWidth * currentLevel.tileDimensions)/2;
		int startY = (currentLevel.mapHeight * currentLevel.tileDimensions)/2;

		playerFocusEntity.setPosition(new float[]{startX, startY, 0});
		camera.setLocation(new Vector3f(startX, startY, 0));
		
	}
	
	private InputListener keyboardListener = new InputListener() {
		public void onEvent() {
			
			float playerDeltaX = 0, camDeltaX = 0;
			float playerDeltaY = 0, camDeltaY = 0;
			boolean lockNS = playerFocusEntity.cameraLockNS(currentLevel, graphicsManager);
			boolean lockEW = playerFocusEntity.cameraLockEW(currentLevel, graphicsManager);
			
			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) 
						System.exit(0);
					else if (Keyboard.getEventKey() == Keyboard.KEY_0) { 
						//**Temporary: testing swapping game levels **//
						changeLevel("Level2.xml");
					}
				}
				
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
				playerDeltaY = -1 * diagonal;
				playerDeltaX = -1 * diagonal;
				if (lockNS) camDeltaY = playerDeltaY;
				if (lockEW) camDeltaX = playerDeltaX;
				/*
				if (lockNS) camera.translateY(-1 * diagonal);
				if (lockEW) camera.translateX(-1 * diagonal);
				playerFocusEntity.moveY(-1 * diagonal, currentLevel);
				playerFocusEntity.moveX(-1 * diagonal, currentLevel);
				*/
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_D)) {
				playerDeltaY = -1 * diagonal;
				playerDeltaX =  1 * diagonal;
				if (lockNS) camDeltaY = playerDeltaY;
				if (lockEW) camDeltaX = playerDeltaX;
				/*
				if (lockNS) camera.translateY(-1 * diagonal);
				if (lockEW) camera.translateX(1 * diagonal);
				playerFocusEntity.moveY(-1 * diagonal, currentLevel);
				playerFocusEntity.moveX(1 * diagonal, currentLevel);
				*/
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
				playerDeltaY =  1 * diagonal;
				playerDeltaX = -1 * diagonal;
				if (lockNS) camDeltaY = playerDeltaY;
				if (lockEW) camDeltaX = playerDeltaX;
				/*
				if (lockNS) camera.translateY(1 * diagonal);
				if (lockEW) camera.translateX(-1 * diagonal);
				playerFocusEntity.moveY(1 * diagonal, currentLevel);
				playerFocusEntity.moveX(-1 * diagonal, currentLevel);
				*/
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_D)) {
				playerDeltaY = 1 * diagonal;
				playerDeltaX = 1 * diagonal;
				if (lockNS) camDeltaY = playerDeltaY;
				if (lockEW) camDeltaX = playerDeltaX;
				/*
				if (lockNS) camera.translateY(1 * diagonal);
				if (lockEW) camera.translateX(1 * diagonal);
				playerFocusEntity.moveY(1 * diagonal, currentLevel);
				playerFocusEntity.moveX(1 * diagonal, currentLevel);
				*/
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				playerDeltaY = -1;
				if (lockNS) camDeltaY = playerDeltaY;
				/*
				if (lockNS) camera.translateY(-1);
				playerFocusEntity.moveY(-1, currentLevel);
				*/
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_north"));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				playerDeltaX = -1;
				if (lockEW) camDeltaX = playerDeltaX;
				/*
				if (lockEW) camera.translateX(-1);
				playerFocusEntity.moveX(-1, currentLevel);
				*/
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_west"));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				playerDeltaY = 1;
				if (lockNS) camDeltaY = playerDeltaY;
				/*
				if (lockNS) camera.translateY(1);
				playerFocusEntity.moveY(1, currentLevel);
				*/
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_south"));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				playerDeltaX = 1;
				if (lockEW) camDeltaX = playerDeltaX;
				/*
				if (lockEW) camera.translateX(1);
				playerFocusEntity.moveX(1, currentLevel);
				*/
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_east"));
			}
			else {
				playerFocusEntity.idle();
			}
			
			float mult = 1;
			if (playerDeltaX != 0) mult = playerFocusEntity.moveX(playerDeltaX);
			if (playerDeltaY != 0) mult = playerFocusEntity.moveY(playerDeltaY);
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