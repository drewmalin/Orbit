package com.orbit.core;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

public class ResourceManager {
	
	private Game gameHandle;
	
	public ResourceManager(Game g) {
		gameHandle = g;
	}

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

	public GameMap loadMap(String file) {

		XMLParser mapFile = new XMLParser(file);
		
		GameMap map = new GameMap();
		
		for (Node mapEl : mapFile.root.children) {
			for (Node el : mapEl.children) {
				if (el.name.equals("tileDimensions"))
					map.setTileDimension(el.readInt());
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

	public void updateFromPacket(GameEntity gameEntity, Packet p) {
		gameEntity.setPosition(new float[] {p.x, p.y, p.z});
		gameEntity.setRotation(new float[] {p.rx, p.ry, p.rz});
		gameEntity.setHeight(p.height);
		gameEntity.setWidth(p.width);
	}

	public Packet packetify(GameEntity ge) {
		Packet p = new Packet();
		
		p.width = ge.getWidth();
		p.height = ge.getHeight();
		p.file = ge.getFile();
		
		Vector3f temp = ge.getPosition();
		p.x = temp.x;
		p.y = temp.y;
		p.z = temp.z;
		temp = ge.getRotation();
		p.rx = temp.x;
		p.ry = temp.y;
		p.rz = temp.z;
		
		p.id = ge.id;
		return p;		
	}
}
