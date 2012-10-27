package com.orbit.ui;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.orbit.core.WindowManager;

public class Window extends Canvas {
	public HashMap<String, Button> buttons;
	public Button clickedButton;
	public String name;

	public Window(WindowManager wm, Canvas parent) {
		super(wm, parent);
		buttons = new HashMap<String, Button>();
	}

	public void draw() {
		super.draw();
		drawMessages();
		drawButtons();
	}

	public Window setWidth(int w) {
		width = w;
		return this;
	}
	
	public Window setHeight(int h) {
		height = h;
		return this;
	}
	
	public void drawButtons() {
		for (String b : buttons.keySet()) {
			buttons.get(b).draw();
		}
//		for (String b : buttons.keySet()) {
//			if (buttons.get(b).hoverMessageBox != null) {
//				if (buttons.get(b).hovering)
//					buttons.get(b).openHoverMessageBox();
//				else
//					buttons.get(b).closeHoverMessageBox();
//			}
//		}
	}
	/*
	public void updateLoadingScreen(String message, float percentage) {
		
		this.draw();
		messageBoxes.get(0).setMessage(message);

		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1f, 0f, 0f, 1f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f((Engine.WIDTH * .1f), (Engine.HEIGHT * .75f));
			GL11.glVertex2f((Engine.WIDTH * .1f), (Engine.HEIGHT * .75f) + 10);
			GL11.glVertex2f((Engine.WIDTH * .1f) + ((Engine.WIDTH * .8f) * percentage), (Engine.HEIGHT * .75f) + 10);		
			GL11.glVertex2f((Engine.WIDTH * .1f) + ((Engine.WIDTH * .8f) * percentage), (Engine.HEIGHT * .75f));
		GL11.glEnd();
		GL11.glPopMatrix();
		Display.update();

	}
	*/
	/*
	public void checkGuiClick() {
		for (String b : buttons.keySet()) {
			buttons.get(b).checkHover(Mouse.getX(), Engine.HEIGHT - Mouse.getY());
			if (buttons.get(b).hovering && Mouse.isButtonDown(0)) {
				clickedButton = buttons.get(b);
			}
			if (buttons.get(b).hovering && Input.isMouseButtonUp() && clickedButton != null && clickedButton.equals(b)) {
				buttons.get(b).onClick();
				clickedButton = null; 
			}
		}
	}
	*/
	public void poll() {
		pollMouse();
		pollKeyboard();
	}
	
	public void pollMouse() {
		
		int mouseWheelDelta = Mouse.getDWheel();
		
		for (String b : buttons.keySet()) {
			buttons.get(b).checkHover(Mouse.getX(), windowManagerHandle.gameHandle.graphicsManager.getHeight() - Mouse.getY());
		}
		for (MessageBox mb : messageBoxes) {
			mb.checkHover(Mouse.getX(), windowManagerHandle.gameHandle.graphicsManager.getHeight() - Mouse.getY());
		}
		
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				switch (Mouse.getEventButton()) {
					case 0:	//Left click
						for (Button b : buttons.values()) {
							if (b.hovering) {
								b.onClick();
							}						
						}
						for (MessageBox mb : messageBoxes) {
							if (mb.hovering)
								mb.onClick();
						}
						break;
					case 1: //Right click
						break;
				}
			}
			if (mouseWheelDelta != 0)
				for (MessageBox mb : messageBoxes) {
					if (mb.hovering) {
						mb.onScroll(mouseWheelDelta);
					}
				}
		}
	}
	
	public void pollKeyboard() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				
				switch (Keyboard.getEventKey()) {
					//Quit the game
					case Keyboard.KEY_ESCAPE:
						System.exit(0);
						break;
				}
			}
		}
	}
}