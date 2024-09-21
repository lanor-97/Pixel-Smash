package online;


public class Packet {
	
	String HostPlayer;
	String ClientPlayer;
	String Enemy1;
	String Enemy2;
	String MapName;
	
	boolean moveLeft;
	boolean moveRight;
	boolean ChargingAttack;
	boolean softAttack;
	boolean defend;
	boolean moveUp;
	boolean spawnPowerUp;
	boolean isClimbing;
	boolean isDead;
	
	int powerUp_posx;
	int typePowerUp;
	
	int targetX;
	int targetY;
	int posX;
	int posY;
	
	int serverTime;
	
	int server_death_count;
	int client_death_count;
	
	int server_damage_count;
	int client_damage_count;
	
	
	
	
	public Packet() {
		HostPlayer = new String("");
		ClientPlayer = new String("");
		Enemy1 = new String("");
		Enemy2 = new String("");
		MapName = new String("");

		
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}
	
	public String getHostPlayer() {
		return HostPlayer;
	}

	public void setHostPlayer(String hostPlayer) {
		HostPlayer = hostPlayer;
	}

	public String getClientPlayer() {
		return ClientPlayer;
	}
	
	public String getEnemy1() {
		return Enemy1;
	}

	public void setEnemy1(String enemy1) {
		Enemy1 = enemy1;
	}

	public String getEnemy2() {
		return Enemy2;
	}

	public void setEnemy2(String enemy2) {
		Enemy2 = enemy2;
	}

	public void setClientPlayer(String clientPlayer) {
		ClientPlayer = clientPlayer;
	}
	
	public String getMapName() {
		return MapName;
	}
	

	public void setMapName(String mapName) {
		MapName = mapName;
	}
	
	public int getServerTime() {
		return serverTime;
	}

	public void setServerTime(int serverTime) {
		this.serverTime = serverTime;
	}

	public boolean isMoveLeft() {
		return moveLeft;
	}

	public void setMoveLeft(boolean moveLeft) {
		this.moveLeft = moveLeft;
	}

	public boolean isMoveRight() {
		return moveRight;
	}

	public void setMoveRight(boolean moveRight) {
		this.moveRight = moveRight;
	}

	public boolean isDead() {
		return isDead;
	}
	
	public void setDead(boolean b) {
		isDead = b;
	}
	
	public boolean isChargingAttack() {
		return ChargingAttack;
	}

	public void setChargingAttack(boolean chargingAttack) {
		ChargingAttack = chargingAttack;
	}

	public boolean isSoftAttack() {
		return softAttack;
	}

	public void setSoftAttack(boolean softAttack) {
		this.softAttack = softAttack;
	}

	public boolean isDefend() {
		return defend;
	}

	public void setDefend(boolean defend) {
		this.defend = defend;
	}

	public boolean isMoveUp() {
		return moveUp;
	}

	public void setMoveUp(boolean moveUp) {
		this.moveUp = moveUp;
	}
	
	public int getPowerUp_posx() {
		return powerUp_posx;
	}
	public boolean getSpawnPowerUp() {
		return spawnPowerUp;
	}
	public int getTypePowerUp() {
		return typePowerUp;
	}
	public void setPowerUp(int powerUp_targetx, int typePowerUp) {
		this.powerUp_posx = powerUp_targetx;
		this.typePowerUp = typePowerUp;
	}

	public void setSpawnPowerUp(boolean b) {
		spawnPowerUp = b;
	}
	
	public boolean getIsClimbing() {
		return isClimbing;
	}
	
	public void setIsClimbing(boolean b) {
		isClimbing = b;
	}
	
	public void setPosX(int x) {
		posX = x;
	}
	
	public void setPosY(int y) {
		posY = y;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public void clear() {
		HostPlayer = "";
		ClientPlayer = "";
		Enemy1 = "";
		Enemy2 = "";
		MapName = "";
		
		moveLeft = false;
		moveRight = false;
		ChargingAttack = false;
		softAttack = false;
		defend = false;
		moveUp = false;
		isDead = false;
		isClimbing = false;
		
		targetX = 0;
		targetY = 0;
		posX = 0;
		posY = 0;
		
		
		serverTime = 999;
	}
	
	public int getServer_death_count() {
		return server_death_count;
	}

	public void setServer_death_count(int server_death_count) {
		this.server_death_count = server_death_count;
	}

	public int getClient_death_count() {
		return client_death_count;
	}

	public void setClient_death_count(int client_death_count) {
		this.client_death_count = client_death_count;
	}

	public int getServer_damage_count() {
		return server_damage_count;
	}

	public void setServer_damage_count(int server_damage_count) {
		this.server_damage_count = server_damage_count;
	}

	public int getClient_damage_count() {
		return client_damage_count;
	}

	public void setClient_damage_count(int client_damage_count) {
		this.client_damage_count = client_damage_count;
	}

	@Override
	public String toString() {
		return "Packet [HostPlayer=" + HostPlayer + ", ClientPlayer=" + ClientPlayer + ", Enemy1=" + Enemy1
				+ ", Enemy2=" + Enemy2 + ", MapName=" + MapName + ", targetX=" + targetX + ", targetY=" + targetY + "]";
	}
}