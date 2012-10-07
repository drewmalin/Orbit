package com.orbit.core;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputManager {
	private InputListener mouseListener;
	private InputListener keyboardListener;
	private ArrayList<KeyTrigger> keyTriggers;
	
	private Game gameHandle;
	
	public InputManager(Game g) {
		mouseListener 		= new InputListener();
		keyboardListener 	= new InputListener();
		gameHandle 			= g;
		keyTriggers			= new ArrayList<KeyTrigger>();
	}
	
	public void setMouseListener(InputListener il) {
		mouseListener = il;
	}
	
	public void setKeyboardListener(InputListener il) {
		keyboardListener = il;
	}
	
	public void pollKeyboard() {
		if (gameHandle.windowManager.windowStack.size() > 0) {
			gameHandle.windowManager.pollKeyboard();
		}
		else {
			keyboardListener.onEvent();
		}
	}
	
	public void pollMouse() {
		
		// Poll GUI, steal context if menu stack size > 0
		gameHandle.windowManager.pollMouse();
		
		// Regular game mouse polling
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				mouseListener.onEvent(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
			}
			// Make sure the wheel delta doesn't go crazy
			Mouse.getDWheel();
		}
		
		
	}

	public void addKeyTrigger(int key, KeyTrigger keyTrigger) {
		keyTrigger.key = key;
		keyTriggers.add(keyTrigger);
	}
}
