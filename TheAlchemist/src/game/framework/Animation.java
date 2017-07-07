package game.framework;

import java.util.ArrayList;

public class Animation {
	private ArrayList<AnimFrame> frames;
	private int currentFrame;
	private long animTime; // long takes up more memory than int but can hold
							// more accurate numbers.
	private long totalDuration;

	public Animation() {
		frames = new ArrayList<AnimFrame>();
		totalDuration = 0;

		synchronized (this) {
			animTime = 0;
			currentFrame = 0;
		}
	}

	public synchronized void addFrame(int textureID, long duration) {
		totalDuration += duration;
		frames.add(new AnimFrame(textureID, totalDuration));
	}

	public synchronized void update(long elapsedTime) {
		if (frames.size() > 1) {
			animTime += elapsedTime;
			if (animTime >= totalDuration) {
				animTime = animTime % totalDuration;
				currentFrame = 0;

			}

			while (animTime > getFrame(currentFrame).endTime) {
				currentFrame++;

			}
		}
	}

	public synchronized int getFrameTexture() {
		if (frames.size() == 0) {
			return 0;
		} else {
			return getFrame(currentFrame).textureID;
		}
	}

	private AnimFrame getFrame(int i) {
		return (AnimFrame) frames.get(i);
	}
	
	private class AnimFrame {

		   int textureID;
		   long endTime;

		   public AnimFrame(int textureID, long endTime) {
		      this.textureID = textureID;
		      this.endTime = endTime;
		   }
		}
}
