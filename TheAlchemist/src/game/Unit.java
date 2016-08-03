package game;


//import java.util.List;
//import java.util.Map;

public abstract class Unit {
	
	private String name;
	private int currentHealth;
	private int maxHealth;
	private int baseAttack;
	private int totalAttack;
	private int baseDefense;
	private int totalDefense;
	private boolean alive;
	private int level;
	protected DungeonRoom location;
	
//	private List<Item> inventory;	//TODO: implement this
//	private Map<String, Item> equipment;	//TODO: implement this
	
	public Unit(DungeonRoom location, String name, int level, int bonusHealth){
		this.location = location;
		this.name = name;
		this.level = level;
		
		this.maxHealth = 10 + level/2 + bonusHealth;
		this.currentHealth = this.maxHealth;
		this.alive = true;
		this.baseAttack = 10 + level/3 + this.maxHealth/4;
		this.baseDefense = 10 + level/3 + this.baseAttack/4;
		this.totalAttack = this.baseAttack;
		this.totalDefense = this.baseDefense;
	}

	public int takeDamage(int incomingAttackPower){
		int damage = incomingAttackPower - this.totalDefense;
		damage = (damage < 1) ? 0 : damage;
		this.currentHealth -= damage;
		this.alive = (this.currentHealth > 0) ? true : false;
		return damage;
	}
	
	protected void gainLevel(){
		System.out.printf("%s has grown stronger! Level increased to %d\n", this.name,this.level);
		this.level++;
		this.maxHealth += this.level%2;
		this.currentHealth = this.maxHealth;
		this.baseAttack++;
		this.baseDefense++;
	}
	
	public int getTotalAttack(){
		return this.totalAttack;
	}
	
	public boolean isAlive(){
		return this.alive;
	}
	
	public int getLevel() {
		return level;
	}

	public String getName() {
		return this.name;
	}
	
	public DungeonRoom getLocation(){
		return this.location;
	}
	
	protected void setLocation(DungeonRoom room){
		this.location = room;
	}
}
