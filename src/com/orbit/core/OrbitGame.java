package com.orbit.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class OrbitGame extends Game {
	
	//Temp
	public boolean multiplayer = true;
	
	public static void main(String[] args) {
		OrbitGame game = new OrbitGame();
		game.start();
	}
	
	@Override
	public void initGame() {
		inputManager.setKeyboardListener(keyboardListener);
		inputManager.setMouseListener(mouseListener);
		
		addTriggers();
		
		graphicsManager.setResolution(800, 600);
		graphicsManager.create("2D");
		
		camera.setLocation(new Vector3f(0, 0, 0));
		
		GameEntity player = new GameEntity(resourceManager.loadEntity("res/Player.xml"));
		addEntity(player);
		setFocusEntity(player);
		
		if (multiplayer) {
			networkManager.setServerURL("http://lezendstudios.net/OrbitServer", 1337);
			networkManager.setPutService(putWebService);
			networkManager.setGetService(getWebService);
			networkManager.connect();
		}
		else {
			GameMap map = new GameMap(resourceManager.loadMap("res/Level1.xml"));
			setLevel(map);
		}
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
					GameMap map = new GameMap(resourceManager.loadMap(p.file));
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
		public void onEvent(int key) {
			if (key == Keyboard.KEY_ESCAPE) {
				System.exit(0);
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
	
	private void addTriggers() {
		inputManager.addKeyTrigger(Keyboard.KEY_W, new KeyTrigger() {
			public void onEvent() {
				camera.translateY(1);
			}
		});
		inputManager.addKeyTrigger(Keyboard.KEY_A, new KeyTrigger() {
			public void onEvent() {
				camera.translateX(1);
			}
		});
		inputManager.addKeyTrigger(Keyboard.KEY_S, new KeyTrigger() {
			public void onEvent() {
				camera.translateY(-1);
			}
		});
		inputManager.addKeyTrigger(Keyboard.KEY_D, new KeyTrigger() {
			public void onEvent() {
				camera.translateX(-1);
			}
		});
	}
}