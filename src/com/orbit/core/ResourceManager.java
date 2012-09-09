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
		
		GameEntity entity = new GameEntity();
		entity.setFile(file);
		
		for (Node entityEl : entityFile.root.children) {
			for (Node el : entityEl.children) {
				if (el.name.equals("position"))
					entity.setPosition(el.readFloatArray());
				else if (el.name.equals("width"))
					entity.setWidth(el.readInt());
				else if (el.name.equals("height"))
					entity.setHeight(el.readInt());
				else if (el.name.equals("animationFile"))
					entity.setAnimationFile(el.readString());
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
