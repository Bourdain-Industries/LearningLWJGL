package game;

import static org.lwjgl.opengl.GL15.glGenBuffers;

import game.framework.TextureLoader;

public class Player extends Unit {

	private int xp;
	private DungeonRoom lastLocation;

	public Player(DungeonRoom location, String name, int level) {
		super(location, name, level, 5);
		lastLocation = location;
		location.setExplored(true);
	}

	public void gainXP(int gainedXP) {
		xp += gainedXP;
		if (xp >= 100) {
			xp -= 100;
			gainLevel();
		}
	}
	
	@Override
	public float[] init(int vao) {
		loadTextures();
		this.vao = vao;
		vbo = glGenBuffers();
		ebo = glGenBuffers();
		vertices = new float[]{
				0f, 0f, 0.0f,	0f,0f,0f, 	0f, 0f,
				0f, 0f, 0.0f,	0f,0f,0f, 	0f, 1f,
				0f, 0f, 0.0f,	0f,0f,0f, 	1f, 0f,
				0f, 0f, 0.0f,	0f,0f,0f,	1f, 1f
		};
		return vertices;
		
	}
	
	@Override
	public void update() {
		
		centerX += speedX;
		centerY += speedY;
		
		// Prevents going beyond X coordinate of 0 unless there is a door
		if (centerX + speedX <= -0.8f) {
			if (location.getWest() != null && centerY > -0.15f && centerY < 0.15f) {
				moveLocation(location.getWest());
				centerX = 0.78f;
			} else centerX = -0.78f;
		}
		// Prevents going beyond X coordinate of 800 unless there is a door
		if (centerX + speedX >= 0.8f) {
			if (location.getEast() != null && centerY > -0.15f && centerY < 0.15f) {
				moveLocation(location.getEast());
				centerX = -0.78f;
			} else centerX = 0.78f;
		}

		// Prevents going beyond Y coordinate of 0 unless there is a door
		if (centerY + speedY >= 0.8f) {
			if (location.getNorth() != null && centerX > -0.15f && centerX < 0.15f) {
				moveLocation(location.getNorth());
				centerY = -0.78f;
			} else centerY = 0.78f;
		}
		// Prevents going beyond Y coordinate of 480 unless there is a door
		if (centerY + speedY <= -0.8f) {
			if (location.getSouth() != null && centerX > -0.15f && centerX < 0.15f) {
				moveLocation(location.getSouth());
				centerY = 0.78f;
			} else centerY = -0.78f;
		}
		
		super.update();
	}

	public void moveLocation(DungeonRoom location) {
		if (location == null) return;
		lastLocation = this.location;
		super.setLocation(location);
		System.out.println(Integer.toString(location.getExits()));
		location.setExplored(true);
	}
	
	public void runAway() {
		if (location == lastLocation) {
			int exits = location.getExits();
			if (exits%10 > 0) lastLocation = location.getSouth();
			else if (exits%100 > 0) lastLocation = location.getWest();
			else if (exits%1000 > 0) lastLocation = location.getNorth();
			else lastLocation = location.getEast();
		}
		moveLocation(lastLocation);
	}

	protected void gainLevel() {
		super.gainLevel();
	}

	@Override
	public int getTotalAttack() {
		return super.getTotalAttack() + 5;
	}

	private void loadTextures() {
		int character2 =TextureLoader.loadTexture( TextureLoader.loadImage("/data/character2.png"));
		anim.addFrame(TextureLoader.loadTexture(TextureLoader.loadImage("/data/character.png")), 1250);
		anim.addFrame(character2, 50);
		anim.addFrame(TextureLoader.loadTexture(TextureLoader.loadImage("/data/character3.png")), 50);
		anim.addFrame(character2, 50);
	}

}
