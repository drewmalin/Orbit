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

		for (KeyTrigger kt : keyTriggers) {
			if (Keyboard.isKeyDown(kt.key)) {
				kt.onEvent();
			}
		}
		
		while (Keyboard.next()) {
			if (!Keyboard.getEventKeyState()) continue;
			
			keyboardListener.onEvent(Keyboard.getEventKey());
		}
		
	}
	
	public void pollMouse() {
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				mouseListener.onEvent(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
			}
		}
	}

	public void addKeyTrigger(int key, KeyTrigger keyTrigger) {
		keyTrigger.key = key;
		keyTriggers.add(keyTrigger);
	}
}
