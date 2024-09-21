package interfaces;

public class Position  {
    public int posX;
    public int posY;
    
    public Position()  {
    	posX = 0;
    	posY = 0;
    }
    
    public Position(Position position)  {
    	posX = position.getPosX();
    	posY = position.getPosY();
    }
    
    public Position(int x, int y)  {
    	posX = x;
    	posY = y;
    }

    public int getPosX()  {
    	return posX;
    }
    
    public int getPosY()  {
    	return posY;
    }

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public void setPosXY(int posX, int posY)  {
		this.posX = posX;
		this.posY = posY;
	}
}
