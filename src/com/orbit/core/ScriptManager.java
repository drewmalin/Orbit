package com.orbit.core;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public enum ScriptManager {
	
	MANAGER;
	
	private PythonInterpreter interpreter = null;
	private PyObject pyObj, entObj;
	public EntityScript entityScript;
	//public Game gameHandle;
	
	ScriptManager() {
		
		PythonInterpreter.initialize(System.getProperties(),
								     System.getProperties(), new String[0]);
		
		interpreter = new PythonInterpreter();
		
		interpreter.exec("import sys\n"+
					     "sys.path.append(\"res/scripts\")\n");
	}
	
	public void run(String script) {
		interpreter.exec("from " + script + " import Entity");
		pyObj = interpreter.get("Entity");
		entObj = pyObj.__call__();
		
		entityScript = (EntityScript) entObj.__tojava__(EntityScript.class);
	}
	
	public void runFile(String script) {
		interpreter.execfile(script);
	}
	
	// when playerfocus entity interacts with this entity
	public void onInteract(GameEntity ge) {
		
		Object queryScript;
		
		if (ge.scriptFile != "") {

			ScriptManager.MANAGER.run(ge.scriptFile);
			if (ScriptManager.MANAGER.entityScript.onInteract() != null) {
				
				queryScript = ScriptManager.MANAGER.entityScript.onInteract().get("level");
				if (queryScript != null) {
					ResourceManager.MANAGER.changeLevel(queryScript.toString());
				}
				
				queryScript = ScriptManager.MANAGER.entityScript.onInteract().get("newMessage");
				if (queryScript != null) {
					WindowManager.MANAGER.gui.get("storyBox").messageBoxes.get(0).replaceMessage(queryScript.toString());
				}
				
				queryScript = ScriptManager.MANAGER.entityScript.onInteract().get("appendMessage");
				if (queryScript != null) {
					WindowManager.MANAGER.gui.get("storyBox").messageBoxes.get(0).addMessage(queryScript.toString());
				}
				
				queryScript = ScriptManager.MANAGER.entityScript.onInteract().get("destroy");
				if (queryScript != null) {
					if (queryScript.toString().toLowerCase().equals("true"))
						ResourceManager.MANAGER.gameEntities.remove(ge);
				}
			}
		}
	}
}
