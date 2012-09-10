package com.orbit.core;

import java.util.HashMap;

public class GameMap {

	public int tileDimensions;
	public int mapWidth;
	public int mapHeight;
	
	public HashMap<Integer, MapTile> tiles;
	public int[][][] mapData;

	public GameMap() {
		tiles = new HashMap<Integer, MapTile>();
	}
	
	public void parseCanvas(String data) {
		String[] lines = data.split("\n");
		int lineIdx = 0, tileIdx, stackIdx;
		mapData = new int[lines.length][][];
		
		// For each map row...
		for (String line : lines) {
			tileIdx = 0;
			String[] tiles = line.split(";");			
			int tileData[][] = new int[tiles.length][];
			
			// For each row tile...
			for (String tile : tiles) {
				stackIdx = 0;
				String[] components = tile.split(",");
				int tileStack[] = new int[components.length];
				
				// For each tile component...
				for (String component : components) {
					tileStack[stackIdx++] = Integer.parseInt(component);
				}				
				tileData[tileIdx++] = tileStack;
			}
			mapData[lineIdx++] = tileData;
		}
		setWidth();
		setHeight();
	}

	public void setTileDimension(int data) {
		tileDimensions = data;
	}
	
	private void setWidth() {
		int max = 0;
		for (int i = 0; i < mapData.length; i++) {
			if (mapData[i].length > max) max = mapData[i].length;
		}
		mapWidth = max;
	}
	
	private void setHeight() {
		mapHeight = mapData.length;
	}
}
