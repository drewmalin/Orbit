package com.orbit.core;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureManager {

	private Game gameHandle;
	private HashMap<Integer, TextureCycle> textureStore;
	
	public TextureManager(Game game) {
		gameHandle = game;
		textureStore = new HashMap<Integer, TextureCycle>();
	}

	public void loadCycle(GameEntity ge, String file) throws Exception {

		XMLParser textureFile = new XMLParser(file);
		TextureCycle t = new TextureCycle();

		for (Node animation : textureFile.root.children) {

			ArrayList<Texture> textureList = new ArrayList<Texture>();

			for (Node text : animation.children) {
				if (text.name.equals("speed")) {
					t.speedNano = text.readInt();
				}
				else if (text.name.equals("frame")) {
					Texture texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(text.readString()));
					textureList.add(texture);
				}
			}
			
			t.cycle.put(animation.name, textureList);
		}
		
		if (ge.id == -1) ge.id = textureStore.size();
		textureStore.put(ge.id, t);
	}

	public Texture nextFrame(int id, String animation) {
		TextureCycle tc = textureStore.get(id);
		return tc.getNextFrame(animation);
	}
	
	public Texture setFrame(int id, String frame) {
		return textureStore.get(id).cycle.get(frame).get(0);
	}
	
	public String lastAnimation(int id) {
		return textureStore.get(id).lastAnimation;
	}
}
