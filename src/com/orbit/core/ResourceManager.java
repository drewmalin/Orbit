package com.orbit.core;

public class ResourceManager {
	
	private Game gameHandle;
	
	public ResourceManager(Game g) {
		gameHandle = g;
	}

	public GameEntity loadEntity(String file) {
		XMLParser entityFile = new XMLParser(file);
		
		GameEntity entity = new GameEntity();
		
		for (Node entityEl : entityFile.root.children) {
			for (Node el : entityEl.children) {
				if (el.name.equals("position"))
					entity.setPosition(el.readFloatArray());
				else if (el.name.equals("width"))
					entity.setWidth(el.readInt());
				else if (el.name.equals("height"))
					entity.setHeight(el.readInt());
			}
		}
		
		return entity;
	}

	public GameMap loadMap(String file) {

		XMLParser mapFile = new XMLParser(file);
		
		GameMap map = new GameMap();
		
		for (Node mapEl : mapFile.root.children) {
			for (Node el : mapEl.children) {
				if (el.name.equals("width"))
					map.setWidth(el.readInt());
				else if (el.name.equals("height"))
					map.setHeight(el.readInt());
				else if (el.name.equals("spacing"))
					map.setSpacing(el.readInt());
			}
		}
		return map;
	}
	
	
}
