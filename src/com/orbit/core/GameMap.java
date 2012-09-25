package com.orbit.core;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {

	public int tileDimensions;
	public int mapWidth;
	public int mapHeight;
	
	public final ArrayList<MapCanvas> mapCanvas;
	public final HashMap<Integer, MapTile> tiles;

	/**
	 * GameMap
	 * The GameMap consists of an arraylist of MapCanvas objects. Each
	 * MapCanvas represents a separate elevation level in the current
	 * map. Additionally, the GameMap houses the list of unique tiles
	 * that make up the map. Each tile contains texture, position, and
	 * collision information.
	 */
	public GameMap() {
		mapCanvas = new ArrayList<MapCanvas>();
		
		tiles = new HashMap<Integer, MapTile>();
		
		MapTile invisibleTile = new MapTile();
		invisibleTile.id = -1;
		invisibleTile.collidable = true;
		
		tiles.put(-1, invisibleTile);
	}
	
	/**
	 * Takes an entire canvas string as a parameter and splits it into
	 * individual tiles sorted by relative x,y positions. Each canvas
	 * parsed is stored in the order given by the resource file and thus,
	 * the more canvas tags added to the resource file, the more maps
	 * will be stacked on top of the base layer.
	 * @param data
	 */
	public void parseCanvas(String data) {
		String[] lines = data.split("\n");
		int lineIdx = 0, tileIdx, stackIdx;
		MapCanvas mp = new MapCanvas(lines.length);

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
					tileStack[stackIdx++] = component.equals("-") ? -1 : Integer.parseInt(component);
				}				
				tileData[tileIdx++] = tileStack;
			}
			mp.mapData[lineIdx++] = tileData;
		}

		mp.setWidth();
		mp.setHeight();
		
		mapCanvas.add(mp);
	}

	/**
	 * @param data
	 * Save the width of each tile. This dimension is used for rendering
	 * the map as a whole.
	 */
	public void setTileDimension(int data) {
		tileDimensions = data;
	}

	/**
	 * @param level
	 * Draw the given elevation level of the GameMap. Walks to every individual
	 * tile in the current elevation level and draws the corresponding texture
	 * to the screen. 
	 */
	public void drawLevel(int level) {
		
		for (int row = 0; row < mapCanvas.get(level).mapData.length; row++) {
			for (int col = 0; col < mapCanvas.get(level).mapData[row].length; col++) {
				//Do not bother drawing invisible tiles
				if (mapCanvas.get(level).mapData[row][col][0] != -1) {
					for (int tile = 0; tile < mapCanvas.get(level).mapData[row][col].length; tile++) {
						tiles.get(mapCanvas.get(level).mapData[row][col][tile]).draw(col * tileDimensions, row * tileDimensions);
					}
				}
			}
		}
	}
	
}
