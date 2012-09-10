package com.orbit.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class OrbitGame extends Game {
		
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
				
		GameEntity player = resourceManager.loadEntity("res/Player.xml");
		addEntity(player);
		setFocusEntity(player);
		
		try {
			textureManager.loadCycle(playerFocusEntity, playerFocusEntity.getAnimationFile());
			player.setTexture(textureManager.setFrame(playerFocusEntity.id, "idle_south"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (multiplayer) {
			networkManager.setServerURL("http://lezendstudios.net/OrbitServer", 1337);
			networkManager.setPutService(putWebService);
			networkManager.setGetService(getWebService);
			networkManager.connect();
		}
		else {
			GameMap map = resourceManager.loadMap("res/Level1.xml");
			setLevel(map);
		}
		
		int startX = (currentLevel.mapWidth * currentLevel.tileDimensions)/2;
		int startY = (currentLevel.mapHeight * currentLevel.tileDimensions)/2;

		playerFocusEntity.setPosition(new float[]{startX, startY, 0});
		camera.setLocation(new Vector3f(startX, startY, 0));
	}

	private WebService putWebService = new WebService() {	
		public void work(Packet p) {
			Packet newPacket = resourceManager.packetify(playerFocusEntity);
			networkManager.send(newPacket);
		}
	};
	
	private WebService getWebService = new WebService() {
		public void work(Packet p) {
			if (p.code == networkManager.ADD_ENTITY) {
				if (p.type.equals("MAP")) {
					GameMap map = resourceManager.loadMap(p.file);
					setLevel(map);
				}
				else {
					GameEntity ge = new GameEntity();
					addEntity(ge);
					resourceManager.updateFromPacket(ge, p);
				}
			}
			else if (p.code == networkManager.UPDATE_ENTITY) {
				resourceManager.updateFromPacket(gameEntities.get(p.id), p);
			}
			else if (p.code == networkManager.DROP_ENTITY) {
				gameEntities.remove(p.id);
			}
		}
	};
	
	private InputListener keyboardListener = new InputListener() {
		public void onEvent() {
			
			boolean lockNS = playerFocusEntity.cameraLockNS(currentLevel, graphicsManager);
			boolean lockEW = playerFocusEntity.cameraLockEW(currentLevel, graphicsManager);
			
			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) 
						System.exit(0);
					else if (Keyboard.getEventKey() == Keyboard.KEY_0) { 
						//**Temporary: testing swapping game levels **//
						GameMap map = resourceManager.loadMap("res/Level2.xml");
						setLevel(map);
						int startX = (currentLevel.mapWidth * currentLevel.tileDimensions)/2;
						int startY = (currentLevel.mapHeight * currentLevel.tileDimensions)/2;

						playerFocusEntity.setPosition(new float[]{startX, startY, 0});
						camera.setLocation(new Vector3f(startX, startY, 0));
					}
				}
				
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
				if (lockNS) camera.translateY(-1 * diagonal);
				if (lockEW) camera.translateX(-1 * diagonal);
				playerFocusEntity.moveY(-1 * diagonal, currentLevel);
				playerFocusEntity.moveX(-1 * diagonal, currentLevel);
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_D)) {
				if (lockNS) camera.translateY(-1 * diagonal);
				if (lockEW) camera.translateX(1 * diagonal);
				playerFocusEntity.moveY(-1 * diagonal, currentLevel);
				playerFocusEntity.moveX(1 * diagonal, currentLevel);
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
				if (lockNS) camera.translateY(1 * diagonal);
				if (lockEW) camera.translateX(-1 * diagonal);
				playerFocusEntity.moveY(1 * diagonal, currentLevel);
				playerFocusEntity.moveX(-1 * diagonal, currentLevel);
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S) && Keyboard.isKeyDown(Keyboard.KEY_D)) {
				if (lockNS) camera.translateY(1 * diagonal);
				if (lockEW) camera.translateX(1 * diagonal);
				playerFocusEntity.moveY(1 * diagonal, currentLevel);
				playerFocusEntity.moveX(1 * diagonal, currentLevel);
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				if (lockNS) camera.translateY(-1);
				playerFocusEntity.moveY(-1, currentLevel);
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_north"));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				if (lockEW) camera.translateX(-1);
				playerFocusEntity.moveX(-1, currentLevel);
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_west"));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				if (lockNS) camera.translateY(1);
				playerFocusEntity.moveY(1, currentLevel);
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_south"));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				if (lockEW) camera.translateX(1);
				playerFocusEntity.moveX(1, currentLevel);
				playerFocusEntity.setTexture(textureManager.nextFrame(playerFocusEntity.id, "walk_east"));
			}
			else {
				String last = textureManager.lastAnimation(playerFocusEntity.id);
				String idleAnim = "idle_south";
				
				if (last.contains("south"))
					idleAnim = "idle_south";
				else if (last.contains("north"))
					idleAnim = "idle_north";
				else if (last.contains("east"))
					idleAnim = "idle_east";
				else if (last.contains("west"))
					idleAnim = "idle_west";
				
				playerFocusEntity.setTexture(textureManager.setFrame(playerFocusEntity.id, idleAnim));
			}
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