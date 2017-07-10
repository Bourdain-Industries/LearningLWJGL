package game;

import static org.lwjgl.glfw.GLFW.*;
import game.framework.*;

public class Game {
	
	enum State {
		Running, Pause
	}
	
	private static State gameState;
	private static Player player;
	static Background bg1;
	static RandomDungeon dungeon1;
	Renderer renderer;
	
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
		renderer = new Renderer();
		renderer.init(bg1);
		renderer.init(player);
	}
	protected void update() {
		bg1.update();
		player.update();
	}
	protected void render() {
		if (this.isPaused()) {
			//TODO: pause menu
		}
		else {
			renderer.render(bg1);
			renderer.render(player);
		}
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
		renderer.cleanup(bg1);
		renderer.cleanup(player);
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
