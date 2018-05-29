package game;

import static org.lwjgl.opengl.GL15.glGenBuffers;

import org.joml.Vector2f;

import game.framework.*;

public class LootableObject implements IRenderable {
	
	enum Type {
		Bookcase,
		Chest,
		Dresser,
		Armoire,
		Crate
	}
	
	private Item[] items;
	private Type type;
	private LootWindow lootWindow;
	protected Vector2f position = new Vector2f(0f, 0f);
	protected int vao, vbo, ebo, texture;
	protected float[] vertices;
	
	public LootableObject(){
		position.x = 0.82f;
		position.y = 0.72f;
		this.type = Type.Chest;
		generateLoot();
		lootWindow = new LootWindow(items);
	}
	
	private void generateLoot() {
		items = new Item[10];
	}

	@Override
	public int getVao() {
		return vao;
	}

	@Override
	public int getVbo() {
		return vbo;
	}
	@Override
	public int getEbo() {
		return ebo;
	}	
	@Override
	public int getTexture() {
		return texture;
	}

	@Override
	public float[] init(int vao) {
		loadTexture();
		this.vao = vao;
		vbo = glGenBuffers();
		ebo = glGenBuffers();
		vertices = new float[]{
				position.x - 0.07f, position.y - 0.1f, 0.0f,	0f,0f,0f, 	0f, 0f,
				position.x - 0.07f, position.y + 0.1f, 0.0f,	0f,0f,0f, 	0f, 1f,
				position.x + 0.07f, position.y - 0.1f, 0.0f,	0f,0f,0f, 	1f, 0f,
				position.x + 0.07f, position.y + 0.1f, 0.0f,	0f,0f,0f,	1f, 1f
		};
		return vertices;
		
	}

	private void loadTexture() {
		if (this.type == Type.Chest) {
			texture = TextureLoader.loadTexture(TextureLoader.loadImage("/data/chest.png"));
		}
		else {
			texture = TextureLoader.loadTexture(TextureLoader.loadImage("/data/character3.png"));
		}
	}

	public Item[] getContents() {
		return items;
	}
	
	public LootWindow getLootWindow() {
		return lootWindow;
	}

	@Override
	public int getNumVertices() {
		return 6;
	}
	
}
