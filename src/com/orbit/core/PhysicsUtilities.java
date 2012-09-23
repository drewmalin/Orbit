package com.orbit.core;

public class PhysicsUtilities {
	
	static boolean onMapY(GameEntity ge, float delta, GameMap gm) {
		if (ge.position.y + delta > 0 &&
				ge.position.y + delta < (gm.mapHeight - 1) * gm.tileDimensions)
			return true;
		return false;
	}
	
	static boolean onMapX(GameEntity ge, float delta, GameMap gm) {
		if (ge.position.x + delta > 0 &&
				ge.position.x + delta < (gm.mapWidth - 1) * gm.tileDimensions)
			return true;
		return false;
	}
	
	static boolean collision(GameEntity e1, float deltaX, float deltaY, GameEntity e2) {
		
		if (e1.id == e2.id) return false;
		
		float e1MinY = e1.position.y, e1MinX = e1.position.x;
		float e1MaxY = e1.position.y + e1.height, e1MaxX = e1.position.x + e1.width;
		
		float e2MinY = e2.position.y, e2MinX = e2.position.x;
		float e2MaxY = e2.position.y + e2.height, e2MaxX = e2.position.x + e2.width;

		if (e1.id != 1 && e2.id != 1) System.out.println(e1MinX);

		
		if ((e1MinY + deltaY >= e2MinY && e1MinY + deltaY <= e2MaxY) || 
			(e1MaxY + deltaY >= e2MinY && e1MaxY + deltaY <= e2MaxY)) {
				if ((e1MinX + deltaX >= e2MinX && e1MinX + deltaX <= e2MaxX) ||
					(e1MaxX + deltaX >= e2MinX && e1MaxX + deltaX <= e2MaxX)) {
					//Collision!
					if (e1.id != 1) System.out.println(e1.id + " " + e2.id);
					return true;
				}
		}
		return false;
	}
}
