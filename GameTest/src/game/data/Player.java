package game.data;

public class Player extends Unit {

	private int xp;

	public Player(DungeonRoom location, String name, int level) {
		super(location, name, level);
	}

	public void gainXP(int gainedXP) {
		this.xp += gainedXP;
		if (this.xp >= 100) {
			this.xp -= 100;
			this.gainLevel();
		}
	}

	public void move(int direction) {
		DungeonRoom location = null;
		switch (direction) {
		case 1:
			location = this.location.getSouth();
			break;
		case 2:
			location = this.location.getWest();
			break;
		case 4:
			location = this.location.getNorth();
			break;
		case 8:
			location = this.location.getEast();
			break;
		}
		if (location == null) location = this.location;
		super.setLocation(location);
	}

	protected void gainLevel() {
		super.gainLevel();
	}

	@Override
	public int getTotalAttack() {
		return super.getTotalAttack() + 5;
	}

}
