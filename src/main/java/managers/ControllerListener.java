package managers;



import java.util.ArrayList;


import graphics.GamePanel;
import interfaces.GameState;
import interfaces.MenuOption;
import managers.WorldManager;
import menus.MainMenu;
import net.java.games.input.Controller;


public class ControllerListener extends Thread {
	
	private ControllerManager controller = null;
	private ArrayList<Boolean> keys;
	private WorldManager world;
	private MainMenu mainMenu;
	private GamePanel panel;

	
	public ControllerListener(WorldManager wm, GamePanel gp, MainMenu mainMenu) {
		super();
		this.world = wm;
		this.mainMenu = mainMenu;
		this.panel = gp;
		
		controller = new ControllerManager(Controller.Type.GAMEPAD);
		keys = new ArrayList<Boolean> ();
	}
	
	public void run() {
			
		if(controller.isControllerConnected())
			System.out.println(controller.getControllerName());
			
			
		if(controller.isControllerConnected())
			while(true){	
				
				//System.out.println("Connesso");
	            if( !controller.pollController() ) {
	            	System.out.println("Controller disconnected!");
	                break;
	            }
	            
	            //Analogici
//		            int xValuePercentageLeftcontroller = controller.getX_LeftJoystick_Percentage();
//		            int yValuePercentageLeftcontroller = controller.getY_LeftJoystick_Percentage();
//		            
//		            int xValuePercentageRightcontroller = controller.getX_RightJoystick_Percentage();
//		            int yValuePercentageRightcontroller = controller.getY_RightJoystick_Percentage();
	            
	            //Tasti
	            keys = controller.getButtonsValues();
	            

//		            for(int i =0; i<keys.size();i++) {
//		            	if(keys.get(i)) {
//		            		System.out.println(i);
//		            	}
//		            }
	            
	            verifyActions();
	            try {
	                Thread.sleep(120);
	            } catch (InterruptedException ex) {
	                System.out.println("disconnected");
	            }
		    } 
	}
	
	public ArrayList<Boolean> getKeys(){
		return keys;
	}
	
	public boolean isPressed(int i) {
		return keys.get(i);
	}
	public boolean isControllerConnected() {
		return controller.isControllerConnected();
	}
	
	public void verifyActions(){
		
			if(mainMenu.getOptionsMenu().isActive())  {
					
				
					if(keys.get(15)) {
						int chose = mainMenu.getOptionsMenu().chose();
						
						if(chose == 2 && panel.getStatus() == GameState.GAME)  {
							world.clear();
							panel.changeStatus(GameState.MENU1);
							mainMenu.getOptionsMenu().clear();
						}
						else if(chose == 3)  {
							panel.resize();
						}
					}
					
					if(keys.get(4)) {
						mainMenu.getOptionsMenu().previous();
					}
					
					if(keys.get(5)) {
						mainMenu.getOptionsMenu().next();
					}
					
					if(keys.get(16))  {
						mainMenu.getOptionsMenu().close();
					}
					
					return;
				}
				
				if(panel.getStatus() == GameState.MENU1)  {
	
					if(keys.get(4)) {
						mainMenu.previous();
					}
					if(keys.get(5)) {
						mainMenu.next();
					}
					if(keys.get(16))
						System.exit(0);
					
					
	//				//ENTER
					if(keys.get(15))  {
						if(mainMenu.getOption() == MenuOption.SINGLEPLAYER)  {
							panel.changeStatus(GameState.MENU2_SINGLEPLAYER);
							mainMenu.setMenu2("single");
						}
						else if(mainMenu.getOption() == MenuOption.MULTIPLAYER)  {
							panel.changeStatus(GameState.MENU2_MULTIPLAYER);
							mainMenu.setMenu2("multi");
						}	
						else if(mainMenu.getOption() == MenuOption.OPTIONS)  {
							mainMenu.getOptionsMenu().activate();
						}
						else if(mainMenu.getOption() == MenuOption.EXIT)  {
							System.exit(0);
						}
	//						
					}
				}
				else if(panel.getStatus() == GameState.MENU2_SINGLEPLAYER || panel.getStatus() == GameState.MENU2_MULTIPLAYER)  {
					
					//Left
					if(keys.get(6)) {
						mainMenu.getCurrentMenu2().previous();
					}
					
					//Right
					if(keys.get(7)) {
						mainMenu.getCurrentMenu2().next();
					}
					
					//ENTER
					if(keys.get(15))  {
						if(mainMenu.getCurrentMenu2().chose())  {
							panel.loadMap();
							panel.changeStatus(GameState.GAME);
						}
							
					}
					else if(keys.get(16))  {
						mainMenu.clearMenu();
						world.clear();
						panel.changeStatus(GameState.MENU1);
					}
					
				}
				else if(panel.getStatus() == GameState.GAME)  {
					
					if(world.isLevelFinished())  {
						//Enter
						if(keys.get(15))  {
							
							if(world.isBossRushMode())  {
								if(!world.changeLevel())  {	//returna false se è game over o ha vinto tutti i livelli
									world.clear();
									panel.changeStatus(GameState.MENU1);
								}
								else  {
									panel.loadMap();
								}
							}
							else  {
								world.clear();
								panel.changeStatus(GameState.MENU1);
							}
							return;
						}
					}
					
					//Up
					if(keys.get(15)) {
						if(world.getPlayer() != null) {
							//System.out.println("muovosu");
							world.getPlayer().moveUp();
							world.getPacket().setMoveUp(true);
							
						}
					}
					
					//Left
					if(keys.get(6)) {
						if(world.getPlayer() != null) {
							world.getPlayer().moveLeft();
							world.getPacket().setMoveLeft(true);
							
						}
					}
					else
						if(world.getPlayer() != null) {
							world.getPlayer().stopMoveLeft();
							world.getPacket().setMoveLeft(false);
							
						}
					
					//Down
					if(keys.get(5)) {
						world.getPlayer().defend();
						world.getPacket().setDefend(true);
						
						
					}
					else
						if(world.getPlayer() != null) {
							world.getPlayer().stopDefend();
							world.getPacket().setDefend(false);
							
						}
					
					//Right
					if(keys.get(7)) {
						if(world.getPlayer() != null) {
							world.getPlayer().moveRight();
							world.getPacket().setMoveRight(true);
							
						}
					}
					else
						if(world.getPlayer() != null) {
							world.getPlayer().stopMoveRight();
							world.getPacket().setMoveRight(false);
							
						}
					
					//Z
					if(keys.get(17)) {
						if(world.getPlayer() != null) {
							world.getPlayer().softAttack();
							world.getPacket().setSoftAttack(true);
							
						}
					}
					
					//X
					if(keys.get(18)) {
						if(world.getPlayer() != null) {
							world.getPlayer().chargeAttack();
							world.getPacket().setChargingAttack(true);
							
						}
					}
					else
						if(world.getPlayer() != null) {
							world.getPlayer().stopChargingAttack();
							world.getPacket().setChargingAttack(false);
							
						}
					
					//ESC
					if(keys.get(8))  {
						mainMenu.getOptionsMenu().activate();
					}
				}		
	}	
}
