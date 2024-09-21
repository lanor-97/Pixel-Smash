package core;

import java.awt.Point;

import interfaces.*;

public abstract class AbstractStaticObject implements StaticObject {
	
	protected int posX;
	protected int posY;
	
	protected int height;
	protected int width;
	
	protected String tag;
	
	public AbstractStaticObject(int x, int y, int width, int height, String objectTag) {
		super();
		this.posX = x;
		this.posY = y;
		this.width = width;
		this.height = height;
		tag = objectTag;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public String getObjectTag() {
		return tag;
	}
	
	public void setPosX(int x) {
		this.posX = x;
	}
	
	public void setPosY(int y) {
		this.posY = y;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setObjectTag(String objectTag) {
		tag = objectTag;
	}

	public boolean hasPointInside(Point p)  {
		int x = (int) p.getX();
		int y = (int) p.getY();
		
		if(x > posX + width || x < posX)
			return false;
		
		if(y > posY + height || y < posY)
			return false;
		
		return true;
	}


}
