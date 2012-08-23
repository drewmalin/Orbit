package com.orbit.core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputManager {
	private InputListener mouseListener;
	private InputListener keyboardListener;
	
	public InputManager() {
		mouseListener 		= new InputListener();
		keyboardListener 	= new InputListener();
	}
	
	public void setMouseListener(InputListener il) {
		mouseListener = il;
	}
	
	public void setKeyboardListener(InputListener il) {
		keyboardListener = il;
	}
	
	public void pollKeyboard() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				keyboardListener.onEvent(Keyboard.getEventKey());
			}
		}
	}
	
	public void pollMouse() {
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				mouseListener.onEvent(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
			}
		}
	}
}
