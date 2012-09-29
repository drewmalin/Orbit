package com.orbit.core;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

public class ResourceManager {
	
	private Game gameHandle;
	
	public ResourceManager(Game g) {
		gameHandle = g;
	}

	/**
	 * Loads from an external resource file a new GameEntity object for use
	 * within the game. This method will parse the given XML file and instantiate
	 * a new GameEntity object, returning it to the calling context.
	 * @param file
	 * @return The newly created GameEntity
	 */
	public GameEntity loadEntity(String file) {

		XMLParser entityFile = new XMLParser(file);
		
		GameEntity entity = new GameEntity(gameHandle);
		entity.setFile(file);
		
		for (Node entityEl : entityFile.root.children) {
			for (Node el : entityEl.children) {
				if (el.name.equals("position"))
					entity.setPosition(el.readFloatArray());
				else if (el.name.equals("width"))
					entity.setWidth(el.readInt());
				else if (el.name.equals("height"))
					entity.setHeight(el.readInt());
				else if (el.name.equals("mass"))
					entity.setMass(el.readFloat());
				else if (el.name.equals("animationFile")) {
					entity.setAnimationFile(el.readString());
				}
				else if (el.name.equals("scriptFile")) {
					entity.setScriptFile(el.readString());
				}
			}
		}

		return entity;
	}

	/**
	 * Loads from an external file a new GameMap object for use within the
	 * game. A GameMap is the primary object which house each MapCanvas object
	 * (that is, each elevation level in the map) and in turn, each MapTile object
	 * (that is, each unique texture block).
	 * @param file
	 * @return The newly created GameMap
	 */
	public GameMap loadMap(String file) {

		XMLParser mapFile = new XMLParser(file);
		
		GameMap map = new GameMap();
		
		for (Node mapEl : mapFile.root.children) {
			for (Node el : mapEl.children) {
				if (el.name.equals("tileDimensions"))
					map.setTileDimension(el.readInt());
				else if (el.name.equals("lightLevel"))
					map.setLightLevel(el.readFloat());
				else if (el.name.equals("tile")) {
					MapTile mt = new MapTile();
					for (Node tileChild : el.children) {
						if (tileChild.name.equals("id"))
							mt.id = tileChild.readInt();
						else if (tileChild.name.equals("file"))
							mt.loadTexture(tileChild.readString());
						else if (tileChild.name.equals("collidable"))
							mt.collidable = tileChild.readBoolean();
					}
					mt.width = mt.height = map.tileDimensions;
					map.tiles.put(mt.id, mt);
				}
				else if (el.name.equals("canvas"))
					map.parseCanvas(el.data);
				else if (el.name.equals("entity")) {

					GameEntity entity = null;

					for (Node entityEl : el.children) {
						if (entityEl.name.equals("file")) {
							entity = loadEntity(entityEl.readString());
						}
						else if (entityEl.name.equals("position"))
							entity.setPosition(entityEl.readFloatArray());
					}
					gameHandle.addEntity(entity);
				}
			}
		}
		return map;
	}
}
