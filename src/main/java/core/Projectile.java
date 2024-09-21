package core;

import interfaces.Direction;

public class Projectile extends AbstractDynamicObject {
	
	AbstractCharacter shooter;
	private int power;

	public Projectile(int posX, int posY, int power, AbstractCharacter shooter, Direction d)  {
		
		super(posX, posY, 0, 0, "projectile");
		if(power > 10)  {
			width = 44;
			height = 34;
		}
		else  {
			width = 17;
			height = 7;
		}
		
		this.power = power;
		this.shooter = shooter;
		directionX = d;
	}
	
	public boolean isBig()  {
		return power > 10;
	}
	
	public void update()  {
		int speed = 6;
		
		if(isBig())
			speed = 15;
		
		if(directionX == Direction.RIGHT)
			posX += speed;
		else
			posX -= speed;
	}
	
	public boolean outOfWorld(int width, int height)  {
		if(posX > width || posX + this.width < 0)
			return true;
		
		if(posY > height || posY + this.height < 0)
			return true;
		
		return false;
	}
	
	public AbstractCharacter getShooter()  {
		return shooter;
	}
	
	public int getPower()  {
		return power;
	}
}
