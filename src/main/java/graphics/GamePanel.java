package graphics;

import java.awt.*;
import java.io.*;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;

import managers.WorldManager;
import menus.MainMenu;
import menus.MultiplayerMenu;
import managers.GraphicManager;
import managers.SoundManager;
import core.Map;
import core.Player;
import core.PowerUp;
import core.Projectile;
import interfaces.Direction;
import interfaces.GameState;
import interfaces.PowerUpType;
import core.AbstractCharacter;
import core.AbstractStaticObject;

public class GamePanel extends JPanel {
	
	static final long serialVersionUID = 123;
	
	 public static GameState status;
	 
	 JFrame frame;
	 
	 Animator guy;
	 Animator gerry;
	 Animator knight;
	 Animator ninja;
	 Animator others;
	 Animator others2;
	 
	 Font font1;
	 Font font2;
	 Font font3;
	 Font font4;	//per scritta risoluzione
	 
	 Image backgroundImg;
	 Image platform;
	 
	 LinkedList<Image> staticImages;
	 
	 Image mainMenuImg = null;
	 Image singleplayerMenuImg = null;
	 Image multiplayerMenuImg = null;
	 Image pauseMenu = null;
	 Image optionsMenu = null;
	 Image verticalCursor = null;
	 Image horizontalCursor = null;
	 Image cursorCom1 = null;
	 Image cursorCom2 = null;
	 Image cursorCom3 = null;
	 Image cursorP1 = null;
	 Image cursorP2 = null;
	 Image errorBox = null;
	 Image bigProjectileGuyRight = null;
	 Image bigProjectileGuyLeft = null;
	 Image smallProjectileGuy = null;
	 Image bigProjectileGerryRight = null;
	 Image bigProjectileGerryLeft = null;
	 Image smallProjectileGerry = null;
	 Image powerUpImmune = null;
	 Image powerUpLife = null;
	 Image powerUpSmash = null;
	 Image powerUpSpeed = null;
	 Image hudGuy;
	 Image hudGerry;
	 Image hudKnight;
	 Image hudNinja;
	 Image victoryBackground;
	 Image gameOverMenu;
	 Image optionsMenuImg;
	 Image optionsMenuCursor;
	 
	 static String errorString;
	 private int width, height;
	 private int currentWidth;
	 
	 private MainMenu mainMenu;
	 private WorldManager world;
	 private EditorGUI editor;
	 
	 private double fractionPos; //rapporto risoluzione default(1200x800 e quella scelta)
	 private double fractionImg;
	 
	 private String currentPath;
	 private String resolutionText;
	
	public GamePanel(JFrame frame, WorldManager wm, MainMenu menu)  {
		super();
		this.frame = frame;
		this.world = wm;
		this.width = wm.getBorderWidth();
		this.height = wm.getBorderHeight();
		this.currentWidth = this.width;
		this.editor = new EditorGUI(world.getMapEditor());
		
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		fractionPos = 1.0;
		fractionImg = 1.0;
		guy = new Animator("guy");
		gerry = new Animator("gerry");
		knight = new Animator("knight");
		ninja = new Animator("ninja");
		others = new Animator("others");
		others2 = new Animator("others2");
		staticImages = new LinkedList<Image>();
		currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/";
		
		try {
			font1 = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(currentPath + "resources/fonts/font1.TTF"))).deriveFont(Font.PLAIN, 34);
			font2 = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(currentPath + "resources/fonts/font2.ttf"))).deriveFont(Font.PLAIN, 24);
			font3 = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(currentPath + "resources/fonts/font1.TTF"))).deriveFont(Font.PLAIN, 56);
			font4 = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File(currentPath + "resources/fonts/font1.TTF"))).deriveFont(Font.PLAIN, 18);
		}
		catch(Exception e)  {
			System.out.println("[ERROR] Unable to find font on disk. ");
			//GamePanel.setError("[ERROR] Unable to find font on disk.");
			//GamePanel.changeStatus(Status.ERROR);	
			
		}	
		mainMenu = menu;
		loadMenu();
		
		this.add(mainMenu.getMultiplayerMenu().getField());
	}
	
	public void loadMenu() {
		if(status != GameState.ERROR) {
			status = GameState.MENU1;
			SoundManager.PlaySound("mainMenu");
		}
		loadImages();
	}
	
	public void loadMap()  {
		
		Map map = world.getMap();
		
		if(map.getName().equals("Custom"))
			return;
		
		try  {
			backgroundImg = ImageIO.read(new File(currentPath + "resources/img/maps/" + map.getBackground() + ".png"));
				
			staticImages.clear();
			for(AbstractStaticObject obj : world.getMap().getObjects()) {
				staticImages.add(ImageIO.read(new File(currentPath + "resources/img/maps/" + obj.getObjectTag() + ".png")));
			}

		}  catch(IOException e)  {
			errorString = "[ERROR] Unable to find images on disk. ";
			System.out.println(errorString);
		}  finally  {
			
			if(fractionPos > 0.99 && fractionPos < 1.01)
				return;
			
			backgroundImg = backgroundImg.getScaledInstance((int) (backgroundImg.getWidth(this) / fractionImg), (int) (backgroundImg.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
			
			//resize platforms
			LinkedList<Image> newImages = new LinkedList<Image>();
			for(Image img : staticImages) {
				newImages.add(img.getScaledInstance((int) (img.getWidth(this) / fractionImg), (int) (img.getHeight(this) / fractionImg), Image.SCALE_DEFAULT));
			}
			staticImages = newImages;
		}
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if (status == GameState.ERROR) {
			g.setColor(new Color(255,255,255));
			g.setFont(font1);
			g.drawImage(errorBox, 0, 0, this);
			g.drawString(errorString,100,650);
			
		}
		else if(status == GameState.MENU1) {
			
			g.drawImage(mainMenuImg, 0, 0, this);
			g.drawImage(horizontalCursor, (int) (mainMenu.getPosX() / fractionPos), (int) (mainMenu.getPosY() / fractionPos), this);
		}
		else if(status == GameState.MENU2_SINGLEPLAYER || status == GameState.MENU2_MULTIPLAYER) {
			
			if(status == GameState.MENU2_SINGLEPLAYER)
				g.drawImage(singleplayerMenuImg, 0, 0, this);
			else  {
				g.drawImage(multiplayerMenuImg, 0, 0, this);
				g.setFont(font2);
				g.drawString(("  " + ((MultiplayerMenu) mainMenu.getCurrentMenu2()).getHostIP()), (int) (547 / fractionPos), (int) (110 / fractionPos));
			}
			
			if(mainMenu.getCurrentMenu2().cursor1())
				g.drawImage(horizontalCursor, (int) (mainMenu.getCurrentMenu2().cursor1PosX() / fractionPos), (int) (mainMenu.getCurrentMenu2().cursor1PosY() / fractionPos), this);
			if(mainMenu.getCurrentMenu2().cursorP1())
				g.drawImage(cursorP1, (int) (mainMenu.getCurrentMenu2().cursorP1PosX() / fractionPos), (int) (mainMenu.getCurrentMenu2().cursorP1PosY() / fractionPos), this);
			if(mainMenu.getCurrentMenu2().cursorP2())
				g.drawImage(cursorP2, (int) (mainMenu.getCurrentMenu2().cursorP2PosX() / fractionPos), (int) (mainMenu.getCurrentMenu2().cursorP2PosY() / fractionPos), this);
			if(mainMenu.getCurrentMenu2().cursorCom1())
				g.drawImage(cursorCom1, (int) (mainMenu.getCurrentMenu2().cursorCom1PosX() / fractionPos), (int) (mainMenu.getCurrentMenu2().cursorCom1PosY() / fractionPos), this);
			if(mainMenu.getCurrentMenu2().cursorCom2())
				g.drawImage(cursorCom2, (int) (mainMenu.getCurrentMenu2().cursorCom2PosX() / fractionPos), (int) (mainMenu.getCurrentMenu2().cursorCom2PosY() / fractionPos), this);
			if(mainMenu.getCurrentMenu2().cursorCom3())
				g.drawImage(cursorCom3, (int) (mainMenu.getCurrentMenu2().cursorCom3PosX() / fractionPos), (int) (mainMenu.getCurrentMenu2().cursorCom3PosY() / fractionPos), this);
			if(mainMenu.getCurrentMenu2().cursor3())
				g.drawImage(verticalCursor, (int) (mainMenu.getCurrentMenu2().cursor3PosX() / fractionPos), (int) (mainMenu.getCurrentMenu2().cursor3PosY() / fractionPos), this);
			if(mainMenu.getCurrentMenu2().cursorFight())
				g.drawImage(verticalCursor, (int) (mainMenu.getCurrentMenu2().cursorFightPosX() / fractionPos), (int) (mainMenu.getCurrentMenu2().cursorFightPosY() / fractionPos), this);
			
			
		}
		else if(status == GameState.MENU_EDITOR)  {
			g.drawImage(editor.getBackground(world.getMapEditor().getBackgroundIndex()), 0, 0, this);
			
			//caricamento in caso di aggiunta
			int i = 0;
			for(AbstractStaticObject platform : world.getMapEditor().getPlatforms())  {
				try  {
					editor.getImages().get(i);
				}
				catch(IndexOutOfBoundsException e)  {
					editor.load(platform);
				}
				i++;
			}
			
			//platforms posizionate
			i = 0;
			for(AbstractStaticObject platform : world.getMapEditor().getPlatforms())  {
				int posX = platform.getPosX();
				int posY = platform.getPosY();
				
				g.drawImage(editor.getImages().get(i), (int) (posX / fractionPos), (int) (posY / fractionPos), this);
				i++;
			}
			
			i = 0;
			
			//platform box components
			if(!world.getMapEditor().isDraggingPlatform())  {
				g.drawImage(editor.getPlatformBox(), 0, (int) (520 / fractionPos), this);
				for(AbstractStaticObject platform : world.getMapEditor().getDefaultPlatforms())  {
					int posX = platform.getPosX();
					int posY = platform.getPosY();						
					
					g.drawImage(editor.getDefaultImages().get(i), (int) (posX / fractionPos), (int) (posY / fractionPos), this);
						
					i++;
				}
			}
			g.drawImage(editor.getCursorLeft(), (int) (world.getMapEditor().getCursorLeftPosX() / fractionPos), 0, this);
			g.drawImage(editor.getCursorRight(), (int) (world.getMapEditor().getCursorRightPosX() / fractionPos), 0, this);
		}
		else if(status == GameState.GAME ) {
			
			if(world.getMapName().equals("Custom"))  {
				g.drawImage(editor.getBackground(world.getMapEditor().getBackgroundIndex()), 0, 0, this);
				
				for(int i = 0; i < world.getMap().getObjects().size(); i++)  {
					g.drawImage(editor.getImages().get(i), (int) (world.getMap().getObjects().get(i).getPosX() / fractionPos), (int) (world.getMap().getObjects().get(i).getPosY() / fractionPos), this);
				}
			}
			else  {
				g.drawImage(backgroundImg, 0, 0, this);
				
				for(int i = 0; i < world.getMap().getObjects().size(); i++)  {
					g.drawImage(staticImages.get(i), (int) (world.getMap().getObjects().get(i).getPosX() / fractionPos), (int) (world.getMap().getObjects().get(i).getPosY() / fractionPos), this);
				}
			}
			
			for(PowerUp p : world.getPowerUps())  {
				
				if(p.getType() == PowerUpType.IMMUNE)
					g.drawImage(powerUpImmune, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
				else if(p.getType() == PowerUpType.LIFE)
					g.drawImage(powerUpLife, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
				else if(p.getType() == PowerUpType.SMASH)
					g.drawImage(powerUpSmash, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
				else if(p.getType() == PowerUpType.SPEED)
					g.drawImage(powerUpSpeed, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
			}
			
			
			
			boolean options = options();
			int posHudX = 50; int posHudY = 670;
			
			if(world.getMapName().equals("Sand")) {
				g.setColor(new Color(0,0,0));
			}
			else
				g.setColor(new Color(255,255,255));
			
			g.setFont(font1);
			g.drawString("time", (int) (550 / fractionPos), (int) (700 / fractionPos));
			g.setFont(font3);
			g.drawString(String.valueOf(world.getGameTime()/60), (int) (550 / fractionPos), (int) (760 / fractionPos));
			g.setFont(font2);
			
			if(world.isLevelFinished()) {
				if(world.hasPlayerWon())
					g.drawImage(victoryBackground, 0, 0, this);	
				else
					g.drawImage(gameOverMenu,0,0,this);
			}
			
			int charactersCount = 0;
			for(AbstractCharacter current : world.getCharacters()) {
				
				Image currentCursor = null;
				if(current.getObjectTag().equals("p1"))
					currentCursor = cursorP1;
				else if(current.getObjectTag().equals("p2"))
					currentCursor = cursorP2;
				else if(current.getObjectTag().equals("com1"))
					currentCursor = cursorCom1;
				else if(current.getObjectTag().equals("com2"))
					currentCursor = cursorCom2;
				else if(current.getObjectTag().equals("com3"))
					currentCursor = cursorCom3;
				
				
				
			if(!world.isLevelFinished()) {
				
				if(!current.isDead())
					g.drawImage(currentCursor, (int) ((current.getHitbox().getPosX()-5) / fractionPos), (int) ((current.getPosY()-25) / fractionPos), this);
					
				if(current.getStats().getType().equals("guy"))  {
					
					g.drawImage(hudGuy, (int) (posHudX / fractionPos), (int) (posHudY / fractionPos), this);
					guy.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos), fractionPos, current, options);
					
					for(Projectile p : current.getProjectiles())  {
						if(p.isBig() && p.getDirectionX() == Direction.RIGHT)
							g.drawImage(bigProjectileGuyRight, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
						else if(p.isBig())
							g.drawImage(bigProjectileGuyLeft, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
						else
							g.drawImage(smallProjectileGuy, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
					}
				}
					
				else if(current.getStats().getType().equals("gerry"))  {
					
					g.drawImage(hudGerry, (int) (posHudX / fractionPos), (int) (posHudY / fractionPos), this);
					
					gerry.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos),  fractionPos, current, options);
					
					for(Projectile p : current.getProjectiles())  {
						if(p.isBig() && p.getDirectionX() == Direction.RIGHT)
							g.drawImage(bigProjectileGerryRight, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
						else if(p.isBig())
							g.drawImage(bigProjectileGerryLeft, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
						else
							g.drawImage(smallProjectileGerry, (int) (p.getPosX() / fractionPos), (int) (p.getPosY() / fractionPos), this);
					}
					
				}
				else if(current.getStats().getType().equals("knight")) {
					
					g.drawImage(hudKnight, (int) (posHudX / fractionPos), (int) (posHudY / fractionPos), this);
					
					knight.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos), fractionPos, current, options);
				}
				else if(current.getStats().getType().equals("ninja")) {
					
					g.drawImage(hudNinja, (int) (posHudX / fractionPos), (int) (posHudY / fractionPos), this);
					
					ninja.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos),  fractionPos, current, options);
				}
					
				g.setFont(font2);
				if(world.getMap().getName()!= "Sand")
					g.setColor(new Color(255,255,255));
				else
					g.setColor(new Color(0,0,0));
					
				g.drawString(String.valueOf(current.getDeathCount()), (int) ((posHudX+120) / fractionPos), (int) ((posHudY+20) / fractionPos));
				g.drawString("deaths", (int) ((posHudX+120) / fractionPos), (int) ((posHudY+40) / fractionPos));
				
				g.setFont(font1);
				if(current.getDamageTaken() >= 0)
					g.setColor(new Color(60,200,9));
				if(current.getDamageTaken() > 30)
					g.setColor(new Color(200,200,9));
				if(current.getDamageTaken() > 60)
					g.setColor(new Color(200,100,0));
				if(current.getDamageTaken() > 90)
					g.setColor(new Color(255,0,0));
				if(current.getDamageTaken() > 120)
					g.setColor(new Color(255,0,100));
									
				g.drawString(String.valueOf(Math.round(current.getDamageTaken())) + "%", (int) ((posHudX+120) / fractionPos), (int) ((posHudY+80) / fractionPos));
				
				if(!current.isDead())  {
					if(current.hasPowerSpeed()) {
						g.drawImage(powerUpSpeed,(int) ((current.getPosX()-10)/fractionPos), (int) (current.getPosY() / fractionPos),(int) ((current.getPosX()-10+25)/fractionPos) , (int) ((current.getPosY()+25) / fractionPos), 0, 0, 35, 35, null);
					}
					
					if(current.hasPowerSmash()) {
						g.drawImage(powerUpSmash,(int) ((current.getPosX()-10)/fractionPos), (int) ((current.getPosY()+25 )/ fractionPos),(int) ((current.getPosX()-10+25)/fractionPos) , (int) ((current.getPosY()+25+25) / fractionPos), 0, 0, 35, 35, null);
					}
					if(current.hasPowerImmune()) {
						g.drawImage(powerUpImmune,(int) ((current.getPosX()-10)/fractionPos), (int) ((current.getPosY()+50) / fractionPos),(int) ((current.getPosX()-10+25)/fractionPos) , (int) ((current.getPosY()+25+50) / fractionPos), 0, 0, 35, 35, null);
					}
				}	
				
				
				others.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos), fractionPos, current, options);
				others2.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos),  fractionPos, current, options);
				
				charactersCount++;
				if(charactersCount == 2)
					posHudX += 400;
				else
					posHudX += 250;
				
				
			}
			
			if(world.isLevelFinished())  {
				if(world.getMapName()=="Sand")
					g.setColor(new Color(0,0,0));
				else
					g.setColor(new Color(255,255,255));
				
				
				posHudX = 50; posHudY = 600;
			
				
				if(current.getVictory()) {
					
					if(!(SoundManager.isPlaying("victory") || SoundManager.isPlaying("gameOver")))
						SoundManager.StopSounds();
					
					if(current instanceof Player)
						SoundManager.PlaySound("victory");
					else
						SoundManager.PlaySound("gameOver");
					
					g.drawImage(currentCursor, (int)(120/fractionPos),(int) (50/fractionPos), 
							(int)(130+70/fractionImg), (int)(50+64/fractionImg), 0, 0, (int)(46/fractionImg), (int)(38/fractionImg), null);
						
					
					if(current.getType().equals("guy")) {
						guy.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos), fractionPos, current,options);
						g.drawImage(hudGuy, (int) (posHudX / fractionPos), (int) (posHudY / fractionPos), this);
					}	
					else if(current.getType().equals("gerry")) {
						gerry.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos), fractionPos, current,options);
						g.drawImage(hudGerry, (int) (posHudX / fractionPos), (int) (posHudY / fractionPos), this);
					}	
					else if(current.getType().equals("knight")) {
						knight.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos), fractionPos,current, options);
						g.drawImage(hudKnight, (int) (posHudX / fractionPos), (int) (posHudY / fractionPos), this);
					}	
					else if(current.getType().equals("ninja")) {
						ninja.drawAnimation(g, (int) (current.getPosX() / fractionPos), (int) (current.getPosY() / fractionPos), fractionPos, current, options);
						g.drawImage(hudNinja, (int) (posHudX / fractionPos), (int) (posHudY / fractionPos), this);
					}	
										
					g.setFont(font2);
					if(world.getMap().getName()!= "Sand")
						g.setColor(new Color(255,255,255));
					else
						g.setColor(new Color(0,0,0));
					
					g.drawString(String.valueOf(current.getDeathCount()), (int) ((posHudX+120) / fractionPos), (int) ((posHudY+20) / fractionPos));
					g.drawString("deaths", (int) ((posHudX+120) / fractionPos), (int) ((posHudY+40) / fractionPos));
					
					g.setFont(font1);
					
					if(current.getDamageTaken() > 120)
						g.setColor(new Color(255,0,100));
					else if(current.getDamageTaken() > 90)
						g.setColor(new Color(255,0,0));
					else if(current.getDamageTaken() > 60)
						g.setColor(new Color(200,100,0));
					else if(current.getDamageTaken() > 30)
						g.setColor(new Color(100,255,0));
					else
						g.setColor(new Color(0,255,0));
				
					g.drawString(String.valueOf(Math.round(current.getDamageTaken())) + "%", (int)((posHudX+120)/fractionPos),(int) ((posHudY+80)/fractionPos));
					
				}		
				
				
				}
			}
		}
		
		if(mainMenu.getOptionsMenu().isActive())  {
			g.drawImage(optionsMenuImg, 0, 0, this);
			
			//flip orizzontale del cursore
			int x = mainMenu.getOptionsMenu().getCursorPosX();
			int y = mainMenu.getOptionsMenu().getCursorPosY();
			int width = optionsMenuCursor.getWidth(this);
			int height = optionsMenuCursor.getHeight(this);
			
			g.drawImage(optionsMenuCursor, (int) ((x + width) / fractionPos), (int) (y / fractionPos), - width, height, this);
			g.setFont(font4);
			g.drawString(mainMenu.getOptionsMenu().getResolution(), (int) (630 / fractionPos), (int) (520 / fractionPos));
		}			
	}
	
	public void setError(String error) {
		errorString = error;
	}
	
	public GameState getStatus() {
		return status;
	}
	
	public void changeStatus(GameState state) {
		if(state == GameState.MENU2_MULTIPLAYER)  {
			mainMenu.getMultiplayerMenu().setFieldVisible(true);
		}
		else  {
			mainMenu.getMultiplayerMenu().setFieldVisible(false);
		}
		
		if(state != GameState.MENU2_MULTIPLAYER && state != GameState.MENU2_SINGLEPLAYER)	{
			if(state == GameState.GAME) {
				SoundManager.StopSounds();
				SoundManager.PlaySound(world.getMapName()+"Music");
			}
			else if(state == GameState.MENU1 && status != GameState.MENU2_MULTIPLAYER && status != GameState.MENU2_SINGLEPLAYER) {
				SoundManager.StopSounds();
				SoundManager.PlaySound("mainMenu");
			}
			
		}	
		status = state;
		
	}
	
	public void resize()  {
		
		resolutionText = mainMenu.getOptionsMenu().getResolution();
		
		int newWidth = Integer.parseInt( resolutionText.substring(0, resolutionText.indexOf("x")));
		
		fractionPos = (double) 1200 / newWidth;
		fractionImg = (double) currentWidth / newWidth;
		currentWidth = newWidth;
		
		GraphicManager.resize(newWidth, (newWidth/3)*2);
		mainMenu.getMultiplayerMenu().resize(fractionPos);	//resize della textfield
		editor.resize(fractionImg, fractionPos);
		
		mainMenuImg = mainMenuImg.getScaledInstance((int) (mainMenuImg.getWidth(null) / fractionImg), (int) (mainMenuImg.getHeight(null) / fractionImg), Image.SCALE_DEFAULT);
		singleplayerMenuImg = singleplayerMenuImg.getScaledInstance((int) (singleplayerMenuImg.getWidth(this) / fractionImg), (int) (singleplayerMenuImg.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		multiplayerMenuImg = multiplayerMenuImg.getScaledInstance((int) (multiplayerMenuImg.getWidth(this) / fractionImg), (int) (multiplayerMenuImg.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		horizontalCursor = horizontalCursor.getScaledInstance((int) (horizontalCursor.getWidth(this) / fractionImg), (int) (horizontalCursor.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		verticalCursor = verticalCursor.getScaledInstance((int) (verticalCursor.getWidth(this) / fractionImg), (int) (verticalCursor.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		cursorP1 = cursorP1.getScaledInstance((int) (cursorP1.getWidth(this) / fractionImg), (int) (cursorP1.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		cursorP2 = cursorP2.getScaledInstance((int) (cursorP2.getWidth(this) / fractionImg), (int) (cursorP2.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		cursorCom1 = cursorCom1.getScaledInstance((int) (cursorCom1.getWidth(this) / fractionImg), (int) (cursorCom1.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		cursorCom2 = cursorCom2.getScaledInstance((int) (cursorCom2.getWidth(this) / fractionImg), (int) (cursorCom2.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		cursorCom3 = cursorCom3.getScaledInstance((int) (cursorCom3.getWidth(this) / fractionImg), (int) (cursorCom3.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		errorBox = errorBox.getScaledInstance((int) (errorBox.getWidth(this) / fractionImg), (int) (errorBox.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		victoryBackground = victoryBackground.getScaledInstance((int) (victoryBackground.getWidth(this) / fractionImg), (int) (victoryBackground.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		gameOverMenu = gameOverMenu.getScaledInstance((int) (gameOverMenu.getWidth(this) / fractionImg), (int) (gameOverMenu.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		
		optionsMenuImg = optionsMenuImg.getScaledInstance((int) (optionsMenuImg.getWidth(this) / fractionImg), (int) (optionsMenuImg.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		optionsMenuCursor = optionsMenuCursor.getScaledInstance((int) (optionsMenuCursor.getWidth(this) / fractionImg), (int) (optionsMenuCursor.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		
		hudGuy = hudGuy.getScaledInstance((int) (hudGuy.getWidth(this) / fractionImg), (int) (hudGuy.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		hudGerry = hudGerry.getScaledInstance((int) (hudGerry.getWidth(this) / fractionImg), (int) (hudGerry.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		hudKnight = hudKnight.getScaledInstance((int) (hudKnight.getWidth(this) / fractionImg), (int) (hudKnight.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		hudNinja = hudNinja.getScaledInstance((int) (hudNinja.getWidth(this) / fractionImg), (int) (hudNinja.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		bigProjectileGuyRight = bigProjectileGuyRight.getScaledInstance((int) (bigProjectileGuyRight.getWidth(this) / fractionImg), (int) (bigProjectileGuyRight.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		bigProjectileGuyLeft = bigProjectileGuyLeft.getScaledInstance((int) (bigProjectileGuyLeft.getWidth(this) / fractionImg), (int) (bigProjectileGuyLeft.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		smallProjectileGuy = smallProjectileGuy.getScaledInstance((int) (smallProjectileGuy.getWidth(this) / fractionImg), (int) (smallProjectileGuy.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		bigProjectileGerryRight = bigProjectileGerryRight.getScaledInstance((int) (bigProjectileGerryRight.getWidth(this) / fractionImg), (int) (bigProjectileGerryRight.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		bigProjectileGerryLeft = bigProjectileGerryLeft.getScaledInstance((int) (bigProjectileGerryLeft.getWidth(this) / fractionImg), (int) (bigProjectileGerryLeft.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		smallProjectileGerry = smallProjectileGerry.getScaledInstance((int) (smallProjectileGerry.getWidth(this) / fractionImg), (int) (smallProjectileGerry.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		
		powerUpImmune = powerUpImmune.getScaledInstance((int) (powerUpImmune.getWidth(this) / fractionImg), (int) (powerUpImmune.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		powerUpLife = powerUpLife.getScaledInstance((int) (powerUpImmune.getWidth(this) / fractionImg), (int) (powerUpImmune.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		powerUpSmash = powerUpSmash.getScaledInstance((int) (powerUpImmune.getWidth(this) / fractionImg), (int) (powerUpImmune.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		powerUpSpeed = powerUpSpeed.getScaledInstance((int) (powerUpImmune.getWidth(this) / fractionImg), (int) (powerUpImmune.getHeight(this) / fractionImg), Image.SCALE_DEFAULT);
		
		guy.resize(fractionImg, this);
		gerry.resize(fractionImg, this);
		knight.resize(fractionImg, this);
		ninja.resize(fractionImg, this);
		others.resize(fractionImg, this);
		others2.resize(fractionImg, this);
		
		try  {			
			LinkedList<Image> newImages = new LinkedList<Image>();
			
			backgroundImg = backgroundImg.getScaledInstance((int) (backgroundImg.getWidth(null) / fractionImg), (int) (backgroundImg.getHeight(null) / fractionImg), Image.SCALE_DEFAULT);
			
			for(Image img : staticImages) {
				newImages.add(img.getScaledInstance((int) (img.getWidth(this) / fractionImg), (int) (img.getHeight(this) / fractionImg), Image.SCALE_DEFAULT));
			}
			staticImages = newImages;
			
		}  catch(Exception e)  {
			
		}
	}
	
	public boolean options()  {
		return mainMenu.getOptionsMenu().isActive();
	}
	
	public void loadImages()  {
		try {
			mainMenuImg = ImageIO.read(new File(currentPath + "resources/img/menus/MainMenu.png"));
			singleplayerMenuImg = ImageIO.read(new File(currentPath + "resources/img/menus/PickingMenu.png"));
			multiplayerMenuImg = ImageIO.read(new File(currentPath + "resources/img/menus/MultiplayerMenu.png"));
			horizontalCursor = ImageIO.read(new File(currentPath + "resources/img/menus/HorizontalCursor.png"));
			verticalCursor = ImageIO.read(new File(currentPath + "resources/img/menus/VerticalCursor.png"));
			cursorP1 = ImageIO.read(new File(currentPath + "resources/img/menus/CursorP1.png"));
			cursorP2 = ImageIO.read(new File(currentPath + "resources/img/menus/CursorP2.png"));
			cursorCom1 = ImageIO.read(new File(currentPath + "resources/img/menus/CursorCom1.png"));
			cursorCom2 = ImageIO.read(new File(currentPath + "resources/img/menus/CursorCom2.png"));
			cursorCom3 = ImageIO.read(new File(currentPath + "resources/img/menus/CursorCom3.png"));
			errorBox = ImageIO.read(new File(currentPath + "resources/img/menus/ErrorBox.png"));
			victoryBackground = ImageIO.read(new File(currentPath + "resources/img/menus/VictoryMenu.png"));
			gameOverMenu = ImageIO.read(new File(currentPath + "resources/img/menus/GameOverMenu.png"));
			optionsMenuImg = ImageIO.read(new File(currentPath + "resources/img/menus/optionsMenu.png"));
			optionsMenuCursor = ImageIO.read(new File(currentPath + "resources/img/menus/HorizontalCursor.png"));
			optionsMenuCursor = optionsMenuCursor.getScaledInstance(15, 15, Image.SCALE_DEFAULT);
			
			hudGuy = ImageIO.read(new File(currentPath + "resources/img/menus/Hud_Guy.png"));
			hudGerry = ImageIO.read(new File(currentPath + "resources/img/menus/Hud_Gerry.png"));
			hudKnight = ImageIO.read(new File(currentPath + "resources/img/menus/Hud_Knight.png"));
			hudNinja = ImageIO.read(new File(currentPath + "resources/img/menus/Hud_Ninja.png"));
			bigProjectileGuyRight = ImageIO.read(new File(currentPath + "resources/img/characters/BulletBig_Guy_right.png"));
			bigProjectileGuyLeft = ImageIO.read(new File(currentPath + "resources/img/characters/BulletBig_Guy_left.png"));
			smallProjectileGuy = ImageIO.read(new File(currentPath + "resources/img/characters/BulletSmall_Guy.png"));
			bigProjectileGerryRight = ImageIO.read(new File(currentPath + "resources/img/characters/BulletBig_Gerry_right.png"));
			bigProjectileGerryLeft = ImageIO.read(new File(currentPath + "resources/img/characters/BulletBig_Gerry_left.png"));
			smallProjectileGerry = ImageIO.read(new File(currentPath + "resources/img/characters/BulletSmall_Gerry.png"));
			
			powerUpImmune = ImageIO.read(new File(currentPath + "resources/img/characters/powerUp_Immune.png"));
			powerUpLife = ImageIO.read(new File(currentPath + "resources/img/characters/powerUp_Life.png"));
			powerUpSmash = ImageIO.read(new File(currentPath + "resources/img/characters/powerUp_Smash.png"));
			powerUpSpeed = ImageIO.read(new File(currentPath + "resources/img/characters/powerUp_Speed.png"));
		} 
		catch(IOException e)  {
			System.out.println("[ERROR] Unable to find menus on disk. ");
		}
	}	
	
	public double getFractionPos()  {
		return fractionPos;
	}
	
	public void clearEditor()  {
		editor.clear();
	}
}