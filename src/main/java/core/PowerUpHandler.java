package core;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import interfaces.PowerUpType;
import managers.WorldManager;

public class PowerUpHandler {
	
	private LinkedBlockingQueue<PowerUp> powerups;
	private Random rand;
	private WorldManager world;
	private int spawnPosX;
	private int cooldownTime;
	private int n;
	
	public PowerUpHandler(WorldManager world)  {
		
		this.world = world;
		powerups = new LinkedBlockingQueue<PowerUp>();
		rand = new Random();
		cooldownTime = 300;
		spawnPosX = 0;
		n = 0;
	}
	
	public boolean canSpawnPowerUp()  {
		
		if(powerups.size() > 3 || cooldownTime > 0)
			return false;
		
		 n = rand.nextInt(10000);
		
		//prob di spawnare ad ogni secondo = (20/10000)*60 = 12%
		if(n < 20)
			return true;
		
		return false;
	}
	
	public void spawnPowerUp()  {
		
		Random rand = new Random();
		 n = rand.nextInt(10);
		n++;
		Platform chosenPlatform = (Platform) world.getMap().getObjects().getFirst();
		
		LinkedList<AbstractStaticObject> platforms = world.getMap().getObjects();
		while(n > 0)  {
			n--;
			for(AbstractStaticObject p : platforms)  {
				if(n == 0)  {
					chosenPlatform = (Platform) p;
					break;
				}
				n--;
			}
		}
		
		spawnPosX = chosenPlatform.getPosX() + (int) (chosenPlatform.getWidth() / 2);
		
		n = rand.nextInt(4);
		
		switch(n)  {
		//immune
		case 0:
			powerups.add( new PowerUp(spawnPosX, 50, PowerUpType.IMMUNE) );
			break;
		//life
		case 1:
			powerups.add( new PowerUp(spawnPosX, 50, PowerUpType.LIFE) );
			break;
		//smash
		case 2:
			powerups.add( new PowerUp(spawnPosX, 50, PowerUpType.SMASH) );
			break;
		//speed
		default:
			powerups.add( new PowerUp(spawnPosX, 50, PowerUpType.SPEED) );
			break;
		}
		
		cooldownTime = 300;
	}
	
	public LinkedBlockingQueue<PowerUp> getPowerUps()  {
		return powerups;
	}
	
	public void addPowerUp(int spawnPosX,int n) {
		switch(n)  {
		//immune
		case 0:
			powerups.add( new PowerUp(spawnPosX, 50, PowerUpType.IMMUNE) );
			break;
		//life
		case 1:
			powerups.add( new PowerUp(spawnPosX, 50, PowerUpType.LIFE) );
			break;
		//smash
		case 2:
			powerups.add( new PowerUp(spawnPosX, 50, PowerUpType.SMASH) );
			break;
		//speed
		default:
			powerups.add( new PowerUp(spawnPosX, 50, PowerUpType.SPEED) );
			break;
		}
	}
	public void update()  {
		
		if(cooldownTime > 0)
			cooldownTime--;
		
		for(PowerUp p : powerups)
			p.update();
	}
	public int getSpawnPosX() {
		return spawnPosX;
	}
	public int getTypePowerUp() {
		return n;
	}
	public void remove(PowerUp p)  {
		powerups.remove(p);
	}
	
	public void clear()  {
		powerups.clear();
	}
}
