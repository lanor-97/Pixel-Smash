package core;



import java.util.Random;

import interfaces.Direction;

public class EnemyPlayer extends AbstractCharacter  {
	
	String name;
	Map map;
	AbstractStaticObject targetPlatform;
	AbstractCharacter target;
	int changeActionTimer;
	boolean aiDefenseMode = false;
	int defenseTimer;
	int attackTimer;
	int rangeRight, rangeLeft;
	int jumpRangeRight, jumpRangeLeft;
	
	public EnemyPlayer(String tag, int x, int y,String character)  {
		//posx, posy, width, height, objectag, direction, speed, type
		super(x, y, 96, 96, tag, Direction.STOP, character);
		targetPlatform = new Platform(0,0,0,0,"platform");
		target = null;
		changeActionTimer = 400;
		defenseTimer = 50;
		attackTimer = 40;
		Random r = new Random();
		aiDefenseMode= r.nextBoolean();
		name = character;
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public String getName()  {
		return name;
	}
	
	public void followPlayerAndMena() {
		
			calculateRange();
			stopDefend();
			
			if(isReachable(target.getPlatform()))
				targetPlatform = target.getPlatform();
			else
				targetPlatform = nextPlatform(target.getPlatform());
			
			
			if(attackTimer >= 0) {
				attackTimer --;
			}
			else {
				attackTimer = 40;
			}	
			
			
			if(isClimbing() || (isFalling() && fallOnPlatform() == null)) {
				
				jumpOnPlatform();
			}
			
			else if(targetPlatform == platform && !target.isClimbing() && !target.isFalling() && isReachable(targetPlatform)) {
				
				if(!enemyInRange() && getJumps() == 0 && target.getHitbox().getPosX()+target.getHitbox().getWidth() < rangeLeft && target.getJumps() == 0 ) {

							moveLeft();
					} 
				else if(!enemyInRange() && getJumps() == 0 && target.getHitbox().getPosX() > rangeRight  && target.getJumps() == 0 ) {
							
							moveRight();
					}
				else if (enemyInRange()) {
					stopMove();
					if(attackTimer < 20) {
						if(target.getPosX() > posX) {
							directionX = Direction.RIGHT;
						}
						else {
							directionX = Direction.LEFT;
						}
						if(!hasPowerSmash())
							softAttack();
						else
							chargeAttack();
					}
					stopDefend();	
				}
			}	
			else if(target.getPlatform() != getPlatform() && !(fallOnPlatform() == target.getPlatform()) &&  !isClimbing() && isReachable(targetPlatform)){
					if(targetPlatform.getPosX() < jumpRangeRight && targetPlatform.getPosX() + targetPlatform.getWidth() > jumpRangeLeft) {
						jumpOnPlatform();
						if(fallOnPlatform() == targetPlatform || getPlatform() == targetPlatform)
							stopMove();
					}
			}		
			else if((getJumps() == 0 && !isFalling() && !isClimbing()) || fallOnPlatform() == targetPlatform)	
					stopMove();
			else
				stopMove();
				
	}
	
	public void jumpOnPlatform() {
		
		calculateRange();

		if(target.isFalling() && isFalling() && !isClimbing()) {
			if(targetPlatform.getPosX()+(targetPlatform.getWidth()/2) > posX+96) {
				if(isMoving())
					stopMove();
				moveRight();
				
					if(getJumps()==0)
						moveUp();
					else if(speedY >=0 || climbing) {
						moveUp();
					}
					
			}	
			else if(targetPlatform.getPosX()+(targetPlatform.getWidth()/2)<posX- 96) {	
				if(isMoving())
					stopMove();
				moveLeft();
				if(getJumps()==0)
					moveUp();
				else if(speedY >=0 || climbing) {
					moveUp();
				}	
			}
		}
			
		else if(isClimbing()) {
			stopMove();

			moveUp();
			if(speedY >=0) {
				moveUp();
			}	
			
		}
		
		else if(target.getJumps()==0 && !target.isFalling() && !target.isClimbing() && !target.isDamaged()) {
			if(targetPlatform.getPosX()+(targetPlatform.getWidth()/2) > posX+96) {
				if(isMoving())
					stopMove();
				moveRight();
				if(targetPlatform.getPosY() < getPosY()){
					if(getJumps()==0)
						moveUp();
					else if(speedY >=0 || climbing) {
						moveUp();
					}
				}
			}	
			else if(targetPlatform.getPosX()+(targetPlatform.getWidth()/2)<posX- 96) {	
				if(isMoving())
					stopMove();
				moveLeft();
				if(targetPlatform.getPosY() < getPosY()){
					if(getJumps()==0)
						moveUp();
					else if(speedY >=0 || climbing) {
						moveUp();
					}
				}
			}
		}
				
	}
	
	public boolean isReachable(AbstractStaticObject targetPlatform) {
		
		if(platform == targetPlatform) {
			return true;
		}	
		else if(platform != null && targetPlatform != null) {
			return (targetPlatform.getPosX() > (platform.getPosX() + platform.getWidth())) || ((targetPlatform.getPosX() + targetPlatform.getWidth()) < platform.getPosX());
		}
		else {
			stopMove();
			return false;
		}
		
	}
	
	public AbstractStaticObject nextPlatform(AbstractStaticObject current) {
		
		for(AbstractStaticObject plat : map.getObjects()) {
			if(plat != current && plat != platform) {
				return plat;
			}	
		}
			return current;
	}
	
	public AbstractStaticObject fallOnPlatform() {
			for(AbstractStaticObject platform : map.getObjects()) {
				if(platform.getPosY() != platform.getPosY())
					if(platform.getPosX()<posX-96 && platform.getPosX() + platform.getWidth() > posX+96 && platform.getPosY() > posY) {
						return platform;
					}		
		}
			return null;
	}

	public void setTarget(AbstractCharacter target) {
		if(target != null)
			this.target = target;
			
	}

	public void notFollowAndDefend() {
		
		calculateRange();
		if(isReachable(target.getPlatform()))
			targetPlatform = target.getPlatform();
		else
			targetPlatform = nextPlatform(target.getPlatform());
		
		if(defenseTimer >= 0) {
			defenseTimer --;
		}
		else {
			stopDefend();
			defenseTimer = 50;
		}	

		if((isFalling() && fallOnPlatform() == null) || isClimbing()) {
			jumpOnPlatform();
		}
		else if(enemyInRange()) {	
			stopMove();
			if(target.isHeavyAttacking() || target.isSoftAttacking())
				defend();
		}
		
		for (AbstractStaticObject platform : map.getObjects()) {
			if(platform != target.getPlatform()) {
				targetPlatform = platform;
				break;
			}
		}
		
}		
		
	public boolean getAiDefenseMode() {
		
		return aiDefenseMode;
			
	}
	
				
	public void actionTimer() {
		if(changeActionTimer > 0) {
			changeActionTimer --;
		}
		else
			changeActionTimer = 400;
		
		if(changeActionTimer == 200 || changeActionTimer == 399) {
			if(!aiDefenseMode)
				aiDefenseMode = true;
			else
				aiDefenseMode = false;
		}
		
	}
	public void calculateRange() {
		if(getType().equals("guy") || getType().equals("gerry")) {
			rangeLeft = posX - 150;
			rangeRight = posX + width+ 150;
		}
		else {
			rangeLeft = getHitbox().getPosX()- (getHitbox().getWidth());
			rangeRight = getHitbox().getPosX() + (getHitbox().getWidth() * 2);
		}
		if(getType().equals("ninja") || getType().equals("gerry")) {
			jumpRangeLeft = posX - 800;
			jumpRangeRight = posX + 800;
		}
		else {
			jumpRangeLeft = posX - 500;
			jumpRangeRight = posX + 500;
		}	
	}
	
	public boolean enemyInRange() {
		
		return (target.getHitbox().getPosX() > rangeLeft && 
				target.getHitbox().getPosX()+target.getHitbox().getWidth() < rangeRight && (target.getPosY() == posY));
			
	}
	
	public int getActionTimer() {
		return changeActionTimer;
	}
}

