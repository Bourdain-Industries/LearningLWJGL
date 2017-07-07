package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.system.MemoryUtil;

import game.framework.TextureLoader;

public class Background {

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

	protected void init() {
		loadTextures();
		
		float[] vertices =  new float[]{
				1.0f, 1.0f, 0.0f, 0f,0f,0f, 1f, 1f,
				1.0f, -1.0f, 0.0f,	0f,0f,0f, 1f, 0f,
				-1.0f, 1.0f, 0.0f,	0f,0f,0f, 0f, 1f,
				-1.0f, -1.0f, 0.0f,	0f,0f,0f, 0f, 0f
		};
		FloatBuffer vBuffer = MemoryUtil.memAllocFloat(vertices.length);
		vBuffer.put(vertices).flip();
	
		int[] indices = new int[] {
				0, 1, 2,
				1, 2, 3
		};
		IntBuffer iBuffer = MemoryUtil.memAllocInt(indices.length);
		iBuffer.put(indices).flip();
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		        
		vbo = glGenBuffers();
		ebo = glGenBuffers();
	
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
	    glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
	    MemoryUtil.memFree(vBuffer);
	
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
	    glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);
	    MemoryUtil.memFree(iBuffer);
	
		// vertex attribute
	    glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
	    // color attribute
	    glVertexAttribPointer(1, 3, GL_FLOAT, false, 32, 12);
	    // texture coord attribute
	    glVertexAttribPointer(2, 2, GL_FLOAT, false, 32, 24);
	
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	    glBindVertexArray(0);
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
	
	protected void render() {
		glBindVertexArray(vao);
		
		glBindTexture(GL_TEXTURE_2D, backgrounds.get(Game.getPlayerLocation().getExits()));
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
	    glEnableVertexAttribArray(0);
	  	glEnableVertexAttribArray(1);
	  	glEnableVertexAttribArray(2);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	protected void cleanup() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
				
	}
}
