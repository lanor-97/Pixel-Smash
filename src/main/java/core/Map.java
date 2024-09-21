package core;

import java.util.*;
import java.io.*;
import menus.MapEditorMenu;

public class Map {
	
	private LinkedList<AbstractStaticObject> objects;
	private String name;
	private String background;
	
	private MapEditorMenu mapEditor;
	
	
	public Map()  {
		objects = new LinkedList<AbstractStaticObject>();
		mapEditor = new MapEditorMenu();
	}
	
	public void setMap(String name)  {
		objects.clear();		
		this.name = name;
		
		if(name.equals("Custom"))  {
			mapEditor.setMap(this);
			return;
		}
		
		String mapPath = "resources/maps/" + name + ".txt";
		readMap(mapPath);
	}
	
	public String getName() {
		return name;
	}
	
	public LinkedList<AbstractStaticObject> getObjects()  {
		return objects;
	}
	
	public String getBackground()  {
		return background;
	}
	
	private void readMap(String path)  {
		
		Scanner scan;
		try  {
			scan = new Scanner(new File(path));
			if(scan.hasNextLine())  {
				background = scan.nextLine();
			}
			
			while(scan.hasNextLine())  {
				String line = scan.nextLine();
				String words[] = line.split(" ");
				
				String object = words[0];
				int x = Integer.parseInt(words[1]);
				int y = Integer.parseInt(words[2]);
				int width = Integer.parseInt(words[3]);
				int height = Integer.parseInt(words[4]);
				
				Platform p = new Platform(x, y, width, height, object);
				objects.add(p);
			}
			scan.close();
		} catch(FileNotFoundException e)  {
			//GamePanel.changeStatus(Status.ERROR);
			System.out.println("[Error] File map not found");
			//GamePanel.setError("[Error] File map not found");
		}
	}
	
	public void setBackground(String background)  {
		this.background = background;
	}
	
	public void setObjects(LinkedList<AbstractStaticObject> objects)  {
		this.objects.clear();
		
		for(AbstractStaticObject object : objects)
			this.objects.add(object);
	}
	
	public MapEditorMenu getMapEditor()  {
		return mapEditor;
	}
}	