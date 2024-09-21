package core;

import java.util.concurrent.LinkedBlockingQueue;

import interfaces.*;

public abstract class AbstractCharacter extends AbstractDynamicObject  {
	private Stats stats;
	
	private String ip;
	
	protected AbstractStaticObject platform;
	private boolean defenseMode;
	private boolean movingLeft;
	private boolean movingRight;
	protected boolean climbing;
	private boolean softAttacking;
	private boolean chargingAttack;
	private boolean heavyAttacking;
	private boolean knockbacking;
	protected boolean onAir;
	protected boolean melee;
	protected boolean releaseAttackWhenPossible;
	protected boolean dead;
	protected boolean victory;
	protected boolean powerSmash;
	
	protected int powerImmuneTime;
	protected int speedEffectTime;
	private int attackTime;
	private int jumps;
	private int maxSpeedY;
	private int armour;
	private int immuneTime;
	private int chargingTime;
	private int knockTime;
	private int attackSpeed;
	private int cooldownTime;
	private int deathCount;
	private int deathTime;
	private int heavyAttackDamage;
	private double damageTaken;
	
	private Hitbox hitboxWhenLeft;
	private Hitbox hitboxWhenRight;
	private Hitbox hitboxAttack;

	LinkedBlockingQueue<Projectile> projectiles;
	
	Direction attackDirection;
		
	public AbstractCharacter(int x, int y, int width, int height, String objectTag, Direction direction, String name) {
		super(x, y, width, height, objectTag);
		
		platform = null;
		targetX = posX;
		targetY = posY;
		stats = new Stats(name); 
		speedX = stats.getSpeed();
		armour = stats.getArmour();
		attackSpeed = stats.getAttackSpeed();
		melee = stats.isMelee();
		defenseMode = false;
		movingLeft = false;
		movingRight = false;
		jumps = 0;
		maxSpeedY = 9;
		climbing = false;
		softAttacking = false;
		chargingAttack = false;
		heavyAttacking = false;
		knockbacking = false;
		onAir = false;
		dead = false;
		victory = false;
		powerSmash = false;
		
		releaseAttackWhenPossible = false;
		speedEffectTime = 0;
		attackTime = 0;
		immuneTime = 0;
		powerImmuneTime = 0;
		chargingTime = 0;
		knockTime = 0;
		damageTaken = 0.0;
		cooldownTime = 0;
		attackDirection = Direction.STOP;
		deathCount = 0;
		deathTime = 400;
		heavyAttackDamage = 0;
		//Online
		ip = new String("");
		
		//i primi due parametri sono la distanza (sx e da sopra) dell'img
		//gli altri due sono le dimensioni
		hitboxWhenRight = new Hitbox(27, 27, 24, 69, this);
		hitboxWhenLeft = new Hitbox(35, 27, 23, 69, this);
		
		//melee
		hitboxAttack = new Hitbox(0, 0, 40, 30, this);
		//ranged
		projectiles = new LinkedBlockingQueue<Projectile>();
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	

	public Stats getStats() {
		return stats;
	}
	
	public String getType() {
		return stats.getType();
	}
	
	public void defend()  {
		if(softAttacking || heavyAttacking || chargingAttack)
			return;
		
		defenseMode = true;
	}
	
	public void stopDefend()  {
		defenseMode = false;
	}
	
	public void moveLeft()  {		
		movingLeft = true;
	}
	
	public void moveRight()  {
		movingRight = true;
	}

	public boolean isMoving() {
		return movingLeft || movingRight;
	}

	
	public void moveUp()  {
		if(jumps < 2 && !chargingAttack)  {
			speedY = -maxSpeedY;
			jumps++;
		}		
	}
	
	public void stopMoveLeft()  {
		movingLeft = false;
	}
	
	public void stopMoveRight()  {
		movingRight = false;
	}
	
	public void stopMove() {
		movingLeft = false;
		movingRight = false;
	}
	
	public void update()  {	
		
		if(dead) {
			damageTaken = 0;
			deathTime--;
		}
		if(deathTime <= 0) {
			resetStats();
		}
		else if(!dead){
			
			if(targetX > posX)  {
				directionX = Direction.RIGHT;
			}
			else if(targetX < posX)  {
				directionX = Direction.LEFT;
			}
			
			posX = targetX;
			posY = targetY;
			
			if(knockbacking)  {
				targetX += speedX;
				knockTime--;
				
				if(knockTime == 0)  {
					knockbacking = false;
					speedX = stats.getSpeed();
				}
			}
			else if(movingLeft && !chargingAttack)
				targetX -= speedX;
			else if(movingRight && !chargingAttack)
				targetX += speedX;
			
			if(directionX == Direction.RIGHT && melee)  {
				hitboxAttack.setPosX(hitboxWhenRight.getPosX() + hitboxWhenRight.getWidth());
				hitboxAttack.setPosY(hitboxWhenRight.getPosY());
			}
			else if(directionX == Direction.LEFT && melee)  {
				hitboxAttack.setPosX(hitboxWhenLeft.getPosX() - hitboxAttack.getWidth());
				hitboxAttack.setPosY(hitboxWhenLeft.getPosY());
			}
			
			hitboxWhenLeft.update();
			hitboxWhenRight.update();
			
			if(attackTime == 1)  {
				attackTime--;
				softAttacking = false;
				chargingAttack = false;
				heavyAttacking = false;
				heavyAttackDamage = 0;
			}
			else
				attackTime--;
			
			if(powerImmuneTime > 0) {
				powerImmuneTime --;
			}
			if(immuneTime > 0) {
				immuneTime--;
			}
			if(chargingAttack)
				chargingTime++;
			
			if(cooldownTime > 0)
				cooldownTime--;
			
			onAir = jumps > 0 || isFalling();
			
			if(!melee)  {
				for(Projectile p : projectiles)  {
					p.update();
				}
			}
			
			if(releaseAttackWhenPossible && chargingTime > 60)  {
				releaseAttackWhenPossible = false;
				heavyAttack();
			}
		}
		if(speedEffectTime > 0)  {
			speedEffectTime--;
			
			if(speedEffectTime == 0)
				speedX = stats.getSpeed();
		}
	}
	
	public void applyGravity()  {
		double accelleration = 0.3;
		
		if(climbing)  {
			targetY = posY + maxSpeedY / 4;
			climbing = false;
			return;
		}
		
		targetY = posY + (int) speedY;
		if(speedY + accelleration < maxSpeedY)
			speedY += accelleration;
	}
	
	public void rightCollision(AbstractStaticObject object)  {
		
		int targetX = this.getHitbox().getTargetX();
		int posY = this.getHitbox().getPosY();
		int width = this.getHitbox().getWidth();
		int height = this.getHitbox().getHeight();
		
		if(posY >=  object.getPosY() + object.getHeight() || posY + height <= object.getPosY())
			return;
		
		int difference = Math.abs(targetX + width - object.getPosX());
		int hitboxFactor = 1;
		if(posX + width > object.getPosX())
			hitboxFactor = 3;
		
		if(targetX + width > object.getPosX() && difference <= Math.abs(speedX) * hitboxFactor)  {
			this.targetX = object.getPosX() - width - getHitbox().getLeftDifference();
			climbing = true;
			jumps = 0;
		}
	}
	
	public void leftCollision(AbstractStaticObject object)  {
		
		int targetX = this.getHitbox().getTargetX();
		int posY = this.getHitbox().getPosY();
		int height = this.getHitbox().getHeight();
		
		if(posY >= object.getPosY() + object.getHeight() || posY + height <= object.getPosY())
			return;
		
		int difference = Math.abs(targetX - object.getPosX() - object.getWidth());
		int hitboxFactor = 1;
		if(posX < object.getPosX() + object.getWidth())
			hitboxFactor = 3;
			
		if(targetX < object.getPosX() + object.getWidth() && difference <= Math.abs(speedX) * hitboxFactor)  {
			this.targetX = object.getPosX() + object.getWidth() - getHitbox().getLeftDifference();
			climbing = true;
			jumps = 0;
		}
	}
	
	public boolean bottomCollision(AbstractStaticObject object)  {
		int posX = this.getHitbox().getPosX();
		int width = this.getHitbox().getWidth();
		
		if(speedY < 0 || posX + 5 > object.getPosX() + object.getWidth() || posX + width < object.getPosX() + 5)
			return false;
		
		int difference = Math.abs(targetY + height - object.getPosY())-3;
		if(targetY + height > object.getPosY() && difference <= speedY)  {
			targetY = object.getPosY() - height;
			speedY = 0;
			jumps = 0;
			onAir = false;
			return true;
		}
		return false;
	}
	
	public void topCollision(AbstractStaticObject object)  {
		int targetY = this.getHitbox().getTargetY();
		int posX = this.getHitbox().getPosX();
		int width = this.getHitbox().getWidth();
		
		if(posX > object.getPosX() + object.getWidth() || posX + width < object.getPosX())
			return;
		
		int difference = Math.abs(targetY - object.getPosY() - object.getHeight());
		if(targetY < object.getPosY() + object.getHeight() && difference <= maxSpeedY)  {
			if(speedY < 0)
				speedY = 1;
			this.targetY = object.getPosY() + object.getHeight() - this.getHitbox().getTopDifference();
		}
	}
	
	public boolean isMovingLeft() {
		return movingLeft;
	}
	
	public boolean isMovingRight() {
		return movingRight;
	}
	
	public int getJumps() {
		return jumps;
	}
	
	public void setJumps(int jumps) {
		this.jumps = jumps;
	}

	public boolean isDefending() {
		return defenseMode;
	}
	
	public boolean isFalling()  {
		return speedY > 2 && !climbing;
	}
	
	public Hitbox getHitbox()  {
		if(directionX == Direction.RIGHT)
			return hitboxWhenRight;
		else
			return hitboxWhenLeft;
	}
	
	public void softAttack()  {
		if(heavyAttacking || chargingAttack || attackTime > 0 || cooldownTime > 0 || defenseMode)
			return;
		
		softAttacking = true;
		attackTime = 21;
		
		cooldownTime = 10 * (10 - attackSpeed);			
		
		if(!melee)  {
			cooldownTime = attackTime;
			int startX = 0;
			if(directionX == Direction.LEFT)
				startX = posX;
			else
				startX = posX + width;
			
			//10 = potenza proiettile
			projectiles.add(new Projectile(startX, posY+(height/2), 10, this, directionX));
		}
	}
	
	public void chargeAttack()  {
		if(powerSmash)  {
			chargingTime = 150;
			heavyAttack();
			powerSmash = false;
		}
		
		if(heavyAttacking || chargingAttack || attackTime > 0 || cooldownTime > 0 
				|| defenseMode || onAir)
			return;
		
		chargingAttack = true;
	}
	
	public void heavyAttack()  {
		chargingAttack = false;
		heavyAttacking = true;
		attackTime = 17;
		cooldownTime = 10 * (10 - attackSpeed);
		
		if(!melee)  {
			int startX = 0;
			if(directionX == Direction.LEFT)
				startX = posX;
			else
				startX = posX + width;
			
			projectiles.add(new Projectile(startX, posY+(height/2), chargingTime, this, directionX));
		}
		heavyAttackDamage = chargingTime;
		chargingTime = 0;
	}
	
	public boolean isSoftAttacking()  {
		return softAttacking;
	}
	
	public boolean isChargingAttack()  {
		return chargingAttack;
	}
	
	public boolean isHeavyAttacking()  {
		return heavyAttacking;
	}
	
	public boolean attackCollision(AbstractCharacter enemy)  {
		if(!softAttacking && !heavyAttacking)
			return false;
		
		if(enemy.isImmune())
			return false;
		
		Hitbox enemyHitbox = enemy.getHitbox();
		
		if(hitboxAttack.getPosX() > enemyHitbox.getPosX() + enemyHitbox.getWidth())
			return false;
		
		if(hitboxAttack.getPosX() + hitboxAttack.getWidth() < enemyHitbox.getPosX())
			return false;
		
		if(hitboxAttack.getPosY() > enemyHitbox.getPosY() + enemyHitbox.getHeight())
			return false;
		
		if(hitboxAttack.getPosY() + hitboxAttack.getHeight() < enemyHitbox.getPosY())
			return false;
		
		enemy.setAttackDirection(directionX);
		
		if(softAttacking)
			enemy.hit(40, directionX);
		else
			enemy.hit(heavyAttackDamage, directionX);
		
		return true;
	}
	
	public boolean isImmune()  {
		return immuneTime > 0 || powerImmuneTime > 0;
	}
	
	public void hit(int damage, Direction d)  {
		
		if(isImmune())
			return;
		
		if(chargingAttack)  {
			chargingAttack = false;
			chargingTime = 0;
			cooldownTime = 15;
		}
		
		immuneTime += damage / 2;
		if(defenseMode)
			damage = damage / 2;
		
		damage = (damage*2) / armour;
		damageTaken += damage;
		knockback(damage, d);
	}
	
	public boolean isClimbing() {
		return climbing;
	}
	
	public boolean isDamaged() {
		return knockbacking;
	}
	
	public boolean isStrongAttack() {
		return chargingTime > 70;
	}
	
	public void knockback(int power, Direction d)  {
		if(defenseMode && damageTaken < 100)
			return;
		
		knockbacking = true;
		
		speedX = 8;
		speedY = -8;
		
		if(damageTaken < 20)
			speedY = -4;
		
		if(d == Direction.LEFT)
			speedX = -speedX;
		
		knockTime = (int) (damageTaken / 2);
	}
	
	public Direction getAttackDirection()  {
		return attackDirection;
	}
	
	public void setAttackDirection(Direction direction)  {
		attackDirection = direction;
	}
	
	public void setPlatform(AbstractStaticObject platform) {
		this.platform = platform;
	}
	
	public AbstractStaticObject getPlatform() {
		return platform;
	}
	
	public Hitbox getHitboxAttack() {
		return hitboxAttack;
	}

	public LinkedBlockingQueue<Projectile> getProjectiles() {
		return projectiles;
	}
	
	public void clearProjectiles()  {
		projectiles.clear();
	}
	
	public void removeProjectile(AbstractDynamicObject p)  {
		projectiles.remove(p);
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public int getDeathCount() {
		return deathCount;
	}
	
	public int getDeathTime() {
		return deathTime;
	}
	
	public void setDeath(boolean death) {
		this.dead = death;
		deathCount++;
		projectiles.clear();
	}
	
	public void hitByProjectile(Projectile p)  {
		hit(p.getPower(), p.getDirectionX());
	}
	
	public boolean isMelee()  {
		return melee;
	}
	
	public void stopChargingAttack()  {
		if(chargingTime > 60)
			heavyAttack();
		else if(chargingAttack)
			releaseAttackWhenPossible = true;
	}
	public void setClimbing(boolean b) {
		climbing = b;
		if(climbing)
			jumps = 0;
	}
	public void resetStats() {
		if(platform != null)
			posX = platform.getPosX();
		else
			posX = 300;
		
		posY = 0;
		platform = null;
		targetX = posX;
		targetY = posY;
		stats = new Stats(getType()); 
		speedX = stats.getSpeed();
		armour = stats.getArmour();
		attackSpeed = stats.getAttackSpeed();
		melee = stats.isMelee();
		defenseMode = false;
		movingLeft = false;
		movingRight = false;
		jumps = 0;
		maxSpeedY = 9;
		climbing = false;
		softAttacking = false;
		chargingAttack = false;
		heavyAttacking = false;
		knockbacking = false;
		onAir = false;
		dead = false;
		powerSmash = false;
		releaseAttackWhenPossible = false;
		powerImmuneTime = 0;
		attackTime = 0;
		immuneTime = 0;
		chargingTime = 0;
		knockTime = 0;
		damageTaken = 0;
		cooldownTime = 0;
		deathTime = 400;
		victory = false;
		heavyAttackDamage = 0;
	}
	
	public double getDamageTaken() {
		return damageTaken;
	}
	
	public void setVictory(boolean v) {
		victory = v;
	}
	
	public boolean getVictory() {
		return victory;
	}
	
	public boolean hasPowerSpeed() {
		return speedEffectTime > 0;
	}
	
	public boolean hasPowerSmash() {
		return powerSmash;
	}
	
	public boolean hasPowerImmune() {
		return powerImmuneTime > 0;
	}
	
	public void gotPowerUp(PowerUpType type)  {
		if(type == PowerUpType.LIFE)  {
			if(damageTaken - 20 > 0)
				damageTaken -= 20;
			else 
				damageTaken = 0;
		}
		else if(type == PowerUpType.IMMUNE) {
			powerImmuneTime += 400;	//5 secondi

		}	
		else if(type == PowerUpType.SPEED)  {
			speedX = stats.getSpeed() + 4;
			speedEffectTime = 400;
		}
		else if(type == PowerUpType.SMASH)
			powerSmash = true;
	}
	
	public int getChargingTime()  {
		return chargingTime;
	}

	public void setDeathCount(int deathCount) {
		this.deathCount = deathCount;
	}

	public void setDamageTaken(double damageTaken) {
		this.damageTaken = damageTaken;
	}
	
}
