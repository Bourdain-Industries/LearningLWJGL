package game.data;

import game.DungeonRoom;
import game.Item;

public class LootableObject {
	
	private DungeonRoom room;
	private Item[] items;
	private String type;	//type of object i.e. bookshelf, desk, chest, etc.
	private int numItems = 0;
	
	public LootableObject(){
		
	}
}
