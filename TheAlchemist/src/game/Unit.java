package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

import game.framework.Animation;

//import java.util.List;
//import java.util.Map;

public abstract class Unit {
	
	private final float MOVE_SPEED = 0.02f;
	private String name;
	protected float centerX = 0;
	protected float centerY = 0;
	protected float speedX = 0;
	protected float speedY = 0;
	private int currentHealth, maxHealth;
	private int baseAttack, totalAttack;
	private int baseDefense, totalDefense;
	private boolean alive;
	private int level;
	protected DungeonRoom location;
	private boolean movingRight = false;
	private boolean movingLeft = false;
	private boolean movingUp = false;
	private boolean movingDown = false;
	protected Animation anim;
	private int vao, vbo, ebo;
	protected float[] vertices;
	
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
		this.anim = new Animation();
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
	
	public float getCenterX() {
		return centerX;
	}

	public float getCenterY() {
		return centerY;
	}
	
	public float getSpeedX() {
		return speedX;
	}

	public float getSpeedY() {
		return speedY;
	}
	
	public void moveRight() {
		speedX = MOVE_SPEED;
		this.movingRight = true; 
	}

	public void moveLeft() {
		speedX = -MOVE_SPEED;
		this.movingLeft = true;
	}

	public void moveUp() {
		speedY = MOVE_SPEED;
		this.movingUp = true;
	}

	public void moveDown() {
		speedY = -MOVE_SPEED;
		this.movingDown = true;
	}

	public void stopRight() {
		setMovingRight(false);
		stop();
	}

	public void stopLeft() {
		setMovingLeft(false);
		stop();
	}

	public void stopUp() {
		setMovingUp(false);
		stop();
	}

	public void stopDown() {
		setMovingDown(false);
		stop();
	}
	
	public void stopAll() {
		setMovingUp(false);
		setMovingDown(false);
		setMovingRight(false);
		setMovingLeft(false);
		stop();
	}

	private void stop() {
		if (!isMovingRight() && !isMovingLeft()) {
			speedX = 0;
		}

		if (!isMovingRight() && isMovingLeft()) {
			moveLeft();
		}

		if (isMovingRight() && !isMovingLeft()) {
			moveRight();
		}
		
		if (!isMovingUp() && !isMovingDown()) {
			speedY = 0;
		}

		if (!isMovingUp() && isMovingDown()) {
			moveDown();
		}

		if (isMovingUp() && !isMovingDown()) {
			moveUp();
		}

	}
	
	public boolean isMovingRight() {
		return movingRight;
	}

	private void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	private void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}
	
	public boolean isMovingUp() {
		return movingUp;
	}

	private void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

	public boolean isMovingDown() {
		return movingDown;
	}

	private void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}
	
	public void init() {
		FloatBuffer vBuffer = MemoryUtil.memAllocFloat(vertices.length);
		vBuffer.put(vertices).flip();
	
		int[] indices = new int[] {
				0, 1, 2,
				1, 2, 3
		};
		IntBuffer iBuffer = MemoryUtil.memAllocInt(indices.length);
		iBuffer.put(indices).flip();
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		        
		vbo = glGenBuffers();
		ebo = glGenBuffers();
	
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
	    glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
	    MemoryUtil.memFree(vBuffer);
	
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
	    glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);
	    MemoryUtil.memFree(iBuffer);
	
		// vertex attribute
	    glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
	    // color attribute
	    glVertexAttribPointer(1, 3, GL_FLOAT, false, 32, 12);
	    // texture coord attribute
	    glVertexAttribPointer(2, 2, GL_FLOAT, false, 32, 24);
	
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	    glBindVertexArray(0);
	}
	
	protected void update() {
		anim.update(10);
		
		vertices[0] = centerX - 0.1f;
		vertices[8] = centerX - 0.1f;
		vertices[16] = centerX + 0.1f;
		vertices[24] = centerX + 0.1f;
		vertices[1] = centerY - 0.1f;
		vertices[9] = centerY + 0.1f;
		vertices[17] = centerY - 0.1f;
		vertices[25] = centerY + 0.1f;
		
		glBindVertexArray(vao);
		FloatBuffer vBuffer = MemoryUtil.memAllocFloat(vertices.length);
		vBuffer.put(vertices).flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
	    glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
	    MemoryUtil.memFree(vBuffer);
	    
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	    glBindVertexArray(0);
	    
	}
	
	protected void render() {
		glBindVertexArray(vao);
		glBindTexture(GL_TEXTURE_2D,anim.getFrameTexture());
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
	    glEnableVertexAttribArray(0);
	  	glEnableVertexAttribArray(1);
	  	glEnableVertexAttribArray(2);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	protected void cleanup() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
				
	}
}
