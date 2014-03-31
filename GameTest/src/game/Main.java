package game;

import java.util.InputMismatchException;
import java.util.Scanner;

import game.data.*;

public class Main {

	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		// WorldMap map = new WorldMap();
		// map.displayMap();
		// System.out.println();
		newGame(1);

	}

	private static void gameOver(int level) {
		System.out.print("Thanks for playing! [R]etry or [Q]uit? ");
		String entry = sc.next(".*[rRqQ].*");
		if (entry.contains("q")) {
			sc.close();
			System.exit(0);
		} else if (entry.contains("r")) {
			newGame(level);
		}
	}

	private static void newGame(int level) {
		RandomDungeon dungeon1 = new RandomDungeon(level);
		Player player = new Player(dungeon1.getEntrance(), "Player", level);
		// dungeon1.printDungeon();
		System.out.println("Welcome to the Dungeon!");
		while (true) {
			player.getLocation().setExplored(true);
			if (player.getLocation().isOccupied()) {
				if (!player.getLocation().combat(player)) {
					sc.close();
					gameOver(player.getLevel());
				}
			}
			byte numExits = player.getLocation().getNumberOfExits();
			int exit = 0;
			try {
				sc.reset();
				System.out
						.printf("The room you are in has %d exits.\n"
								+ "Enter the number that corresponds to the direction you want to go %s",
								player.getLocation().getNumberOfExits(),
								player.getLocation());
				exit = Integer.parseInt(sc.next());
			} catch (NumberFormatException e) {
				exit = 0;
			}
			player.move(exit);
		}
	}

}
