package com.orbit.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.orbit.ui.Button;
import com.orbit.ui.Canvas;
import com.orbit.ui.MessageBox;
import com.orbit.ui.Window;
import com.orbit.xml.Node;
import com.orbit.xml.XMLParser;

public final class WindowManager {
	
	public final Game gameHandle;
	
	public final HashMap<String, Window> gui;
	public final HashMap<String, Window> windows;
	public ArrayList<Window> windowStack;
	
	public WindowManager(Game gh) {
		gameHandle 	= gh;
		gui 		= new HashMap<String, Window>();
		windows 	= new HashMap<String, Window>();
		windowStack	= new ArrayList<Window>();
	}

	public Window createWindow(String name, int x, int y) {
		Window w = new Window(this, null);
		w.name = name;
		w.x = x;
		w.y = y;
		w.color[0] = w.color[1] = w.color[2] = w.color[3] = 1;
		
		gui.put(name, w);
		return w;
	}
	
	public void addToGui(String name) {
		gui.put(name, windows.get(name));
	}
	
	public void draw() {
		for (Window w : gui.values()) {
			w.draw();
		}
		for (Window w : windowStack) {
			w.draw();
		}
	}
	
	public void pushMenuStack(String name) {
		windowStack.add(windows.get(name));
	}
	
	public void popMenuStack() {
		if (windowStack.size() > 0)
			windowStack.remove(windowStack.size() - 1);
	}

	/** Load a menu xml file and store the resulting window, along with all of its message
	 * boxes, buttons, and attributes.
	 * 
	 * @param filename
	 */
	public void loadMenu(String filename) {
		XMLParser itemLib = new XMLParser(filename);

		for (Node windowEl : itemLib.root.children) {
			Window window = new Window(this, null);
			window.name = filename.substring(filename.lastIndexOf("/")+1, filename.indexOf("."));
				
			for (Node infoEl : windowEl.children) {
				if (infoEl.name.equals("x"))
					window.x = infoEl.readInt();
				else if (infoEl.name.equals("y"))
					window.y = infoEl.readInt();
				else if (infoEl.name.equals("width")) {
					if (infoEl.readString().equals("FULL"))
						window.width = gameHandle.graphicsManager.getWidth();
					else
						window.width = infoEl.readInt();
				}
				else if (infoEl.name.equals("height")) {
					if (infoEl.readString().equals("FULL"))
						window.height = gameHandle.graphicsManager.getHeight();
					else
						window.height = infoEl.readInt();
				}
				else if (infoEl.name.equals("backgroundImage"))
					window.setBackgroundImage(infoEl.readString());
				else if (infoEl.name.equals("backgroundColor"))
					window.setColor(infoEl.readFloatArray());
				else if (infoEl.name.equals("messageBox"))
					loadMessageBox(infoEl, window);
				else if (infoEl.name.equals("button")) {
					Button button = new Button(this, window);
				
					for (Node buttonEl : infoEl.children) {
						if (buttonEl.name.equals("name"))
							button.name = buttonEl.readString();
						else if (buttonEl.name.equals("x"))
							button.x = window.x + buttonEl.readInt();
						else if (buttonEl.name.equals("y"))
							button.y = window.y + buttonEl.readInt();
						else if (buttonEl.name.equals("width"))
							button.width = buttonEl.readInt();
						else if (buttonEl.name.equals("height"))
							button.height = buttonEl.readInt();
						else if (buttonEl.name.equals("messageBox"))
							loadMessageBox(buttonEl, button);
						else if (buttonEl.name.equals("onHover"))
							loadHoverBehavior(buttonEl, button);
						else if (buttonEl.name.equals("backgroundImage"))
							button.setBackgroundImage(buttonEl.readString());
						else if (buttonEl.name.equals("backgroundColor"))
							button.setColor(buttonEl.readFloatArray());
					}
					window.buttons.put(button.name, button);
				}
			}
			windows.put(window.name, window);
		}
	}
	
	private void loadMessageBox(Node xmlContext, Canvas canvas) {
		MessageBox mb = new MessageBox(this, canvas);
		for (Node mbEl : xmlContext.children) {
			if (mbEl.name.equals("x"))
				mb.x = canvas.x + mbEl.readInt();
			else if (mbEl.name.equals("y"))
				mb.y = canvas.y + mbEl.readInt();
			else if (mbEl.name.equals("width"))
				mb.width = mbEl.readInt();
			else if (mbEl.name.equals("height"))
				mb.height = mbEl.readInt();
			else if (mbEl.name.equals("fontName"))
				mb.fontName = mbEl.readString();
			else if (mbEl.name.equals("fontSize"))
				mb.fontSize = mbEl.readInt();
			else if (mbEl.name.equals("fontColor"))
				mb.setFontColor(mbEl.readString());
			else if (mbEl.name.equals("message"))
				mb.message = mbEl.readString();
			else if (mbEl.name.equals("backgroundImage"))
				mb.setBackgroundImage(mbEl.readString());
			else if (mbEl.name.equals("backgroundColor"))
				mb.setColor(mbEl.readFloatArray());
		}
		mb.load();
		canvas.messageBoxes.add(mb);
	}
	
	private void loadHoverBehavior(Node xmlContext, Button button) {
		
		for (Node hoverEl : xmlContext.children) {
			if (hoverEl.name.equals("backgroundColor"))
				button.setHovorColor(hoverEl.readFloatArray());
			else if (hoverEl.name.equals("messageBox")) {
				MessageBox mb = new MessageBox(this, button);
				for (Node mbEl : hoverEl.children) {
					if (mbEl.name.equals("width"))
						mb.width = mbEl.readInt();
					else if (mbEl.name.equals("height"))
						mb.height = mbEl.readInt();
					else if (mbEl.name.equals("fontName"))
						mb.fontName = mbEl.readString();
					else if (mbEl.name.equals("fontSize"))
						mb.fontSize = mbEl.readInt();
					else if (mbEl.name.equals("fontColor"))
						mb.setFontColor(mbEl.readString());
					else if (mbEl.name.equals("message"))
						mb.message = mbEl.readString();
					else if (mbEl.name.equals("backgroundImage"))
						mb.setBackgroundImage(mbEl.readString());
					else if (mbEl.name.equals("backgroundColor"))
						mb.setColor(mbEl.readFloatArray());
				}
				mb.load();
				button.hoverMessageBox = mb;
			}
		}
	}

	public void pollKeyboard() {
		if (windowStack.size() > 0)
			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState())
					if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
						popMenuStack();
			}
	}

	public void pollMouse() {
		
		if (windowStack.size() > 0)
			windowStack.get(windowStack.size() - 1).pollMouse();
		else
			for (Window w : gui.values()) {
				if (hovering(w)) {
					
					w.pollMouse();
				}
			}
	}
	
	public boolean hovering(Window w) {
		int x = Mouse.getX();
		int y = Mouse.getY();
		
		if ((x >= w.x && x <= w.x + w.width)
			&& (y >= w.y && y <= w.y + w.height))
			return true;
		
		return false;
	}
	
	void createClickListeners() {
		//---------------------------   Pause menu   ---------------------------//
		windows.get("pause").buttons.get("resume").setClickListener(new ClickListener() {
			public void onClick() {
				popMenuStack();
			}
		});
		windows.get("pause").buttons.get("save").setClickListener(new ClickListener() {
			public void onClick() {
				//saveGame();
			}
		});
		windows.get("pause").buttons.get("quit").setClickListener(new ClickListener() {
			public void onClick() {
				System.exit(0);
			}
		});
		
		if (gui.get("storyBox") != null)
		gui.get("storyBox").messageBoxes.get(0).setClickListener(new ClickListener() {
			public void onScroll(int delta) {
				if (delta > 0) {
					gui.get("storyBox").messageBoxes.get(0).moveDown();
				}
				else {
					gui.get("storyBox").messageBoxes.get(0).moveUp();
				}
			}
		});
	}
}
