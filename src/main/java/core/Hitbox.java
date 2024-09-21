package core;

public class Hitbox extends AbstractStaticObject {
	AbstractDynamicObject object;
	int leftDifference;
	int topDifference;
	int targetX;
	int targetY;
	
	public Hitbox(int left, int top, int width, int height, AbstractDynamicObject obj)  {
		super(0, 0, width, height, "");
		
		leftDifference = left;
		topDifference = top;
		object = obj;
		
		update();		
	}
	
	public void update()  {
		targetX = object.getTargetX() + leftDifference;
		targetY = object.getTargetY() + topDifference;
		posX = object.getPosX() + leftDifference;
		posY = object.getPosY() + topDifference;
	}
	
	public int getTargetX()  {
		return targetX;
	}
	
	public int getTargetY()  {
		return targetY;
	}
	
	public int getLeftDifference()  {
		return leftDifference;
	}
	
	public int getTopDifference()  {
		return topDifference;
	}
}
