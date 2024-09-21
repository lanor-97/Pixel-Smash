package core;

public class Stats {
	private int armour;
	private int attackSpeed;
	private int speed;
	private String type;
	private boolean melee;
	
	public Stats(String name) {	
		
		this.type = name;
		attackSpeed = 5;	//per ora a tutti così, poi vediam
		melee = false;
		
		if( name.equals("guy")) {
			armour = 6;
			speed = 6;
		}
		
		else if (name.equals("ninja")) {
			armour = 4;
			speed = 9;
			melee = true;
		}
		
		else if (name.equals("knight")) {
			armour = 10;
			speed = 4;
			melee = true;
		}
		
		else if (name.equals("gerry")) {
			armour = 8;
			speed = 8;
		}
		else {
			System.out.println("[ERROR] Wrong character name");
		}
	}
	
	public int getArmour() {
		return armour;
	}
	
	public String getType() {
		return type;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getAttackSpeed()  {
		return attackSpeed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public boolean isMelee()  {
		return melee;
	}
}
