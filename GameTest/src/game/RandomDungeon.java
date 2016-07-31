package game;


import java.util.Random;

public class RandomDungeon {
	
	private DungeonRoom[][] rooms;
	private int roomCount = 0;
	//size is initialized in range 7-12
	private int sizeX = new Random().nextInt(6) + 7;
	private int sizeY = new Random().nextInt(6) + 7;
	

	public RandomDungeon(int level){
		//make size 7x7, 9x9, or 11x11: odd number ensures centered starting room
		if (sizeX%2==0) { this.sizeX -= 1; }
		if (sizeY%2==0) { this.sizeY -= 1; }
		rooms = new DungeonRoom[this.sizeX][this.sizeY];
		this.addRoom(0, 0, level);
	}

	public DungeonRoom addRoom(int x, int y, int level){
		int midX = this.sizeX/2, midY = this.sizeY/2;
		if(Math.abs(x)<=midX && Math.abs(y)<=midY){
			if (this.rooms[midX+x][midY+y] == null){
				this.rooms[midX+x][midY+y] = new DungeonRoom(this, x, y, level);
				this.rooms[midX+x][midY+y].createBranches();
				this.roomCount++;
			}
			return rooms[midX+x][midY+y];
		}
		else return null;
	}

	public void printDungeon(){
		System.out.printf("This dungeon has %d rooms\n", this.roomCount);
		for (int y = 0; y < this.sizeY; y++){
			for (int x = 0; x < this.sizeX; x++){
				if (rooms[x][y]!=null && rooms[x][y].isExplored())
					System.out.print(rooms[x][y]);
				else System.out.print(" |    | ");
			}
			System.out.println();
		}
	}
	
	public DungeonRoom getEntrance(){
		return this.rooms[sizeX/2][sizeY/2];
	}
}
