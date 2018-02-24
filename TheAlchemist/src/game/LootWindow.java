package game;

import static org.lwjgl.opengl.GL15.glGenBuffers;

import org.joml.Vector2f;

import game.framework.IRenderable;
import game.framework.TextureLoader;

public class LootWindow implements IRenderable {

	private int vao, vbo, ebo;
	private float[] vertices;
	private Vector2f position;
	private int texture;
	private float sizeX, sizeY;

	public LootWindow(Item[] items) {
		position = new Vector2f(0f, 0f);
		sizeX = (float) Math.ceil(Math.sqrt(items.length));
		sizeY = (float) Math.ceil(items.length/sizeX)*0.2f;
		sizeX *= 0.2f;
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
				position.x - sizeX, position.y - sizeY, 0.0f,	0f,0f,0f, 	0f, 0f,
				position.x - sizeX, position.y + sizeY, 0.0f,	0f,0f,0f, 	0f, 1f,
				position.x + sizeX, position.y - sizeY, 0.0f,	0f,0f,0f, 	1f, 0f,
				position.x + sizeX, position.y + sizeY, 0.0f,	0f,0f,0f,	1f, 1f
		};
		return vertices;
	}

	private void loadTexture() {
		texture = TextureLoader.loadTexture(TextureLoader.loadImage("/data/chest.png"));
	}

}
