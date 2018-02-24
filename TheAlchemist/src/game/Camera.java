package game;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	
	private static final double PI = Math.PI;
	private final double LOOK_SENSITIVTY = 0.04;
	private final float MOVE_SPEED = 6f;
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
	private Vector3f worldUp = new Vector3f(0f, 1f, 0f);
	private Vector3f right = new Vector3f();
	private float curSpeedH;
	private float curSpeedV;
	
	public Camera(Boolean firstPerson) {
		this.firstPerson = firstPerson;
		this.position = new Vector3f(20f, 20f, 20f);
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
		
		updateCamera(dt);
	}

	private void updateCamera(double dt) {
		direction.set((float) (Math.cos(vertAngle) * Math.sin(horizAngle)), (float) Math.sin(vertAngle),
				(float) (Math.cos(vertAngle) * Math.cos(horizAngle)));
		direction.normalize().cross(worldUp, right);
		//right.set((float) Math.sin(horizAngle - PI/2f), 0, (float) Math.cos(horizAngle - PI/2f));
		if (curSpeedH != 0) {
			right.normalize().mul(curSpeedH*(float) dt, speed);				
			position.add(speed);
		}
		if (curSpeedV != 0) {
			position.add(direction.mul(curSpeedV * (float) dt, speed));
		}
		direction.add(position);
		position.mul(1f,0f,1f);
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

	public float getSpeedX() {
		return speed.x;
	}

	public float getSpeedZ() {
		return speed.z;
	}
	
	public void moveRight() {
		curSpeedH = MOVE_SPEED;
		this.movingRight = true;
	}

	public void moveLeft() {
		curSpeedH = -MOVE_SPEED;
		this.movingLeft = true;
	}

	public void moveForward() {
		curSpeedV = MOVE_SPEED;
		this.movingForward = true;
	}

	public void moveBackward() {
		curSpeedV = -MOVE_SPEED;
		this.movingBackward = true;
	}

	public void stopRight() {
		setMovingRight(false);
		stop();
	}

	public void stopLeft() {
		setMovingLeft(false);
		stop();
	}

	public void stopForward() {
		setMovingForward(false);
		stop();
	}

	public void stopBackward() {
		setMovingBackward(false);
		stop();
	}
	
	public void stopAll() {
		setMovingForward(false);
		setMovingBackward(false);
		setMovingRight(false);
		setMovingLeft(false);
		stop();
	}

	private void stop() {
		if (!isMovingRight() && !isMovingLeft()) {
			curSpeedH = 0;
		}

		if (!isMovingRight() && isMovingLeft()) {
			moveLeft();
		}

		if (isMovingRight() && !isMovingLeft()) {
			moveRight();
		}
		
		if (!isMovingForward() && !isMovingBackward()) {
			curSpeedV = 0;
		}

		if (!isMovingForward() && isMovingBackward()) {
			moveBackward();
		}

		if (isMovingForward() && !isMovingBackward()) {
			moveForward();
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
