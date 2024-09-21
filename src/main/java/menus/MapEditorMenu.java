package menus;

import java.awt.Point;
import java.util.LinkedList;
import core.AbstractStaticObject;
import core.Map;
import core.Platform;

public class MapEditorMenu {
	
	private LinkedList<AbstractStaticObject> platforms;
	
	private AbstractStaticObject defaultPlatforms[];
	
	private boolean draggingDefaultPlatform;
	private boolean draggingPlacedPlatform;
	
	private Cursor cursorLeft;
	private Cursor cursorRight;

	private Platform currentPlatform;
	
	private int backgroundsIndex;
	
	public MapEditorMenu()  {
		platforms = new LinkedList<AbstractStaticObject>();
		
		this.cursorLeft = new Cursor(560, 0, 38, 46);
		this.cursorRight = new Cursor(640, 0, 38, 46);
		
		backgroundsIndex = 3;
		
		defaultPlatforms = new Platform[9];
		defaultPlatforms[0] = new Platform(50, 550, 95, 40, "CityPlatform190x80");
		defaultPlatforms[1] = new Platform(160, 550, 170, 50, "CityPlatform340x100");
		defaultPlatforms[2] = new Platform(50, 630, 405, 125, "CityPlatform810x250");
		defaultPlatforms[3] = new Platform(350, 550, 180, 57, "SandPlatform360x115");
		defaultPlatforms[4] = new Platform(480, 620, 185, 150, "SandPlatform370x300");
		defaultPlatforms[5] = new Platform(550, 540, 227, 76, "SandPlatform455x152");
		defaultPlatforms[6] = new Platform(920, 630, 160, 112, "TemplePlatform320x225");
		defaultPlatforms[7] = new Platform(700, 640, 190, 125, "TemplePlatform380x250");
		defaultPlatforms[8] = new Platform(800, 570, 277, 35, "TemplePlatform555x70");
		
		draggingDefaultPlatform = false;
		draggingPlacedPlatform = false;
	}
	
	public void setMap(Map map)  {
		if(backgroundsIndex == 0)
			map.setBackground("Sand");
		else if(backgroundsIndex == 1)
			map.setBackground("Temple");
		else if(backgroundsIndex == 2)
			map.setBackground("City");
		else if(backgroundsIndex == 3)
			map.setBackground("Custom");
		map.setObjects(platforms);
	}
	
	public AbstractStaticObject[] getDefaultPlatforms()  {
		return defaultPlatforms;
	}
	
	public LinkedList<AbstractStaticObject> getPlatforms()  {
		return platforms;
	}
	
	public int getBackgroundIndex()  {
		return backgroundsIndex;
	}
	
	public boolean isDraggingPlatform()  {
		return draggingDefaultPlatform || draggingPlacedPlatform;
	}
		
	public boolean select(Point point)  {
		
		if(cursorLeft.hasPointInside(point))  {
			if(backgroundsIndex - 1 >= 0)
				backgroundsIndex--;
			else
				backgroundsIndex = 3;
			
			return true;
		}
		else if(cursorRight.hasPointInside(point))  {
			if(backgroundsIndex + 1 <= 3)
				backgroundsIndex++;
			else
				backgroundsIndex = 0;
			
			return true;
		}
		
		for(AbstractStaticObject platform : defaultPlatforms)  {
			if(platform.hasPointInside(point))  {
				selectedDefaultPlatform((Platform) platform);
				return true;
			}
		}
		
		for(AbstractStaticObject platform : platforms)  {
			if(platform.hasPointInside(point))  {
				selectedPlacedPlatform((Platform) platform);
				return true;
			}
		}		
		
		return false;
	}
	
	public void releasePlatform()  {
		if(!isDraggingPlatform())
			return;
		
		draggingDefaultPlatform = false;
		draggingPlacedPlatform = false;
	}
	
	public void selectedDefaultPlatform(Platform p)  {
		draggingDefaultPlatform = true;
		
		int width = Integer.parseInt(p.getObjectTag().substring(p.getObjectTag().indexOf("Platform")+8, p.getObjectTag().indexOf("x")));
		int height = Integer.parseInt(p.getObjectTag().substring(p.getObjectTag().indexOf("x")+1, p.getObjectTag().length()));
		
		platforms.add(new Platform(p.getPosX(), p.getPosY(), width, height, p.getObjectTag()));
		currentPlatform = (Platform) platforms.getLast();
	}
	
	public void selectedPlacedPlatform(Platform p)  {
		draggingPlacedPlatform = true;
		
		currentPlatform = p;
	}
	
	public void drag(Point p)  {
		
		if(isDraggingPlatform())  {
			int x = ((int) p.getX()) - (platforms.getLast().getWidth() / 2);
			int y = ((int) p.getY()) - (platforms.getLast().getHeight() / 2);
			currentPlatform.setPosX(x);
			currentPlatform.setPosY(y);
		}
	}
	
	public void clear()  {
		platforms.clear();
		currentPlatform = null;
	}
	
	public int getCursorLeftPosX()  {
		return cursorLeft.getPosX();
	}
	
	public int getCursorLeftPosY()  {
		return cursorLeft.getPosY();
	}

	public int getCursorRightPosX()  {
		return cursorRight.getPosX();
	}
	
	public int getCursorRightPosY()  {
		return cursorLeft.getPosY();
	}
}
