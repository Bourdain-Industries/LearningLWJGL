package game;

public class Enemy extends Unit {
	
	private int deathXP;

	public Enemy(DungeonRoom location, String name, int level) {
		super(location, name, level, 0);
		this.deathXP = 12;	//temporary value
	}
	
	public int getDeathXP(){
		return this.deathXP;
	}

	@Override
	public float[] init(int vao) {
		return new float[0];
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNumVertices() {
		return 6;
	}

}
