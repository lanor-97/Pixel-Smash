package core;

import interfaces.PowerUpType;

public class PowerUp extends AbstractDynamicObject {
	
	PowerUpType type;
	
	public PowerUp(int x, int y, PowerUpType type)  {
		super(x, y, 35, 35, "powerup");
		this.type = type;
	}
	
	public PowerUpType getType()  {
		return type;
	}
	
	public void update()  {
		posY = targetY;
		targetY += speedY;
	}
}
