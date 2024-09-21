package managers;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import core.*;
import core.Map;
import online.Packet;
import online.ServerThread;
import menus.MapEditorMenu;

public class WorldManager {
	
	
	//Mappa di gioco
	private Map map;
	String mapName;
		
	//Lista degli oggetti che si muovono
	private LinkedList<AbstractCharacter> characters;
	
	//Lista giocatori IA
	private LinkedList<EnemyPlayer> enemies;
	
	//Player aka Character
	private Player player;
	
	//Gestore powerups
	private PowerUpHandler ppsHandler;
	
	boolean levelFinished;
	boolean playerHasWon;
	
	boolean HostOnline;
	
	private ServerThread server;
	
	
	private Packet packet;
	private String[] onlinePlayers;
	
	private int currentGameTime;
	private int defaultGameTime;
	
	
	int borderWidth;	//i bordi del mondo, al momento
	int borderHeight;	//sono gli stessi del pannello
	
	private int currentLevel;	//singleplayer only
	/**
	 * 1 = sand, 2 = temple, 3 = city
	 */
	private boolean mapsCompleted[];	//singleplayer only
	/**
	 * 1 = guy, 2 = knight, 3 = ninja, 4 = gerry
	 */
	private boolean enemiesCompleted[]; //singleplayer only
	private boolean bossRushMode;		//singleplayer only
	
	
	
	public WorldManager(int width, int height)  {
		super();
		this.borderWidth = width;
		this.borderHeight = height;

		characters = new LinkedList<AbstractCharacter>();
		enemies = new LinkedList<EnemyPlayer>();
		map = new Map();
		packet = new Packet();
		onlinePlayers = new String[4];
		ppsHandler = new PowerUpHandler(this);
		levelFinished = false;
		playerHasWon = false;
		bossRushMode =  false;
		defaultGameTime = 2000;
		currentLevel = 1;
		HostOnline = false;
		server = null;
		
		mapsCompleted = new boolean[3];		
		enemiesCompleted = new boolean[4];
	}
	
	public boolean isHostOnline() {
		return HostOnline;
	}


	public void setHostOnline(boolean hostOnline) {
		HostOnline = hostOnline;
	}
	
	public ServerThread getServer() {
		return server;
	}

	public void setServer(ServerThread server) {
		this.server = server;
	}

	public int getBorderWidth() {
		return borderWidth;
	}
	
	public void setBorderWidth(int w) {
		this.borderWidth = w;
	}

	public int getBorderHeight() {
		return borderHeight;
	}

	public void setBorderHeight(int h) {
		this.borderHeight = h;
	}
	
	public List<AbstractCharacter> getCharacters() {
		return characters;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		characters.add(player);
		
	}
	
	public void addCharacter(AbstractCharacter ch)  {
		characters.add(ch);
	}
	
	public void deleteCharacter(AbstractCharacter ch) {
		characters.remove(ch);
	}
	
	public int getCurrentGameTime() {
		return currentGameTime;
	}
	
	public String[] getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(String[] onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}
	
	public void setOnlinePlayers(int i, String type) {
		this.onlinePlayers[i] = type;
	}

	public void setCurrentGameTime(int currentGameTime) {
		this.currentGameTime = currentGameTime;
	}

	public void clear()  {
		player = null;
		enemies.clear();
		characters.clear();
		ppsHandler.clear();
		levelFinished = false;
		playerHasWon = false;
		currentLevel = 1;
		currentGameTime = defaultGameTime;
		
		for(int i = 0; i < 3; i++)
			mapsCompleted[i] = false;
	}
	
	public void addEnemyPlayer(EnemyPlayer enemy)  {
		enemies.add(enemy);
		enemy.setMap(map);
		characters.add(enemy);
	}
	
	public Map getMap() {
		return map;
	}
	
	public void setMap(String name) {
		map.setMap(name);
	}
	

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public void update() {
	
		if(levelFinished) {
			return;
		}
		
		if(currentGameTime < 1)  {
			levelFinished = true;
			calculateVictorious();
			
		}
		else
			currentGameTime --;
		
		if(HostOnline || (enemies.size() != 0 )) {
			if(ppsHandler.canSpawnPowerUp()) {
				ppsHandler.spawnPowerUp();
				packet.setPowerUp(ppsHandler.getSpawnPosX(),ppsHandler.getTypePowerUp());
				packet.setSpawnPowerUp(true);
				
			}
		}
		
		for(PowerUp p : ppsHandler.getPowerUps())  {
			
			for(AbstractCharacter ch : characters)  {
				if(p.collision(ch))  {
					
					ch.gotPowerUp(p.getType());
					ppsHandler.remove(p);
				}
			}
			if(p == null)
				continue;
			
			p.applyGravity();
			
			for(AbstractStaticObject object : map.getObjects()) {
				p.bottomCollision(object);
				
				//non ha bisogno di altre collisioni perchè:
				// 1) cade solamente verso il basso
				// 2) avrà dei punti precisi X per ogni mappa dove spawnare
			}
		}
		
			
		for(EnemyPlayer enemy : enemies) {
				
			enemy.setTarget(whichTarget(enemy));
			
			if(whichTarget(enemy) != null) {
				enemy.actionTimer();
				if(enemy.getAiDefenseMode())
					enemy.notFollowAndDefend();
				else
					enemy.followPlayerAndMena();
			}
		}
		
		for(AbstractCharacter character : characters) {
			if(!character.isDead())
				character.applyGravity();
			verifyDeath(character);
			
			for(AbstractStaticObject object : map.getObjects()) {
				
				if(character.bottomCollision(object)) {
					character.setPlatform(object);
				}
				
				character.rightCollision(object);
				character.leftCollision(object);
				character.topCollision(object);
				
				for(Projectile p : character.getProjectiles())  {
					if(p.collision(object) || p.outOfWorld(borderWidth, borderHeight))
						character.removeProjectile(p);
				}
			
			}
		}
		
		for(AbstractCharacter character1 : characters) {
			for(AbstractCharacter character2 : characters) {
				if(character1 != character2 && character1.isMelee())
					character1.attackCollision(character2);
				else if(character1 != character2)  {
					for(Projectile p : character1.getProjectiles())  {
						if(p.collision(character2.getHitbox()))  {
							character2.hitByProjectile(p);
							character1.removeProjectile(p);
						}
					}
				}	
			}
		}
		
		ppsHandler.update();
		for(AbstractCharacter character : characters) {
			character.update();
		}
		
		
		
	}

	public AbstractCharacter whichTarget(EnemyPlayer chara) {
		AbstractCharacter target = null;
		int x;
		int y;
		int xChara = chara.getPosX();
		int yChara = chara.getPosY();
		double distanceDifference;
		double distanceDifferenceMin= 2000;
		for(AbstractCharacter character : characters) {
			x = character.getPosX();
			y = character.getPosY();
			distanceDifference = (Math.sqrt(Math.pow(yChara-y, 2) + Math.pow(xChara-x, 2)));
			
			if(character != chara && distanceDifference < distanceDifferenceMin && chara.getPlatform()!=null) {
				if((chara.isReachable(character.getPlatform()) || characters.size()==2) && !chara.isDead()) {
					target = character;
					distanceDifferenceMin = distanceDifference;
				}
			}
						
		}
		return target;
				
	}
	
	public void calculateVictorious() {
		int minDeaths = 100;
		AbstractCharacter victorious = null;
		
		for(AbstractCharacter chara : characters) {
			if(chara.getDeathCount() < minDeaths) {
				victorious = chara;
				minDeaths = chara.getDeathCount();
			}	
			else if(chara.getDeathCount() == minDeaths) {
				if(chara.getDamageTaken() < victorious.getDamageTaken()) {
					victorious = chara;
				}
			}
				
		}
		
		for(AbstractCharacter chara : characters) {
			if(chara == victorious)  {
				chara.setVictory(true);
				
				if(chara.getType().equals(player.getType()))
					playerHasWon = true;
			}
		}
		
	}
	
	
	public void setMapName(String name)  {
		mapName = name;
	}
	
	public String getMapName()  {
		return mapName;
	}
	
	public void loadMap()  {
		SoundManager.StopSounds();
		map.setMap(mapName);
		currentGameTime = defaultGameTime;
		SoundManager.PlaySound(mapName+"Music");
		
		int count = 0;
		//int spawn_range;
		for(AbstractCharacter c : characters) {
			if(map.getObjects().size() == 0)
				break;
			
			if(count == map.getObjects().size())
				count = 0;
			
			//spawn_range = ThreadLocalRandom.current().nextInt(0, map.getObjects().get(count).getWidth() - c.getWidth()*2);
			
			c.setPosX(map.getObjects().get(count).getPosX() );//+ spawn_range);
			c.setTargetX(map.getObjects().get(count).getPosX());// + spawn_range);
			c.setPosY(map.getObjects().get(count).getPosY() - map.getObjects().get(count).getHeight() - c.getHeight());
			c.setTargetY(map.getObjects().get(count).getPosY() - map.getObjects().get(count).getHeight() - c.getHeight());
			count++;
		}
	}
	
	public LinkedList<EnemyPlayer> getEnemies()  {
		return enemies;
	}
	
	public LinkedBlockingQueue<PowerUp> getPowerUps()  {
		return ppsHandler.getPowerUps();
	}
	
	public int getGameTime() {
		return currentGameTime;
	}
	
	public boolean isLevelFinished()  {
		return levelFinished;
	}
	
	public boolean hasPlayerWon()  {
		return playerHasWon;
	}
	
	//funzione pericolosa, da controllare quando si usa
	public AbstractCharacter getWinner()  {
		if(!levelFinished)
			return null;
		
		for(AbstractCharacter ch : characters)  {
			if(ch.getVictory())
				return ch;
		}
		
		return null;
	}
	
	public boolean changeLevel() {
		for(int i = 0; i < characters.size(); i++)  {
			characters.get(i).setVictory(false);
			characters.get(i).resetStats();
			characters.get(i).clearProjectiles();
		}
		
		if(!playerHasWon || (playerHasWon && currentLevel == 3))
			return false;
		
		playerHasWon = false;
		currentLevel++;
		
		loadNextLevel();
		
		return true;
	}
	
	public void loadNextLevel()  {
		
		ppsHandler.clear();
		
		if(mapName == "Sand")
			mapsCompleted[0] = true;
		else if(mapName == "Temple")
			mapsCompleted[1] = true;
		else
			mapsCompleted[2] = true;
		
		String name;
		
		if(!mapsCompleted[0])
			name = "Sand";
		else if(!mapsCompleted[1])
			name = "Temple";
		else
			name = "City";

		if(enemies.getFirst().getName().equals("guy"))
			enemiesCompleted[0] = true;
		else if(enemies.getFirst().getName().equals("knight"))
			enemiesCompleted[1] = true;
		else if(enemies.getFirst().getName().equals("ninja"))
			enemiesCompleted[2] = true;
		else
			enemiesCompleted[3] = true;
		
		//rimuovo il nemico char dalla lista dei chars
		for(int i = 0; i  < characters.size(); i++)
			if(characters.get(i) instanceof EnemyPlayer)
				characters.remove(i);
		
		enemies.clear();
		if(!enemiesCompleted[0] && !player.getName().equals("guy"))
			addEnemyPlayer(new EnemyPlayer("com1", 300, 0, "guy"));
		else if(!enemiesCompleted[1] && !player.getName().equals("knight"))
			addEnemyPlayer(new EnemyPlayer("com1", 300, 0, "knight"));
		else if(!enemiesCompleted[2] && !player.getName().equals("ninja"))
			addEnemyPlayer(new EnemyPlayer("com1", 300, 0, "ninja"));
		else
			addEnemyPlayer(new EnemyPlayer("com1", 300, 0, "gerry"));
		
		//player.setPosX(600);
		//player.setPosY(0);
		
		this.mapName = name;
		loadMap();
		
		levelFinished = false;
	}
	
	public boolean isBossRushMode()  {
		return bossRushMode;
	}
	
	public void setBossRushMode(boolean mode)  {
		bossRushMode = mode;
	}
	
	public PowerUpHandler getPowerUpHandler() {
		return ppsHandler;
	}
	
	public void verifyDeath(AbstractCharacter chara) {
		if(chara.getPosY()>borderHeight+100 && chara.isDead()) {
			chara.setPosY(borderHeight-100);
			chara.setPosX(chara.getPosX()-50);
		}
		if (!chara.isDead() && ((chara.getDamageTaken() > 130 && chara.isDamaged() && (chara.getHitbox().getPosX() > borderWidth-50 || 
				chara.getHitbox().getPosX() + 50 < 0  || (chara.getHitbox().getPosY()+50 < 0 ))) 
				|| (chara.getPosY() > borderHeight + borderHeight/3))){
				
				chara.setDeath(true);
			}
	}
	
	public MapEditorMenu getMapEditor()  {
		return map.getMapEditor();
	}
}
