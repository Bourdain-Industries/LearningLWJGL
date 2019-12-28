package game;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	
	private static final double PI = Math.PI;
	private final double LOOK_SENSITIVTY = 0.06;
	private final float MOVE_SPEED = 4.5f;
	private final float GRAVITY = -6f;
	private Vector3f position, direction, up;
	private Matrix4f projection, view;
	private Matrix4f model = new Matrix4f();
	private Matrix4f camera = new Matrix4f();
	private Boolean firstPerson;
	private double horizAngle = -PI, vertAngle = 0; 
	private float[] array = new float[16];
	private boolean movingRight;
	private boolean movingLeft;
	private boolean movingForward;
	private boolean movingBackward;
	private Vector3f speed = new Vector3f();
	private final Vector3f worldUp = new Vector3f(0f, 1f, 0f);
	private Vector3f right = new Vector3f();
	private float curSpeedX;
	private float curSpeedY = -2f;
	private float curSpeedZ;
	private boolean isJumped = false;
	private long jumpStarted;
	
	public Camera(Boolean firstPerson) {
		this.firstPerson = firstPerson;
		this.position = new Vector3f(5f, 0f, 5f);
		this.direction = new Vector3f();
		this.projection = new Matrix4f().perspective((float) Math.toRadians(60.0f) , 16f/9f, 0.1f, 100f);
		this.view = new Matrix4f();
		updateCamera(0);
	}
	
	public void update(double mouseX, double mouseY, double dt) {
		double horiz = 400 - mouseX, vert = 300 - mouseY;
		horizAngle += LOOK_SENSITIVTY * (horiz) * dt;
		vertAngle += LOOK_SENSITIVTY * (vert) * dt;
		
		if (vertAngle <= -PI/2) {
			vertAngle = -PI/2 + 0.001f;
		}
		else if (vertAngle >= PI/2) {
			vertAngle = PI/2 - 0.001f;
		}
		//controls how long jump lasts
		if (this.isJumped) {
			if (System.currentTimeMillis() - this.jumpStarted > 630) {
				if (System.currentTimeMillis() - this.jumpStarted > 700) {
					this.curSpeedY = GRAVITY;
				}
				else this.curSpeedY = 0;
			}			
		}
		else updateSpeed();
		
		updateCamera(dt);
	}

	private void updateCamera(double dt) {
		direction.set((float) (Math.cos(vertAngle) * Math.sin(horizAngle)), (float) Math.sin(vertAngle),
				(float) (Math.cos(vertAngle) * Math.cos(horizAngle)));
		direction.normalize().cross(worldUp, right);
		//right.set((float) Math.sin(horizAngle - PI/2f), 0, (float) Math.cos(horizAngle - PI/2f));
		//if (curSpeedX != 0) {
		right.normalize().mul(curSpeedX*(float) dt, speed);				
		position.add(speed);
		//}
		//if (curSpeedZ != 0) {
		position.add(direction.mul(curSpeedZ * (float) dt, speed));
		//}
		//position.mul(1f,0f,1f);
		position.add(worldUp.mul(curSpeedY * (float) dt, speed));
		direction.add(position);
		
		if (position.y < 0f) {
			position.y = 0f;
			this.isJumped = false;
		}
		
		this.view.setLookAt(position, direction, worldUp); //right.cross(direction));
	}

	public float[] getCamera() {
		camera.identity().mul(projection).mul(view).mul(model);
		return camera.get(array);
	}
	public void print() {
		System.out.println(position);
		System.out.println(direction);
		System.out.println(right);
		System.out.println(horizAngle + " " + vertAngle);
		System.out.println();
	}
	
	public void shake(long time) {
		Random rand = new Random();
		if (time % 2 == 0) {
			float eyeX = time%rand.nextFloat()/7, eyeY = time%rand.nextFloat()/8, eyeZ = 0.8f;
			float targetX = eyeX, targetY = eyeY, targetZ = 0f;
			float upX = time%rand.nextFloat()/5, upY = 1f-time%rand.nextFloat()/5, upZ = 0f;
			this.view.setLookAt(eyeX, eyeY, eyeZ, targetX, targetY, targetZ, upX, upY, upZ);
		}
	}
	
	public void reset() {
		if (this.firstPerson) {
			this.up = new Vector3f(0,0,1f);
			this.projection.setLookAlong(new Vector3f(0,1f,0), this.up);
		}
		else {
			this.projection = new Matrix4f().setPerspective((float) Math.toRadians(60.0f) ,1f, 0f, 1f)
					.lookAt(0f, 0f, 1.70f, 0f, 0f, 0f, 0f, 1f, 0f);
			this.up = new Vector3f(0,1,0);
		}
	}

	public void startRight() {
		setMovingRight(true);
	}

	public void startLeft() {
		setMovingLeft(true);
	}

	public void startForward() {
		setMovingForward(true);
	}

	public void startBackward() {
		setMovingBackward(true);
	}
	
	public void startJump() {
		if (!this.isJumped) {
			curSpeedY = MOVE_SPEED;
			this.jumpStarted = System.currentTimeMillis();
			this.isJumped = true;
		}
	}

	public void stopRight() {
		setMovingRight(false);
	}

	public void stopLeft() {
		setMovingLeft(false);
	}

	public void stopForward() {
		setMovingForward(false);
	}

	public void stopBackward() {
		setMovingBackward(false);
	}
	
	public void stopAll() {
		setMovingForward(false);
		setMovingBackward(false);
		setMovingRight(false);
		setMovingLeft(false);
		updateSpeed();
	}

	private void updateSpeed() {		
		if (!isMovingRight() && !isMovingLeft()) {
			curSpeedX = 0;
		}

		if (isMovingRight() && isMovingLeft()) {
			curSpeedX = 0;
		}

		if (!isMovingRight() && isMovingLeft()) {
			curSpeedX = -MOVE_SPEED;
		}

		if (isMovingRight() && !isMovingLeft()) {
			curSpeedX = MOVE_SPEED;
		}
		
		if (!isMovingForward() && !isMovingBackward()) {
			curSpeedZ = 0;
		}

		if (isMovingForward() && isMovingBackward()) {
			curSpeedZ = 0;
		}

		if (!isMovingForward() && isMovingBackward()) {
			curSpeedZ = -MOVE_SPEED;
		}

		if (isMovingForward() && !isMovingBackward()) {
			curSpeedZ = MOVE_SPEED;
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
	
	public boolean isMovingForward() {
		return movingForward;
	}

	private void setMovingForward(boolean movingForward) {
		this.movingForward = movingForward;
	}

	public boolean isMovingBackward() {
		return movingBackward;
	}

	private void setMovingBackward(boolean movingBackward) {
		this.movingBackward = movingBackward;
	}

}
