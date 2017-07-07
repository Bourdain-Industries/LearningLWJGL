package game;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Exit diagram--pass exit value when moving to next room to indicate direction
 * 		2
 * 	1		3
 * 		0
 */

public class DungeonRoom {
	
	private final RandomDungeon dungeon;
	private DungeonRoom north = null;
	private DungeonRoom south = null;
	private DungeonRoom west = null;
	private DungeonRoom east = null;
	
	private int x, y;
	private boolean explored = false;
	private boolean enemyOccupied = false;
	
	private List<Enemy> enemies;
	private int level;
	
	public DungeonRoom(RandomDungeon dungeon, int x, int y, int level){
		this.dungeon = dungeon;
		this.x = x;
		this.y = y;
		this.level = level;
		populateRoom(level);		
		System.out.println("Room created at " + x + "," + y);
	}

	public void createBranches() {
		int rand = (this.x==0 && this.y==0) ? new Random().nextInt(13) + 3 : //range 3-15
			new Random().nextInt(this.x+this.y+14) + 1;
		//range depends on distance from center...farther rooms are more likely to be dead ends
		if (rand > 15)
			return;		//no doors=dead end--likely place for important things
		if (rand>=8){
			this.east = this.dungeon.addRoom(this.x+1, this.y, this.level);
			if (this.east != null){			
				this.east.addDoor(this, 3);
			}
			rand -= 8;
		}
		if (rand>=4){
			this.north = this.dungeon.addRoom(this.x, this.y-1, this.level);
			if (this.north != null){
				this.north.addDoor(this, 2);
			}
			rand -= 4;
		}
		if (rand>=2){
			this.west = this.dungeon.addRoom(this.x-1, this.y, this.level);
			if (this.west != null){
				this.west.addDoor(this, 1);
			}
			rand-=2;
		}
		if (rand>=1){
			this.south = this.dungeon.addRoom(this.x, this.y+1, this.level);
			if (this.south != null){
				this.south.addDoor(this, 0);
			}
			rand--;
		} if (rand>0) System.out.println("failed");
	}
	
	private void addDoor(DungeonRoom room, int direction) {
		switch (direction) {
		case 0:
			this.north = room;
			break;
		case 1:
			this.east = room;
			break;
		case 2:
			this.south = room;
			break;
		case 3:
			this.west = room;
			break;
		}
	}
	
	private void populateRoom(int level){
		if (this == dungeon.getEntrance())
			return;
		Random random = new Random();
		int num = random.nextInt(3);
		this.enemies = new ArrayList<>(num);
		for (int i = 0; i < num; i++){
			level += random.nextInt(3);
			level -= random.nextInt(1);
			enemies.add(new Enemy(this, "Slime ".concat(Integer.toString(i)), level+1));
			this.enemyOccupied = true;
		}
	}
	
	public boolean combat(Player player){
		while (player.isAlive() && this.enemyOccupied){
			for (Enemy enemy : enemies){
				System.out.printf("%s took %d damage from %s!\n",
						player.getName(), player.takeDamage(enemy.getTotalAttack()), enemy.getName());
			}
			System.out.printf("%s took %d damage from %s!\n",
					enemies.get(0).getName(), enemies.get(0).takeDamage(player.getTotalAttack()), player.getName());
			if (!enemies.get(0).isAlive()){
				System.out.printf("%s has been slain!\n", enemies.get(0).getName());
				player.gainXP(enemies.remove(0).getDeathXP());
				if (enemies.size()==0) this.enemyOccupied = false;
			}
		}
		return player.isAlive();
	}
	
	public byte getNumberOfExits(){
		byte exits = 0;
		if (this.west != null) exits++;
		if (this.north != null) exits++;
		if (this.south != null) exits++;
		if (this.east != null) exits++;
		return exits;
	}

	public int getExits(){
		int exits = 0;
		if (this.west != null) exits+=2;
		if (this.north != null) exits+=4;
		if (this.south != null) exits+=1;
		if (this.east != null) exits+=8;
		return exits;
	}
	public boolean isOccupied() {
		return enemyOccupied;
	}

	public void setOccupied(boolean occupied) {
		this.enemyOccupied = occupied;
	}

	public boolean isExplored() {
		return explored;
	}

	public void setExplored(boolean explored) {
		this.explored = explored;
	}

	public DungeonRoom getNorth() {
		return north;
	}

	public DungeonRoom getSouth() {
		return south;
	}

	public DungeonRoom getWest() {
		return west;
	}

	public DungeonRoom getEast() {
		return east;
	}

	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(" |");
		if (this.south != null) str.append('S');
		else str.append(' ');
		if (this.west != null) str.append('W');
		else str.append(' ');
		if (this.north != null) str.append('N');
		else str.append(' ');
		if (this.east != null) str.append('E');
		else str.append(' ');
		return str.append("| ").toString();
	}
}
