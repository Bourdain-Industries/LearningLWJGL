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
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
