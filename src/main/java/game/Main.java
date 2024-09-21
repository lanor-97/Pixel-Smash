package game;

import managers.*;

public class Main {
	//crea il frame col relativo titolo, setta la X come chiusura, setta il
	//pannello appena creato
	public static WorldManager wm;
	public static GraphicManager graphics;
	public static SoundManager sound;
	//public static MainMenu menu;
	
	public static void main(String args[])  {
		
		sound = new SoundManager();
		wm = new WorldManager(1200, 800);
		
		graphics = new GraphicManager(wm);
		
		
//		 wm.startServer();
//		 wm.startClient();

		 
		
	}
}
