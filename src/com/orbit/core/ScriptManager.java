package com.orbit.core;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class ScriptManager {
	
	private PythonInterpreter interpreter = null;
	private PyObject pyObj, entObj;
	public EntityScript entityScript;
	public Game gameHandle;
	
	public ScriptManager(Game g) {
		
		gameHandle = g;
		
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
}
