package menus;
import javax.swing.JFrame;

import interfaces.MenuOption;
import managers.SoundManager;
import managers.WorldManager;



public class MainMenu {
	
		private int cursorPosX;
		private int cursorPosY;
		private int optionsIndex;
		
		private int[] optionsPosY;
		
		private String currentMenu;
		
		
		private SingleplayerMenu singleplayerMenu;
		private MultiplayerMenu multiplayerMenu;
		
		private OptionsMenu optionsMenu;
		
		public MainMenu(WorldManager wm, JFrame frame) {
			
			cursorPosX = 282;
			optionsIndex = 0;
			optionsPosY = new int[5];
			
			optionsPosY[0] = 300;
			optionsPosY[1] = 385;
			optionsPosY[2] = 470;
			optionsPosY[3] = 540;
			optionsPosY[4] = 625;
			
			cursorPosY = optionsPosY[optionsIndex];
			
			singleplayerMenu = new SingleplayerMenu(wm);
			multiplayerMenu = new MultiplayerMenu(wm, frame);
			
			optionsMenu = new OptionsMenu();
			
			currentMenu = "single";

		}
			
		public void next() {
			
			SoundManager.PlaySound("menuSound");
			if(optionsIndex == 4)
				optionsIndex = 0;
			else
				optionsIndex++;
			
			cursorPosY = optionsPosY[ optionsIndex ];
		}
		
		public void previous() {
			
			SoundManager.PlaySound("menuSound");
			if(optionsIndex == 0)
				optionsIndex = 4;
			else
				optionsIndex--;
			
			cursorPosY = optionsPosY[ optionsIndex ];
		}
		
		public int getPosX() {
			return cursorPosX;
		}
		
		public int getPosY() {
			return cursorPosY;
		}
		
		public MenuOption getOption()  {
			
			if(optionsIndex == 0)
				return MenuOption.SINGLEPLAYER;
			else if(optionsIndex == 1) 
				return MenuOption.MULTIPLAYER;
			else if(optionsIndex == 2)
				return MenuOption.EDITOR;
			else if(optionsIndex == 3)
				return MenuOption.OPTIONS;
			else
				return MenuOption.EXIT;
		}
		
		public SingleplayerMenu getCurrentMenu2()  {
			if(currentMenu.equals("single"))
				return singleplayerMenu;
			
			return multiplayerMenu;
		}
		
		public MultiplayerMenu getMultiplayerMenu()  {
			return multiplayerMenu;
		}
		
		public void setMenu2(String mode)  {
			SoundManager.PlaySound("menuSound");
			currentMenu = mode;
		}
		
		public void clearMenu() {
			if(multiplayerMenu.getServer() != null)
				multiplayerMenu.getServer().stopServer();
			singleplayerMenu.clear();
			multiplayerMenu.clear();
		}
		
		public OptionsMenu getOptionsMenu()  {
			return optionsMenu;
		}
		
		
}		
		
