package game;

import java.util.HashMap;
import static org.lwjgl.opengl.GL15.*;

import game.framework.IRenderable;
import game.framework.TextureLoader;

public class Background implements IRenderable{

	private int bgX, bgY, speedX, speedY, vao, vbo, ebo;
	private HashMap<Integer, Integer> backgrounds;
	private static final int BG_SOUTH_EXIT = 1;
	private static final int BG_WEST_EXIT = 2;
	private static final int BG_NORTH_EXIT = 4;
	private static final int BG_EAST_EXIT = 8;

	public Background(int x, int y) {
		bgX = x;
		bgY = y;
		speedX = 0;
	}

	public void update() {
		/*
		bgY += speedY;
		
		if (bgX>=-1000){
			bgX += speedX;
		}
		*/
		
	}

	public int getBgX() {
		return bgX;
	}

	public int getBgY() {
		return bgY;
	}

	public int getSpeedX() {
		return speedX;
	}
	public int getSpeedY() {
		return speedY;
	}

	public void setBgX(int bgX) {
		this.bgX = bgX;
	}

	public void setBgY(int bgY) {
		this.bgY = bgY;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	
	public int getVao() {
		return vao;
	}
	public int getVbo() {
		return vbo;
	}
	public int getEbo() {
		return ebo;
	}
	public int getTexture() {
		return backgrounds.get(Game.getPlayerLocation().getExits());
	}

	@Override
	public float[] init(int vao) {
		loadTextures();
		this.vao = vao;
		vbo = glGenBuffers();
		ebo = glGenBuffers();
		
		return new float[]{
				1.0f, 1.0f, 0.0f, 0f,0f,0f, 1f, 1f,
				1.0f, -1.0f, 0.0f,	0f,0f,0f, 1f, 0f,
				-1.0f, 1.0f, 0.0f,	0f,0f,0f, 0f, 1f,
				-1.0f, -1.0f, 0.0f,	0f,0f,0f, 0f, 0f
		};
	}

	private void loadTextures() {
		backgrounds = new HashMap<>();
		backgrounds.put(BG_WEST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/west.png")));
		backgrounds.put(BG_EAST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/east.png")));
		backgrounds.put(BG_NORTH_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/north.png")));
		backgrounds.put(BG_SOUTH_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/south.png")));
		backgrounds.put(BG_EAST_EXIT | BG_WEST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/westeast.png")));
		backgrounds.put(BG_NORTH_EXIT | BG_WEST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/northwest.png")));
		backgrounds.put(BG_SOUTH_EXIT | BG_WEST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/southwest.png")));
		backgrounds.put(BG_SOUTH_EXIT | BG_EAST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/southeast.png")));
		backgrounds.put(BG_NORTH_EXIT | BG_EAST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/northeast.png")));
		backgrounds.put(BG_SOUTH_EXIT | BG_NORTH_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/northsouth.png")));
		backgrounds.put(BG_NORTH_EXIT | BG_WEST_EXIT | BG_SOUTH_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/noeast.png")));
		backgrounds.put(BG_SOUTH_EXIT | BG_EAST_EXIT | BG_WEST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/nonorth.png")));
		backgrounds.put(BG_SOUTH_EXIT | BG_EAST_EXIT | BG_NORTH_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/nowest.png")));
		backgrounds.put(BG_NORTH_EXIT | BG_EAST_EXIT | BG_WEST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/nosouth.png")));
		backgrounds.put(BG_SOUTH_EXIT | BG_EAST_EXIT | BG_NORTH_EXIT | BG_WEST_EXIT, TextureLoader.loadTexture(TextureLoader.loadImage("/data/allexits.png")));
	}
	
}