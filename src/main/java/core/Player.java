package core;

import interfaces.Direction;

public class Player extends AbstractCharacter {
	String name;
	
	public Player(String tag, int posX, int posY, String character)  {
		//supp width = 100, height = 100 (da vedere le img poi)
		//posx, posy, width, height, objectag, direction, speed, type
		super(posX, posY, 96, 96, tag, Direction.STOP, character);
		name = character;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void update()  {		
		super.update();
	}	
}