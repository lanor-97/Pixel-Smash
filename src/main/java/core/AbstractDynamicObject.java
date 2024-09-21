package core;

import interfaces.*;

public abstract class AbstractDynamicObject extends AbstractStaticObject implements DynamicObject {
	protected Direction directionX;
	protected Direction directionY;
	protected int speedX;
	protected int maxSpeedY;
	protected double speedY;
	protected int targetX;
	protected int targetY;
	protected int armour;
	
	public AbstractDynamicObject(int x, int y, int width, int height, String objectTag) {
		super(x, y, width, height, objectTag);
		this.directionX = Direction.RIGHT;
		//this.directionY = Direction.STOP;
		speedX = 1;
		speedY = 0;
		maxSpeedY = 10;
	}
	
	public Direction getDirectionX()  {
		return directionX;
	}
	
	public Direction getDirectionY()  {
		return directionY;
	}
	
	public void rightCollision(AbstractStaticObject object)  {
		if(posY >=  object.getPosY() + object.getHeight() || posY + height <= object.getPosY())
			return;
		
		int difference = Math.abs(targetX + width - object.getPosX());
		if(targetX + width > object.getPosX() && difference <= speedX)
			targetX = object.getPosX() - width;
	}
	
	public void leftCollision(AbstractStaticObject object)  {
		if(posY >= object.getPosY() + object.getHeight() || posY + height <= object.getPosY())
			return;
		
		int difference = Math.abs(targetX - object.getPosX() - object.getWidth());
		if(targetX < object.getPosX() + object.getWidth() && difference <= speedX)
			targetX = object.getPosX() + object.getWidth();
	}
	
	public void topCollision(AbstractStaticObject object)  {
		if(posX > object.getPosX() + object.getWidth() || posX + width < object.getPosX())
			return;
		
		int difference = Math.abs(targetY - object.getPosY() - object.getHeight());
		if(targetY < object.getPosY() + object.getHeight() && difference <= speedY)
			targetY = object.getPosY() + height;
	}
	
	public boolean bottomCollision(AbstractStaticObject object)  {
		if(posX > object.getPosX() + object.getWidth() || posX + width < object.getPosX())
			return false;
		
		int difference = Math.abs(targetY + height - object.getPosY());
		if(targetY + height > object.getPosY() && difference <= speedY)  {
			targetY = object.getPosY() - height;
			return true;
		}
		return false;
	}
	
	public boolean collision(AbstractStaticObject object)  {
		if(posX > object.getPosX() + object.getWidth() || posX + width < object.getPosX())
			return false;
		
		if(posY >= object.getPosY() + object.getHeight() || posY + height <= object.getPosY())
			return false;
		
		return true;
	}
	
	public int getTargetX()  {
		return targetX;
	}
	
	public int getTargetY()  {
		return targetY;
	}
	
	public void setTargetX(int x) {
		this.targetX = x;
	}
	
	public void setTargetY(int y) {
		this.targetY = y;
	}
	
	public void applyGravity()  {
		double accelleration = 0.3;
		
		targetY = posY + (int) speedY;
		if(speedY + accelleration < maxSpeedY)
			speedY += accelleration;
	}
	
}
