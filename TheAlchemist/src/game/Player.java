package game;


public class Player extends Unit {

	private final int MOVESPEED = 5;
	private int xp;
	private int centerX = 400;
	private int centerY = 240;
	private int speedX = 0;
	private int speedY = 0;
	private DungeonRoom lastLocation;
	private boolean movingRight = false;
	private boolean movingLeft = false;
	private boolean movingUp = false;
	private boolean movingDown = false;

	public Player(DungeonRoom location, String name, int level) {
		super(location, name, level, 5);
		lastLocation = location;
		location.setExplored(true);
	}

	public void gainXP(int gainedXP) {
		xp += gainedXP;
		if (xp >= 100) {
			xp -= 100;
			gainLevel();
		}
	}
	
	public void update() {
		
		centerX += speedX;
		centerY += speedY;

		// Prevents going beyond X coordinate of 0 unless there is a door
		if (centerX + speedX <= 90) {
			if (location.getWest() != null && centerY > 195 && centerY < 285) {
				moveLocation(location.getWest());
				centerX = 709;
			} else centerX = 91;
		}
		// Prevents going beyond X coordinate of 800 unless there is a door
		if (centerX + speedX >= 710) {
			if (location.getEast() != null && centerY > 195 && centerY < 285) {
				moveLocation(location.getEast());
				centerX = 91;
			} else centerX = 709;
		}

		// Prevents going beyond Y coordinate of 0 unless there is a door
		if (centerY + speedY <= 90) {
			if (location.getNorth() != null && centerX > 365 && centerX < 435) {
				moveLocation(location.getNorth());
				centerY = 389;
			} else centerY = 91;
		}
		// Prevents going beyond Y coordinate of 480 unless there is a door
		if (centerY + speedY >= 390) {
			if (location.getSouth() != null && centerX > 365 && centerX < 435) {
				moveLocation(location.getSouth());
				centerY = 91;
			} else centerY = 389;
		}
	}

	public void moveLocation(DungeonRoom location) {
		if (location == null) return;
		lastLocation = this.location;
		super.setLocation(location);
		System.out.println(Integer.toString(location.getExits()));
		location.setExplored(true);
	}
	
	public void runAway() {
		if (location == lastLocation) {
			int exits = location.getExits();
			if (exits%10 > 0) lastLocation = location.getSouth();
			else if (exits%100 > 0) lastLocation = location.getWest();
			else if (exits%1000 > 0) lastLocation = location.getNorth();
			else lastLocation = location.getEast();
		}
		moveLocation(lastLocation);
	}

	protected void gainLevel() {
		super.gainLevel();
	}

	@Override
	public int getTotalAttack() {
		return super.getTotalAttack() + 5;
	}
	
	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void moveRight() {
		speedX = MOVESPEED;
	}

	public void moveLeft() {
		speedX = -MOVESPEED;		
	}

	public void moveUp() {
		speedY = -MOVESPEED;
	}

	public void moveDown() {
		speedY = MOVESPEED;

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

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}
	
	public boolean isMovingUp() {
		return movingUp;
	}

	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

	public boolean isMovingDown() {
		return movingDown;
	}

	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}

}
