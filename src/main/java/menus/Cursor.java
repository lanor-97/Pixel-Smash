
package menus;

import core.AbstractStaticObject;
import interfaces.Position;

public class Cursor extends AbstractStaticObject {
	
	private boolean visible;

	
	public Cursor()  {
		super(0, 0, 0, 0, "Cursor");
	}
	
	public Cursor(Position position)  {
		super(position.getPosX(), position.getPosY(), 0, 0, "Cursor");
		
		visible = false;
	}
	
	public Cursor(int x, int y, int width, int height)  {
		super(x, y, width, height, "Cursor");
	}
	
	public void setPosXY(Position position)  {
		posX = position.getPosX();
		posY = position.getPosY();
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}



