package game;

import game.framework.IRenderable;

public class LootableObject implements IRenderable {
	
	private DungeonRoom room;
	private Item[] items;
	private String type;	//type of object i.e. bookshelf, desk, chest, etc.
	private int numItems = 0;
	static int vao, vbo, ebo;
	
	public LootableObject(){
		
	}
	
	@Override
	public int getVao() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVbo() {
		return vbo;
	}
	@Override
	public int getEbo() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getTexture() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float[] init(int vao) {
		// TODO Auto-generated method stub
		return new float[0];
	}
}
