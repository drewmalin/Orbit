package com.orbit.core;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.glGetShader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderManager {
	
	private final Game gameHandle;
	
	private int shaderProgram;
	private int vertexShader;
	private int fragmentShader;
	
	private StringBuilder vertexSource;
	private StringBuilder fragmentSource;
	
	public FloatBuffer parmData;
	
	public ShaderManager(Game g) {
		gameHandle = g;
		
		vertexSource = new StringBuilder();
		fragmentSource = new StringBuilder();
		
		parmData = BufferUtils.createFloatBuffer(40);
	}
	
	public void persistParmData(int lvl) {
		int location = GL20.glGetUniformLocation(shaderProgram, "array");
		parmData.flip();
		GL20.glUniform1(location, parmData);
		
		location = GL20.glGetUniformLocation(shaderProgram, "screenHeight");
		GL20.glUniform1i(location, gameHandle.graphicsManager.getHeight());
		
		int count = 0;
		for (GameEntity ge : gameHandle.gameEntities)
			if (ge.mapLevel == lvl)
				count++;
		
		location = GL20.glGetUniformLocation(shaderProgram, "count");
		GL20.glUniform1i(location, count);
	}
	
	public void init(String frag, String vert) {
		vertexShader 	= GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShader 	= GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		shaderProgram 	= GL20.glCreateProgram();

		loadFragmentShader(frag);
		loadVertexShader(vert);
		linkShaders();
	}
	
	public void loadVertexShader(String vertexShaderFile) {
		loadShader(vertexShader, vertexShaderFile, vertexSource);
	}
	
	public void loadFragmentShader(String fragmentShaderFile) {
		loadShader(fragmentShader, fragmentShaderFile, fragmentSource);
	}
	
	public void linkShaders() {
		GL20.glAttachShader(shaderProgram, vertexShader);
		GL20.glAttachShader(shaderProgram, fragmentShader);
		GL20.glLinkProgram(shaderProgram);
		GL20.glValidateProgram(shaderProgram);
	}
	
	public void bind() {
		GL20.glUseProgram(shaderProgram);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	private void loadShader(int shader, String shaderFile, StringBuilder shaderSource) {
		String line;
		try {
			FileInputStream fstream = new FileInputStream(shaderFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			while ((line = br.readLine()) != null) {
				shaderSource.append(line).append('\n');
			}
			
			GL20.glShaderSource(shader, shaderSource);
			GL20.glCompileShader(shader);
			
			if (GL20.glGetShader(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				System.err.println("Shader failed to load: " + shaderFile);
				System.exit(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
