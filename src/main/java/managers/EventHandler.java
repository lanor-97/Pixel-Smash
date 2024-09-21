package managers;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.*;

import javax.swing.SwingUtilities;

import interfaces.MenuOption;
import menus.MainMenu;
import menus.MapEditorMenu;
import interfaces.GameState;
import graphics.GamePanel;

public class EventHandler implements KeyListener, MouseListener, MouseMotionListener {
	
	private WorldManager world;
	private MainMenu mainMenu;
	private GamePanel panel;
	private ControllerListener controllerListener;
	private MapEditorMenu editor;
	
	public EventHandler(WorldManager world, GamePanel panel, MainMenu mainMenu) {
		this.world = world;
		this.mainMenu = mainMenu;
		this.panel = panel;
		this.editor = world.getMapEditor();
		
		if(System.getProperty("os.name").toLowerCase().indexOf("win") != 0)  {
			controllerListener = new ControllerListener(world,panel,mainMenu);
			controllerListener.start();
		}
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(mainMenu.getOptionsMenu().isActive())  {
			
			if(key == KeyEvent.VK_ENTER) {
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
			
			if(key == 'W' || key == KeyEvent.VK_UP ) {
				mainMenu.getOptionsMenu().previous();
			}
			
			if(key == 'S' || key == KeyEvent.VK_DOWN ) {
				mainMenu.getOptionsMenu().next();
			}
			
			if(key == KeyEvent.VK_ESCAPE)  {
				mainMenu.getOptionsMenu().close();
			}
			
			return;
		}
		
		if(panel.getStatus() == GameState.MENU1)  {
			
			//W o up
			if(key == 'W' || key == KeyEvent.VK_UP) {
				mainMenu.previous();
			}
			
			//S o down
			if(key == 'S' || key == KeyEvent.VK_DOWN) {
				mainMenu.next();
			}
			
			//ESC
			if(key == KeyEvent.VK_ESCAPE)
				System.exit(0);
			
			
			//ENTER
			if(key == KeyEvent.VK_ENTER)  {
				if(mainMenu.getOption() == MenuOption.SINGLEPLAYER)  {
					panel.changeStatus(GameState.MENU2_SINGLEPLAYER);
					mainMenu.setMenu2("single");
				}
				else if(mainMenu.getOption() == MenuOption.MULTIPLAYER)  {
					panel.changeStatus(GameState.MENU2_MULTIPLAYER);
					mainMenu.setMenu2("multi");
				}	
				else if(mainMenu.getOption() == MenuOption.EDITOR)  {
					panel.changeStatus(GameState.MENU_EDITOR);
				}
				else if(mainMenu.getOption() == MenuOption.OPTIONS)  {
					mainMenu.getOptionsMenu().activate();
				}
				else if(mainMenu.getOption() == MenuOption.EXIT)  {
					System.exit(0);
				}
					
			}
		}
		else if(panel.getStatus() == GameState.MENU2_SINGLEPLAYER || panel.getStatus() == GameState.MENU2_MULTIPLAYER)  {
			
			//A o left
			if(key == 'A' || key == KeyEvent.VK_LEFT) {
				mainMenu.getCurrentMenu2().previous();
			}
			
			//D o right
			if(key == 'D' || key == KeyEvent.VK_RIGHT) {
				mainMenu.getCurrentMenu2().next();
			}
			
			//ENTER
			if(key == KeyEvent.VK_ENTER)  {
				if(mainMenu.getCurrentMenu2().chose())  {
					panel.loadMap();
					panel.changeStatus(GameState.GAME);
				}
					
			}
			else if(key == KeyEvent.VK_ESCAPE)  {
				mainMenu.clearMenu();
				world.clear();
				panel.changeStatus(GameState.MENU1);
				world.getServer().stopServer();
			}
			
		}
		else if(panel.getStatus() == GameState.MENU_EDITOR)  {
			
			if(key == KeyEvent.VK_ESCAPE)
				panel.changeStatus(GameState.MENU1);
			
			if(key == KeyEvent.VK_BACK_SPACE)  {
				editor.clear();
				panel.clearEditor();
			}
		}
		else if(panel.getStatus() == GameState.GAME)  {
			
			if(world.isLevelFinished())  {
				if(key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE)  {
					
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
			
			//W o up
			if(key == 'W' || key == KeyEvent.VK_UP) {
				if(world.getPlayer() != null) {
					world.getPlayer().moveUp();
					world.getPacket().setMoveUp(true);
					
				}
			}
			
			//A o left
			if(key == 'A' || key == KeyEvent.VK_LEFT) {
				if(world.getPlayer() != null) {
					world.getPlayer().moveLeft();
					world.getPacket().setMoveLeft(true);
					
				}
			}
			
			//S o down
			if(key == 'S' || key == KeyEvent.VK_DOWN) {
				world.getPlayer().defend();
				world.getPacket().setDefend(true);
				
				
			}
			
			//D o right
			if(key == 'D' || key == KeyEvent.VK_RIGHT) {
				if(world.getPlayer() != null) {
					world.getPlayer().moveRight();
					world.getPacket().setMoveRight(true);
					
				}
			}
			
			//Z
			if(key == KeyEvent.VK_Z) {
				if(world.getPlayer() != null) {
					world.getPlayer().softAttack();
					world.getPacket().setSoftAttack(true);
					
				}
			}
			
			//X
			if(key == KeyEvent.VK_X) {
				if(world.getPlayer() != null) {
					world.getPlayer().chargeAttack();
					world.getPacket().setChargingAttack(true);
					
				}
			}
			
			//ESC
			if(key == KeyEvent.VK_ESCAPE)  {
				mainMenu.getOptionsMenu().activate();
			}
		}		
	}

	@Override
	public void keyReleased(KeyEvent e) {	
		
		int key = e.getKeyCode();
		
		if(panel.getStatus() == GameState.GAME)  {
			
			//D o right
			if(key == 'D' || key == KeyEvent.VK_RIGHT) {
				if(world.getPlayer() != null) {
					world.getPlayer().stopMoveRight();
					world.getPacket().setMoveRight(false);
					
				}


			}
			
			//A o left
			if(key == 'A' || key == KeyEvent.VK_LEFT) {
				if(world.getPlayer() != null) {
					world.getPlayer().stopMoveLeft();
					world.getPacket().setMoveLeft(false);
					
				}
			}
			
			//D o right
			if(key == 'D' || key == KeyEvent.VK_RIGHT) {
				if(world.getPlayer() != null) {
					world.getPlayer().stopMoveRight();
					world.getPacket().setMoveRight(false);
					
				}
			}
			
			//X
			if(key == 'X') {
				if(world.getPlayer() != null) {
					world.getPlayer().stopChargingAttack();
					world.getPacket().setChargingAttack(false);
					
				}

			}
			
			//S o down
			if(key == 'S' || key == 40) {
				world.getPlayer().stopDefend();
				world.getPacket().setDefend(false);
				
			}
		}
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(panel.getStatus() != GameState.MENU_EDITOR)
			return;
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub	
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		if(panel.getStatus() != GameState.MENU_EDITOR)
			return;
		
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, panel);
		p.setLocation(p.getX() * panel.getFractionPos(), p.getY() * panel.getFractionPos());
		editor.select(p);
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(panel.getStatus() != GameState.MENU_EDITOR)
			return;
		
		editor.releasePlatform();
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		if(panel.getStatus() != GameState.MENU_EDITOR)
			return;
		
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, panel);
		p.setLocation(p.getX() * panel.getFractionPos(), p.getY() * panel.getFractionPos());
		
		editor.drag(MouseInfo.getPointerInfo().getLocation());
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}