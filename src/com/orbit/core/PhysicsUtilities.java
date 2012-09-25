package com.orbit.core;

public class PhysicsUtilities {
	
	/**
	 * Determines whether or not the GameEntity (ge) will be pushed off of the
	 * GameMap (gm) if the y-translation value (delta) is applied to it.
	 * @param ge
	 * @param delta
	 * @param gm
	 * @return False if the y-delta value pushes the entity off of the GameMap,
	 * True otherwise.
	 */
	static boolean onMapY(GameEntity ge, float delta, GameMap gm) {
		if (ge.position.y + delta > 0 &&
				ge.position.y + delta < (gm.mapCanvas.get(ge.mapLevel).mapHeight - 1) * gm.tileDimensions)
			return true;
		return false;
	}
	
	/**
	 * Determines whether or not the GameEntity (ge) will be pushed off of the
	 * GameMap (gm) if the x-translation value (delta) is applied to it.
	 * @param ge
	 * @param delta
	 * @param gm
	 * @return False if the x-delta value pushes the entity off of the GameMap,
	 * True otherwise.
	 */
	static boolean onMapX(GameEntity ge, float delta, GameMap gm) {
		if (ge.position.x + delta > 0 &&
				ge.position.x + delta < (gm.mapCanvas.get(ge.mapLevel).mapWidth - 1) * gm.tileDimensions)
			return true;
		return false;
	}
	
	/**
	 * Determines whether or not the translation values (deltaX and deltaY) will push the
	 * GameEntity (ge) on top of a collidable MapTile on the current GameMap (gm). 
	 * @param ge
	 * @param deltaX
	 * @param deltaY
	 * @param gm
	 * @return True if the GameEntity overlaps a collidable MapTile, False otherwise.
	 */
	static boolean tileCollision(GameEntity ge, float deltaX, float deltaY, GameMap gm) {
		
		int topLeftX = (int) ((ge.position.x  + deltaX) / gm.tileDimensions);
		int topLeftY = (int) ((ge.position.y  + deltaY) / gm.tileDimensions);
		
		int topRightX = (int) (((ge.position.x + ge.width  + deltaX)) / gm.tileDimensions);
		int topRightY = topLeftY;
		
		int botLeftX = topLeftX;
		int botLeftY = (int) (((ge.position.y + ge.height + deltaY) / gm.tileDimensions));
		
		int botRightX = topRightX;
		int botRightY = botLeftY;
		
		for (int i = 0; i < gm.mapCanvas.get(ge.mapLevel).mapData[topLeftY][topLeftX].length; i++) {
			if (gm.tiles.get(gm.mapCanvas.get(ge.mapLevel).mapData[topLeftY][topLeftX][i]).collidable)
				return true;
		}
		
		for (int i = 0; i < gm.mapCanvas.get(ge.mapLevel).mapData[topRightY][topRightX].length; i++) {
			if (gm.tiles.get(gm.mapCanvas.get(ge.mapLevel).mapData[topRightY][topRightX][i]).collidable)
				return true;
		}
		
		for (int i = 0; i < gm.mapCanvas.get(ge.mapLevel).mapData[botLeftY][botLeftX].length; i++) {
			if (gm.tiles.get(gm.mapCanvas.get(ge.mapLevel).mapData[botLeftY][botLeftX][i]).collidable)
				return true;
		}
		
		for (int i = 0; i < gm.mapCanvas.get(ge.mapLevel).mapData[botRightY][botRightX].length; i++) {
			if (gm.tiles.get(gm.mapCanvas.get(ge.mapLevel).mapData[botRightY][botRightX][i]).collidable)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Determines whether or not the translation values (deltaX and deltaY) will push the
	 * current GameEntity (ge1) into the target GameEntity (ge2).
	 * @param e1
	 * @param deltaX
	 * @param deltaY
	 * @param e2
	 * @return True if the delta values cause ge1 to overlap ge2, False otherwise
	 */
	static boolean entityCollision(GameEntity e1, float deltaX, float deltaY, GameEntity e2) {
		
		if (e1.id == e2.id) return false;
		
		float e1MinY = e1.position.y, e1MinX = e1.position.x;
		float e1MaxY = e1.position.y + e1.height, e1MaxX = e1.position.x + e1.width;
		
		float e2MinY = e2.position.y, e2MinX = e2.position.x;
		float e2MaxY = e2.position.y + e2.height, e2MaxX = e2.position.x + e2.width;

		if ((e1MinY + deltaY >= e2MinY && e1MinY + deltaY <= e2MaxY) || 
			(e1MaxY + deltaY >= e2MinY && e1MaxY + deltaY <= e2MaxY)) {
				if ((e1MinX + deltaX >= e2MinX && e1MinX + deltaX <= e2MaxX) ||
					(e1MaxX + deltaX >= e2MinX && e1MaxX + deltaX <= e2MaxX)) {
					//Collision!
					return true;
				}
		}
		return false;
	}
}
