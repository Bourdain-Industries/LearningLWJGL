package game;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;

import game.framework.Animation;

@SuppressWarnings("serial")
public class StartingClass extends Applet implements Runnable, KeyListener {

	enum State {
		Running, Dead, Alive, Pause
	}

	private static Player player;
	public static int score = 0;
	private Font font = new Font(null, Font.BOLD, 30);

	private Image image, currentSprite, character, character2, character3,
			background, heliboy, heliboy2,
			heliboy3, heliboy4, heliboy5;
	public static Image tilegrassTop, tilegrassBot, tilegrassLeft,
			tilegrassRight, tiledirt;

	private URL base;
	private Graphics second;
	private static Background bg1;
	private static RandomDungeon dungeon1;
	private Animation anim, hanim;

	private ArrayList<Tile> tilearray = new ArrayList<Tile>();
	private State GameState = State.Running;
	private State PlayerState = State.Alive;

	@Override
	public void destroy() {
		System.out.print("Exit");
		super.destroy();
	}

	@Override
	public void init() {
		setSize(800, 480);
		setBackground(Color.BLACK);
		setFocusable(true);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("The Alchemist Alpha");
		addKeyListener(this);
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Image Setups
		character = getImage(base, "data/character.png");
		character2 = getImage(base, "data/character2.png");
		character3 = getImage(base, "data/character3.png");

		getImage(base, "data/down.png");
		getImage(base, "data/jumped.png");

		heliboy = getImage(base, "data/heliboy.png");
		heliboy2 = getImage(base, "data/heliboy2.png");
		heliboy3 = getImage(base, "data/heliboy3.png");
		heliboy4 = getImage(base, "data/heliboy4.png");
		heliboy5 = getImage(base, "data/heliboy5.png");

		background = getImage(base, "data/allexits.png");
		tiledirt = getImage(base, "data/tiledirt.png");
		tilegrassTop = getImage(base, "data/tilegrasstop.png");
		tilegrassBot = getImage(base, "data/tilegrassbot.png");
		tilegrassLeft = getImage(base, "data/tilegrassleft.png");
		tilegrassRight = getImage(base, "data/tilegrassright.png");

		anim = new Animation();
		anim.addFrame(character, 1250);
		anim.addFrame(character2, 50);
		anim.addFrame(character3, 50);
		anim.addFrame(character2, 50);

		hanim = new Animation();
		hanim.addFrame(heliboy, 100);
		hanim.addFrame(heliboy2, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy5, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy2, 100);

		currentSprite = anim.getImage();
		dungeon1 = new RandomDungeon(1);
		player = new Player(dungeon1.getEntrance(), "Apprentice", 1);

	}

	@Override
	public void start() {

		bg1 = new Background(0, 0);

//		try {
//			loadMap("data/map1.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		Thread thread = new Thread(this);
		thread.start();
	}

//	private void loadMap(String filename) throws IOException {
//		ArrayList lines = new ArrayList();
//		int width = 0;
//		int height = 0;
//
//		BufferedReader reader = new BufferedReader(new FileReader(filename));
//		while (true) {
//			String line = reader.readLine();
//			// no more lines to read
//			if (line == null) {
//				reader.close();
//				break;
//			}
//
//			if (!line.startsWith("!")) {
//				lines.add(line);
//				width = Math.max(width, line.length());
//
//			}
//		}
//		height = lines.size();
//
//		for (int j = 0; j < 12; j++) {
//			String line = (String) lines.get(j);
//			for (int i = 0; i < width; i++) {
//				System.out.println(i + "is i ");
//
//				if (i < line.length()) {
//					char ch = line.charAt(i);
//					Tile t = new Tile(i, j, Character.getNumericValue(ch));
//					tilearray.add(t);
//				}
//
//			}
//		}
//
//	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}

	@Override
	public void run() {
		while (true) {
			currentSprite = anim.getImage();
			if (player.getLocation().isOccupied() && player.isAlive()) {
				if (!player.getLocation().combat(player)) {
					PlayerState = State.Dead;
				}
			}
			
			updateTiles();
			bg1.update();
			animate();
			repaint();

			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);

	}

	public void updateBackground(){
		int exits = player.getLocation().getExits();
		switch (exits){
		case 1:
			background = getImage(base, "data/south.png");
			break;
		case 10:
			background = getImage(base, "data/west.png");
			break;
		case 11:
			background = getImage(base, "data/southwest.png");
			break;
		case 100:
			background = getImage(base, "data/north.png");
			break;
		case 101:
			background = getImage(base, "data/northsouth.png");
			break;
		case 110:
			background = getImage(base, "data/northwest.png");
			break;
		case 111:
			background = getImage(base, "data/noeast.png");
			break;
		case 1000:
			background = getImage(base, "data/east.png");
			break;
		case 1001:
			background = getImage(base, "data/southeast.png");
			break;
		case 1010:
			background = getImage(base, "data/westeast.png");
			break;
		case 1100:
			background = getImage(base, "data/northeast.png");
			break;
		case 1011:
			background = getImage(base, "data/nonorth.png");
			break;
		case 1101:
			background = getImage(base, "data/nowest.png");
			break;
		case 1110:
			background = getImage(base, "data/nosouth.png");
			break;
		case 1111:
			background = getImage(base, "data/allexits.png");
			break;
		}
	}
	
	public void animate() {
		anim.update(10);
		hanim.update(50);
	}

	public void paint(Graphics g) {
		if (PlayerState == State.Alive) {
			g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
			paintTiles(g);
			g.drawImage(currentSprite, player.getCenterX() - 61,
					player.getCenterY() - 63, this);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString(Integer.toString(player.getLevel()), 740, 30);
		} else if (PlayerState == State.Dead) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 480);
			g.setColor(Color.WHITE);
			g.drawString("You have been slain!", 280, 240);
			g.drawString("[R]etry or [Q]uit?", 290, 270);
			g.drawString(Integer.toString(player.getLevel()), 740, 30);

		}
		if (GameState == State.Pause) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, 800, 480);
			g.setColor(Color.BLACK);
			g.drawString("GAME PAUSED", 280, 240);
			g.drawString("[Q]uit?", 340, 270);
			g.drawString(Integer.toString(player.getLevel()), 740, 30);
		}
	}

	private void updateTiles() {

		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			t.update();
		}

	}

	private void paintTiles(Graphics g) {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY(), this);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			player.move(player.getLocation().getNorth());
			updateBackground();
			break;

		case KeyEvent.VK_DOWN:
			player.move(player.getLocation().getSouth());
			updateBackground();
			break;

		case KeyEvent.VK_LEFT:
			player.move(player.getLocation().getWest());
			updateBackground();
			break;

		case KeyEvent.VK_RIGHT:
			player.move(player.getLocation().getEast());
			updateBackground();
			break;
			
		case KeyEvent.VK_R:
			if (PlayerState == State.Dead){
				player = new Player(dungeon1.getEntrance(), player.getName(), player.getLevel());
				PlayerState = State.Alive;
			}
			break;

		case KeyEvent.VK_Q:
			if (PlayerState == State.Dead || GameState == State.Pause) {
				destroy();
				System.exit(0);
			}
			break;
			
		case KeyEvent.VK_ESCAPE:
			if (GameState == State.Running){
				GameState = State.Pause;
			}
			else
				GameState = State.Running;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
//		case KeyEvent.VK_CONTROL:
//			player.setReadyToFire(true);
//			break;
//
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyCode()) {
		}
	}

	public static Background getBg1() {
		return bg1;
	}

	public static Player getRobot() {
		return player;
	}

}
