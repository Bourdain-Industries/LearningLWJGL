package game;


import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;

import game.framework.IRenderable;
import game.framework.Renderer;
import game.framework.TextureLoader;

/* Exit diagram--pass exit value when moving to next room to indicate direction
 * 		2
 * 	1		3
 * 		0
 */

public class DungeonRoom implements IRenderable{
	
	private final RandomDungeon dungeon;
	private static final int SOUTH_EXIT = 1;
	private static final int WEST_EXIT = 2;
	private static final int NORTH_EXIT = 4;
	private static final int EAST_EXIT = 8;
	private static final int LOOT_CHANCE = 90;

	private DungeonRoom north = null;
	private DungeonRoom south = null;
	private DungeonRoom west = null;
	private DungeonRoom east = null;
	
	private int x, y, vao, vbo, ebo;
	private int texture = 0;

	private boolean explored = false;
	private boolean enemyOccupied = false;
	
	private List<Enemy> enemies = new ArrayList<Enemy>();
	private LootableObject loot;
	private int level;
	private long startShakeTime = 0;
	private long shakeDuration = 0;
	// private Item[] lootContents;
	
	public DungeonRoom(RandomDungeon dungeon, int x, int y, int level){
		this.dungeon = dungeon;
		this.x = x;
		this.y = y;
		this.level = level;	
		System.out.println("Room created at " + x + "," + y);
	}

	public void createBranches() {
		int rand = (this.x==0 && this.y==0) ? new Random().nextInt(13) + 3 : //range 3-15
			new Random().nextInt(this.x+this.y+14) + 1;
		//range depends on distance from center...farther rooms are more likely to be dead ends
		if (rand > 15)
			return;		//no doors=dead end--likely place for important things
		if (rand >= EAST_EXIT){
			this.east = this.dungeon.addRoom(this.x+1, this.y, this.level);
			if (this.east != null){			
				this.east.addDoor(this, 3);
			}
			rand -= EAST_EXIT;
		}
		if (rand >= NORTH_EXIT){
			this.north = this.dungeon.addRoom(this.x, this.y-1, this.level);
			if (this.north != null){
				this.north.addDoor(this, 2);
			}
			rand -= NORTH_EXIT;
		}
		if (rand >= WEST_EXIT){
			this.west = this.dungeon.addRoom(this.x-1, this.y, this.level);
			if (this.west != null){
				this.west.addDoor(this, 1);
			}
			rand -= WEST_EXIT;
		}
		if (rand >= SOUTH_EXIT){
			this.south = this.dungeon.addRoom(this.x, this.y+1, this.level);
			if (this.south != null){
				this.south.addDoor(this, 0);
			}
			rand -= SOUTH_EXIT;
		} if (rand > 0) System.out.println("failed");
		populateRoom(this.level);
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
		for (int i = 0; i < num; i++){
			level += random.nextInt(2);
			level -= random.nextInt(1);
			enemies.add(new Enemy(this, "Slime ".concat(Integer.toString(i)), level+1));
			this.enemyOccupied = true;
		}
		
		num = random.nextInt(100);
		if (num <= (LOOT_CHANCE / (getNumberOfExits() + 1))) {
			this.loot = new LootableObject();
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
		if (this.west != null) exits += WEST_EXIT;
		if (this.north != null) exits += NORTH_EXIT;
		if (this.south != null) exits += SOUTH_EXIT;
		if (this.east != null) exits += EAST_EXIT;
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
	
	public void init(Renderer renderer) {
		renderer.init(this);
		if (loot != null) {
			renderer.init(loot);
			renderer.init(loot.getLootWindow());
		}
//		for (Enemy enemy : enemies) {
//			renderer.init(enemy);
//		}
	}
	
	public void render(Renderer renderer) {
//		if (this.shakeDuration > 0 && System.currentTimeMillis() - this.startShakeTime <= this.shakeDuration) {
//			if (System.currentTimeMillis()%8 < 4) {
//				renderer.shakeCamera(System.currentTimeMillis() - this.startShakeTime);
//			} 
//			else {
//				renderer.shakeCamera(this.startShakeTime - System.currentTimeMillis());
//			}
//		}
		renderer.render(this);
		renderer.render(loot);
//		for (Enemy enemy : enemies) {
//			renderer.render(enemy);
//		}
	}
	public int getVao() {
		return vao;
	}
	public int getVbo() {
		return vbo;
	}
	public int getEbo() {
		return ebo;
	}
	public int getTexture() {
		return texture;
	}

	@Override
	public float[] init(int vao) {
		if (texture == 0) texture = loadTexture();
		this.vao = vao;
		vbo = glGenBuffers();
		ebo = glGenBuffers();
		
		return new float[]{
				1.0f,  1.0f, 0f,	0f,0f,0f,	1f, 1f,
				1.0f, -1.0f, 0f,	0f,0f,0f,	1f, 0f,
				
				-1.0f, 1.0f, 0f,	0f,0f,0f,	0f, 1f,
				-1.0f, -1.0f, 0f,	0f,0f,0f,	0f, 0f				
		};
	}

	private int loadTexture() {
		switch (this.getExits()) {
			case WEST_EXIT: return TextureLoader.loadTexture(TextureLoader.loadImage("/data/west.png"));
			case EAST_EXIT: return TextureLoader.loadTexture(TextureLoader.loadImage("/data/east.png"));
			case NORTH_EXIT: return TextureLoader.loadTexture(TextureLoader.loadImage("/data/north.png"));
			case SOUTH_EXIT: return TextureLoader.loadTexture(TextureLoader.loadImage("/data/south.png"));
			case (EAST_EXIT | WEST_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/westeast.png"));
			case (NORTH_EXIT | WEST_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/northwest.png"));
			case (SOUTH_EXIT | WEST_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/southwest.png"));
			case (SOUTH_EXIT | EAST_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/southeast.png"));
			case (NORTH_EXIT | EAST_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/northeast.png"));
			case (SOUTH_EXIT | NORTH_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/northsouth.png"));
			case (NORTH_EXIT | WEST_EXIT | SOUTH_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/noeast.png"));
			case (SOUTH_EXIT | EAST_EXIT | WEST_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/nonorth.png"));
			case (SOUTH_EXIT | EAST_EXIT | NORTH_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/nowest.png"));
			case (NORTH_EXIT | EAST_EXIT | WEST_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/nosouth.png"));
			case (SOUTH_EXIT | EAST_EXIT | NORTH_EXIT | WEST_EXIT): return TextureLoader.loadTexture(TextureLoader.loadImage("/data/allexits.png"));
			default : return 0;
		}
	}

	public String toString(){
		StringBuilder str = new StringBuilder();
		if (this.south != null) str.append('S');
		else str.append(' ');
		if (this.west != null) str.append('W');
		else str.append(' ');
		if (this.north != null) str.append('N');
		else str.append(' ');
		if (this.east != null) str.append('E');
		else str.append(' ');
		return str.toString();
	}

	public void startShakeRoom(long duration) {
		if (System.currentTimeMillis() - this.startShakeTime >= this.shakeDuration) {
			this.startShakeTime = System.currentTimeMillis();
			this.shakeDuration = duration;
		}
	}

	public Boolean activateObject(Vector2f playerPosition) {
		if (loot == null) return false;
		if (playerPosition.x > loot.position.x - 0.2f && playerPosition.y > loot.position.y - 0.2f) {
			// lootContents = loot.getContents();
			return true;
		}
		return false;
	}

	public void renderLootWindow(Renderer renderer) {
		renderer.render(loot.getLootWindow());
	}

	@Override
	public int getNumVertices() {
		return 6;
	}
	
}
