package game;

import java.awt.Image;
import java.awt.Rectangle;

public class Tile {

	private int tileX, tileY, speedX, type;
	public Image tileImage;
	private Rectangle r;

//	private Player player = StartingClass.getRobot();
	private Background bg = StartingClass.getBg1();

	public Tile(int x, int y, int typeInt) {
		tileX = x * 40;
		tileY = y * 40;

		type = typeInt;
		r = new Rectangle();

		if (type == 5) {
			tileImage = StartingClass.tiledirt;
		} else if (type == 8) {
			tileImage = StartingClass.tilegrassTop;
		} else if (type == 4) {
			tileImage = StartingClass.tilegrassLeft;

		} else if (type == 6) {
			tileImage = StartingClass.tilegrassRight;

		} else if (type == 2) {
			tileImage = StartingClass.tilegrassBot;
		} else {
			type = 0;
		}

	}

	public void update() {
		speedX = bg.getSpeedX() * 5;
		tileX += speedX;
		r.setBounds(tileX, tileY, 40, 40);

//		if (r.intersects(Player.yellowRed) && type != 0) {
//			checkVerticalCollision(Player.rect, Player.rect2);
//			checkSideCollision(Player.rect3, Player.rect4, Player.footleft,
//					Player.footright);
//		}
	}

	public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}

//	public void checkVerticalCollision(Rectangle rtop, Rectangle rbot) {
//		if (rtop.intersects(r)) {
//			player.setSpeedY(0);
//		}
//
//		if (rbot.intersects(r) && type == 8) {
//			player.setSpeedY(0);
//			player.setCenterY(tileY - 63);
//		}
//	}

//	public void checkSideCollision(Rectangle rleft, Rectangle rright,
//			Rectangle leftfoot, Rectangle rightfoot) {
//		if (type != 5 && type != 2 && type != 0) {
//			if (rleft.intersects(r)) {
//				player.setCenterX(tileX + 102);
//
//				player.setSpeedX(0);
//
//			} else if (leftfoot.intersects(r)) {
//				player.setCenterX(tileX + 85);
//				player.setSpeedX(0);
//			}
//
//			if (rright.intersects(r)) {
//				player.setCenterX(tileX - 62);
//
//				player.setSpeedX(0);
//			}
//
//			else if (rightfoot.intersects(r)) {
//				player.setCenterX(tileX - 45);
//				player.setSpeedX(0);
//			}
//		}
//	}

}