package game.data;

public class Enemy extends Unit {
	
	private int deathXP;

	public Enemy(DungeonRoom location, String name, int level) {
		super(location, name, level);
		this.deathXP = 12;	//temp value
	}
	
	public int getDeathXP(){
		return this.deathXP;
	}

}
