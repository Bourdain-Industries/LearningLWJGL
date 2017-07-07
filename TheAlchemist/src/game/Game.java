package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import game.framework.*;

public class Game {
	
	enum State {
		Running, Pause
	}
	
	private State gameState;
	private static Player player;
	static Background bg1;
	static RandomDungeon dungeon1;
	ShaderProgram shaderProgram;
	
	protected Game() {
		gameState = State.Running;
		bg1 = new Background(0, 0);
		dungeon1 = new RandomDungeon(1);
		player = new Player(dungeon1.getEntrance(), "Apprentice", 1);
		
		/*
		heliboy = TextureLoader.loadImage("/data/heliboy.png");
		heliboy2 = TextureLoader.loadImage("/data/heliboy2.png");
		heliboy3 = TextureLoader.loadImage("/data/heliboy3.png");
		heliboy4 = TextureLoader.loadImage("/data/heliboy4.png");
		heliboy5 = TextureLoader.loadImage("/data/heliboy5.png");
		*/
	}
	
	public static DungeonRoom getPlayerLocation() {
		return player.getLocation();
	}
	
	protected void init() {
		try {
			shaderProgram = new ShaderProgram();
			shaderProgram.createVertexShader(
				"#version 330 core\n" +
				"layout (location = 0) in vec3 aPos; " +
				"layout (location = 1) in vec3 aColor; " +
				"layout (location = 2) in vec2 aTexCoord; " +
				"out vec3 ourColor; " +
				"out vec2 TexCoord; " +
				"void main() { " +
				"gl_Position = vec4(aPos, 1.0); " +
				"ourColor = aColor; " +
				"TexCoord = aTexCoord; }");
			shaderProgram.createFragmentShader(
				"#version 330 core\n" +
				"out vec4 FragColor; " +
				"in vec3 ourColor; " +
				"in vec2 TexCoord; " +
				"// texture sampler\n" +
				"uniform sampler2D texture1; " +
				"void main() { " +
				"FragColor = texture(texture1, TexCoord); }");
			shaderProgram.link();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bg1.init();
		player.init();

	}
	protected void update() {
		bg1.update();
		player.update();
	}
	protected void render() {
		shaderProgram.bind();
		
		bg1.render();
		player.render();
		
		shaderProgram.unbind();
	}
	
	protected void processInput(long window, int key, int scancode, int action, int mods){
		
		if ( this.isPaused() ) {
			switch (key) {
			case GLFW_KEY_ESCAPE :
				if ( action == GLFW_PRESS )
					unpause();
				break;
				
			case GLFW_KEY_Q :
				quit();
				glfwSetWindowShouldClose(window, true);
				break;
				
			case GLFW_KEY_R :
				player = new Player(dungeon1.getEntrance(), player.getName(), player.getLevel());
				break;

			default : break;
			}
		}
			
		else if ( player.isAlive() && action == GLFW_PRESS ) {
			switch (key) {
			case GLFW_KEY_ESCAPE :
				pause();
				break;
				
			case GLFW_KEY_W :
				player.moveUp();
				break;
				
			case GLFW_KEY_S :
				player.moveDown();
				break;

			case GLFW_KEY_A :
				player.moveLeft();
				break;

			case GLFW_KEY_D :
				player.moveRight();
				break;
			
			case GLFW_KEY_R :
				if ( player.getLocation().isOccupied() )
					player.runAway();
				break;
			default : break;
			}
		}
		else if ( player.isAlive() && action == GLFW_RELEASE ) {
			switch (key) {
			case GLFW_KEY_W :
				player.stopUp();
				break;
				
			case GLFW_KEY_S :
				player.stopDown();
				break;

			case GLFW_KEY_A :
				player.stopLeft();
				break;

			case GLFW_KEY_D :
				player.stopRight();
				break;
			
			default : break;
			}
		}
		
		else if ( !player.isAlive() )
			switch (key) {
			case GLFW_KEY_ESCAPE :
				if ( action == GLFW_PRESS )
					pause();
				break;
				
			case GLFW_KEY_Q :
				quit();
				glfwSetWindowShouldClose(window, true);
				break;
				
			case GLFW_KEY_R :
				player = new Player(dungeon1.getEntrance(), player.getName(), player.getLevel());
				break;

			default : break;
			}
						
	}
		
	private void quit() {
		if (shaderProgram != null) shaderProgram.cleanup();
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		bg1.cleanup();
		player.cleanup();
	}

	public boolean isRunning() {
		return gameState == State.Running;
	}
	public boolean isPaused() {
		return gameState == State.Pause;
	}

	public void pause() {
		gameState = State.Pause;
	}

	public void unpause() {
		gameState = State.Running;
	}

}
