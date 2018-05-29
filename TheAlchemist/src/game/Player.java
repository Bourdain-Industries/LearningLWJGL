package game;

import static org.lwjgl.opengl.GL15.glGenBuffers;

import game.framework.TextureLoader;

public class Player extends Unit {

	private int xp;
	private DungeonRoom lastLocation;
	private Boolean looting = false;
	private static final float SIDE_WALL = 0.79f;
	private static final float TOP_WALL = 0.85f;
	private static final float BOTTOM_WALL = -0.68f;

	public Player(DungeonRoom location, String name, int level) {
		super(location, name, level, 6);
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
		
		// Prevents going beyond left wall unless there is a door
		if (position.x + speed.x <= -SIDE_WALL) {
			if (location.getWest() != null && position.y > -0.06f && position.y < 0.25f) {
				moveLocation(location.getWest());
				position.x = -position.x;
			}
		}
		// Prevents going beyond right wall unless there is a door
		else if (position.x + speed.x >= SIDE_WALL) {
			if (location.getEast() != null && position.y > -0.06f && position.y < 0.25f) {
				moveLocation(location.getEast());
				position.x = -position.x;
			}
		}
		else position.x += speed.x;

		// Prevents going beyond top wall unless there is a door
		if (position.y + speed.y >= TOP_WALL) {
			if (location.getNorth() != null && position.x > -0.06f && position.x < 0.06f) {
				moveLocation(location.getNorth());
				position.y = BOTTOM_WALL + 0.01f;
			}
		}
		// Prevents going beyond bottom wall unless there is a door
		else if (position.y + speed.y <= BOTTOM_WALL) {
			if (location.getSouth() != null && position.x > -0.06f && position.x < 0.06f) {
				moveLocation(location.getSouth());
				position.y = TOP_WALL - 0.01f;
			}
		}
		else position.y += speed.y;
		
		super.update();
	}

	public void moveLocation(DungeonRoom location) {
		if (location == null) return;
		lastLocation = this.location;
		super.setLocation(location);
		this.closeLootWindow();
		System.out.println(Integer.toString(location.getExits()));
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

	public void activate() {
		looting = location.activateObject(position);
	}

	public Boolean isLooting() {
		return looting;
	}

	public void closeLootWindow() {
		this.looting = false;
		
	}

	@Override
	public int getNumVertices() {
		return 6;
	}

	public void jump() {
		// TODO Auto-generated method stub
		
	}

}
