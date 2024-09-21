package core;

public class Platform extends AbstractStaticObject {
	
	public Platform(int x, int y, int width, int height, String tag) {
		super(x, y, width, height, tag);
	}
	
	public Platform(Platform p)  {
		super(p.getPosX(), p.getPosY(), p.getWidth(), p.getHeight(), p.getObjectTag());
	}
	
	public boolean equals(Platform p)  {
		return posX == p.getPosX() && posY == p.getPosY() && tag.equals(p.getObjectTag());
	}
}
