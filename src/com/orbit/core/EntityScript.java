package com.orbit.core;
import org.python.core.PyDictionary;

public interface EntityScript {
	public PyDictionary onInteract();
	public PyDictionary onTouch();
	public PyDictionary onStand();
}
