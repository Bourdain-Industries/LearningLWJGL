package game;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

import game.framework.*;

public class Game {
	
	enum State {
		Running, Pause
	}
	
	private static State gameState;
	private static Player player;
	static RandomDungeon dungeon;
	Renderer renderer;
	static Camera camera;
	DoubleBuffer cameraX = BufferUtils.createDoubleBuffer(1);
	DoubleBuffer cameraY = BufferUtils.createDoubleBuffer(1);
	
	
	protected Game() {
		gameState = State.Running;
		dungeon = new RandomDungeon(1);
		player = new Player(dungeon.getEntrance(), "Apprentice", 1);
		camera = new Camera(true);
	}
	
	public static DungeonRoom getPlayerLocation() {
		return player.getLocation();
	}
	
	protected void init() {
		renderer = new Renderer(camera);
		player.getLocation().init(renderer);
		
		renderer.init(player);
	}
	protected void update(long window, double dt) {
		if (!this.isPaused()) {
			glfwGetCursorPos(window, cameraX, cameraY);
			glfwSetCursorPos(window, 400, 300);
			camera.update(cameraX.get(0), cameraY.get(0), dt);
		}
		DungeonRoom lastLocation = player.getLocation();
		player.update();
		DungeonRoom newLocation = player.getLocation();
		if (newLocation != lastLocation && !newLocation.isExplored()) {
			newLocation.init(renderer);
			newLocation.setExplored(true);
		}
	}
	protected void render() {
		if (this.isPaused()) {
			//TODO: pause menu
		}
		else {
			player.getLocation().render(renderer);
			
			renderer.render(player);
			if (player.isLooting()) {
				player.getLocation().renderLootWindow(renderer);
			}
		}
	}
	
	protected void processKeyboardInput(long window, int key, int scancode, int action, int mods){
		
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
				player = new Player(dungeon.getEntrance(), player.getName(), player.getLevel());
				renderer.init(player);
				unpause();
				break;

			default : break;
			}
		}
			
		else if ( player.isAlive() && action == GLFW_PRESS ) {
			switch (key) {
			case GLFW_KEY_ESCAPE :
				if(player.isLooting()) {
					player.closeLootWindow();
				}
				else pause();
				break;
				
			case GLFW_KEY_UP :
				player.moveUp();
				break;
				
			case GLFW_KEY_DOWN :
				player.moveDown();
				break;

			case GLFW_KEY_LEFT :
				player.moveLeft();
				break;

			case GLFW_KEY_RIGHT :
				player.moveRight();
				break;
				
			case GLFW_KEY_W :
				camera.startForward();
				break;
				
			case GLFW_KEY_S :
				camera.startBackward();
				break;

			case GLFW_KEY_A :
				camera.startLeft();
				break;

			case GLFW_KEY_D :
				camera.startRight();
				break;
				
			case GLFW_KEY_Q :
				player.getLocation().startShakeRoom(1000);
				break;
				
			case GLFW_KEY_E :
				player.activate();
				break;
				
			case GLFW_KEY_R :
				if ( player.getLocation().isOccupied() )
					player.runAway();
				break;
				
			case GLFW_KEY_M :
				dungeon.printDungeon(player.getLocation());
				break;
				
			case GLFW_KEY_SPACE :
				camera.startJump();
				break;
				
			default : break;
			}
		}
		else if ( player.isAlive() && action == GLFW_RELEASE ) {
			switch (key) {
			case GLFW_KEY_UP :
				player.stopUp();
				break;
				
			case GLFW_KEY_DOWN :
				player.stopDown();
				break;

			case GLFW_KEY_LEFT :
				player.stopLeft();
				break;

			case GLFW_KEY_RIGHT :
				player.stopRight();
				break;
				
			case GLFW_KEY_W :
				camera.stopForward();
				break;
				
			case GLFW_KEY_S :
				camera.stopBackward();
				break;

			case GLFW_KEY_A :
				camera.stopLeft();
				break;

			case GLFW_KEY_D :
				camera.stopRight();
				break;
				
			case GLFW_KEY_Q :
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
				player = new Player(dungeon.getEntrance(), player.getName(), player.getLevel());
				renderer.init(player);
				unpause();
				break;

			default : break;
			}
						
	}
		
	protected void processCursorInput(long window, double mouseX, double mouseY) {
		if (this.isPaused()) {
			
		}
		else {
			
		}
	}

	public void processMouseButtonInput(long window, int button, int action, int mods) {
		if (action == GLFW_RELEASE) camera.print();
	}
	
	private void quit() {
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
