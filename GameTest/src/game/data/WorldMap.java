package game.data;

import java.util.Random;

public class WorldMap {
	
	private int[][] map;
	private final static int DEFAULT_SIZE = 10;
	
	public WorldMap(){
		this(DEFAULT_SIZE);
	}
	
	public WorldMap(int size){
		map = new int[size][size];
		setBorder();
		initializeMid();
	}
	
	private void initializeMid() {
		Random rand = new Random();
		for (int i = 1; i < map.length-1; i++){
			for (int j = 1; j < map.length-1; j++){
				map[i][j] = rand.nextInt(9);
			}
		}
	}
	
	private void setBorder(){
		for (int i = 0; i < map.length; i++){
			map[0][i] = 9;
			map[i][0] = 9;
			map[map.length-1][i] = 9;
			map[i][map.length-1] = 9;
		}
	}

	public void displayMap(){
		for (int y = 0; y < map.length; y++){
			for (int x = 0; x < map.length; x++){
				System.out.print(map[x][y] + " ");
			}
			System.out.println();
		}
	}

}
