package com.orbit.core;
import org.python.core.PyDictionary;

public interface EntityScript {
	public void setPosition(float x, float y, float z);
	public PyDictionary onInteract();
	public PyDictionary onTouch();
	public PyDictionary onStand();
}
