package graphics;

import java.awt.Graphics;
import java.awt.Image;

import core.AbstractCharacter;
import interfaces.Direction;
import managers.SoundManager;
public class Animator {
	
	SpriteSheet sprites;
	private String type;
	private int index = 0;
	private int count = 0;
	private int frames = 0;
	private int speed = 0;
	private int countBlood = 0;
	private int indexBlood = 0;
	private int imgDim;			//dimensione di ogni sprite in pixel
	private boolean reJump = false;
	private boolean stop = false; // stops at last frame
	private boolean repeat = false; // repeats animation
	private boolean attacking = false;
	private boolean chargedAttack = false;
	private boolean drawDeath = false;
	private boolean hitSound = false;
	
	private Image current = null;
	private Image current_sword = null;

	public Animator (String type) {
		frames = 0;
		this.type = type;
		sprites = new SpriteSheet(type);
		imgDim = 96;
	}
	
	public void drawAnimation(Graphics g, int x, int y,  double fraction, AbstractCharacter chara, boolean options) {
		
		int swordPosX, swordPosY, swordScaleX, swordScaleY;
		int deathX, deathY;
		
		if(!chara.isDamaged())
			hitSound = false;
		
		if(chara.getDirectionX() == Direction.LEFT && !chara.getVictory()) {
			if(chara.isClimbing() || chara.isDamaged())
				current = sprites.getSheetRight();
			else	
				current = sprites.getSheetLeft();
			
			current_sword = sprites.getSwordLeft();
			swordPosX = x - (int) (110 / fraction);  swordPosY = y - (int) (20 / fraction);
			swordScaleX= (int) (150 / fraction); swordScaleY = (int) (100 / fraction);
		}
		else {
			if(chara.isClimbing() || chara.isDamaged())
				current = sprites.getSheetLeft();
			else	
				current = sprites.getSheetRight();
			
			current_sword = sprites.getSwordRight();
			swordPosX = x - (int) (70 / fraction);  swordPosY = y - (int) (20 / fraction);
			swordScaleX= (int) (200 / fraction); swordScaleY = (int) (100 / fraction);
		}
		
		if(type.equals("others")) {
			if(chara.isDefending() && !chara.isDead()) {
				
				if(chara.getAttackDirection() == Direction.RIGHT && chara.isDamaged()) {
					SoundManager.PlaySound("hitShield");
					stop = false;  frames = 4;	speed = 15; repeat = false;
					g.drawImage(sprites.getGuardLeft(), x, y, x + imgDim, y + imgDim, (count)*imgDim, 0, (count)*imgDim+imgDim, imgDim, null);
				}
				else if(chara.getAttackDirection() == Direction.LEFT && chara.isDamaged()) {
					SoundManager.PlaySound("hitShield");
					stop = false;  frames = 4;	speed = 15; repeat = false;
					g.drawImage(sprites.getGuardRight(), x, y, x+imgDim, y+imgDim, (count)*imgDim, 0, (count)*imgDim+imgDim, imgDim, null);
				}
			}	
			
			 if(chara.isChargingAttack()) {
				 stop = false;  frames = 3;	speed = 13; repeat = true;
				 g.drawImage(sprites.getChargingEffect(), x, y, x+imgDim, y+imgDim, (count)*imgDim, 0, count*imgDim+imgDim, imgDim, null);
			 }
		}
		
		else if(type.equals("others2")) {
			if(chara.isStrongAttack() && !chara.isDead()) {
				SoundManager.PlaySound("endCharge");
				stop = false;  frames = 5;	speed = 13; repeat = true;
				g.drawImage(sprites.getChargeComplete(), x, y, x+imgDim, y+imgDim, (count)*imgDim, 0, count*imgDim+imgDim, imgDim, null);
			 }
		}
		
		else {
			
			if(chara.getDeathTime() == 399)
				drawDeath = true;
			
			if((chara.isChargingAttack() || chara.isHeavyAttacking() || chara.isSoftAttacking()) && !attacking) {
				count = 0;
				attacking = true;
			}
			else if( !chara.isChargingAttack() && !chara.isHeavyAttacking() && !chara.isSoftAttacking())
				attacking = false;
			
			
			if(chara.isStrongAttack() || chara.hasPowerSmash()) {
				chargedAttack = true;
			}
			else if(!chara.isHeavyAttacking() && chargedAttack && !chara.isStrongAttack() && !chara.hasPowerSmash())
				chargedAttack = false;
			
			
			if(chara.getJumps() == 2 && !reJump) {
				reJump = true;
				count = 0;
			}
			else if(!(chara.getJumps() == 2))
				reJump = false;
			
		
		//ANIMATIONS
		
			if(chara.getVictory()) {
				if(count >=4)
					count = 0;
				stop = false;  frames = 4;	speed= 8; repeat = true;		
				g.drawImage(current, -50, 50, 300+imgDim, 350+imgDim, count*imgDim, 0, count*imgDim+imgDim, imgDim, null);
			}
		
			else if(chara.isDead() && drawDeath ) {
				deathX = x; deathY = y;
				SoundManager.PlaySound("elimination");
				stop = false;  frames = 31;	speed = 2; repeat = false;
				g.drawImage(sprites.getExplosion(), deathX, deathY, deathX+200, deathY+200, (count)*imgDim, 0, count*imgDim+imgDim, imgDim, null);
				if(count > 30) {
					drawDeath = false;
				}	
			}
			else if(!chara.isDead()) {
				
				 if(chara.isClimbing()) {
					stop = false;  frames = 1;	speed = 8; repeat = true;
					g.drawImage(current, x, y, x+imgDim, y+imgDim, (count)*imgDim, 3*imgDim, (count)*imgDim+imgDim, 3*imgDim+imgDim, null);
				 }
				else if (chara.isFalling() && !chara.isSoftAttacking()){
					if( count >= 3 )
						count = 0;
					stop = true;  frames = 2;	speed = 8; repeat = false;
					g.drawImage(current, x, y, x+imgDim, y+imgDim, (count+3)*imgDim, 2*imgDim, (count+3)*imgDim+imgDim, 2*imgDim+imgDim, null);
				}
				else if(chara.isDefending()) {
					if(chara.isMoving()) {
						stop = false;
						repeat = true;
						frames = 5;
					}	
					else {
						stop = true;
						repeat = false;
						frames = 0;
					}	
					speed = 5;
					g.drawImage(current, x, y, x+imgDim, y+imgDim, (count)*imgDim, 6*imgDim, count*imgDim+imgDim, 6*imgDim+imgDim, null);
				}
				else if (chara.isChargingAttack() ) {
					stop = true;  frames = 1;	speed = 8; repeat = false;
					g.drawImage(current, x, y, x+imgDim, y+imgDim, (count)*imgDim, 5*imgDim, count*imgDim+imgDim, 5*imgDim+imgDim, null);
				}
				
				else if (chara.isHeavyAttacking() ) {
					if ( count >= 5)
						count = 0;
					stop = false;  frames = 4;	speed = 8; repeat = false;
					g.drawImage(current, x, y, x+imgDim, y+imgDim, (count+1)*imgDim, 5*imgDim, (count+1)*imgDim+imgDim, 5*imgDim+imgDim, null);
					if(chargedAttack)  {
						SoundManager.PlaySound("powerSmash");
						int imgWidth = (300*imgDim)/96;
						int imgHeight = (200*imgDim)/96;
						g.drawImage(current_sword, swordPosX, swordPosY, x+swordScaleX, y+swordScaleY, count*imgWidth, 0, count*imgWidth+imgWidth, imgHeight, null);
					}
				}
			
				else if(chara.isSoftAttacking()) {
					if(chara.getType().equals("knight") || chara.getType().equals("ninja"))
						frames = 5;
					else
						frames = 0;
					
					stop = false; speed = 7; repeat = false;
					if(chara.getJumps()>=1)
						g.drawImage(current, x, y, x+imgDim, y+imgDim, (count)*imgDim, 8*imgDim, count*imgDim+imgDim, 8*imgDim+imgDim, null);
					
					else {
						g.drawImage(current, x, y, x+imgDim, y+imgDim, (count)*imgDim, 4*imgDim, count*imgDim+imgDim, 4*imgDim+imgDim, null);
					}
				}
			
				else if(chara.getJumps() == 1) {
					stop = true;  frames = 3;	speed = 8; repeat = false;
					g.drawImage(current, x, y, x+imgDim, y+imgDim, count*imgDim, 2*imgDim, count*imgDim+imgDim, 2*imgDim+imgDim, null);
				}
				else if(chara.getJumps() == 2) {
					stop = true;  frames = 3;	speed = 8; repeat = false;
					g.drawImage(current, x, y, x+imgDim, y+imgDim, count*imgDim, 2*imgDim, count*imgDim+imgDim, 2*imgDim+imgDim, null);
				}
					
				else if(chara.isMoving()) {
					stop = false;  frames = 5;	speed = 5; repeat = true;
					g.drawImage(current, x, y, x+imgDim, y+imgDim, count*imgDim, imgDim, count*imgDim+imgDim, imgDim+imgDim, null);
				}
				
				else {	
					if(count >=4)
						count = 0;
					stop = false;  frames = 4;	speed= 7; repeat = true;		
					g.drawImage(current, x, y, x+imgDim, y+imgDim, count*imgDim, 0, count*imgDim+imgDim, imgDim, null);
				}
			
				 if(chara.isDamaged() && !chara.isDefending()) {
					 
					 if(!hitSound) {
						if(chara.getDamageTaken()<70) {
							SoundManager.PlaySound("hitSound");	
							hitSound = true;
						}
						else {
							SoundManager.PlaySound("heavyHitSound");
							hitSound = true;
						}
						
					 }		
					 
					g.drawImage(sprites.getBlood(), x-20, y-20, x+120, y+120, (countBlood)*imgDim, 0, (countBlood)*imgDim+imgDim, imgDim, null);
					indexBlood++;
					if(indexBlood >= 10) {
						if(countBlood < frames) {
							countBlood++;
							indexBlood = 0;
						}
						else countBlood = 0;
					}
				}
			}	 
		}
			
		if(!options)	
			index++;
		
		if(index >= speed) {
			if(!(count >= frames && stop)) {
				count++;
				index = 0;
			}
		}
		
		else if((count >= frames && repeat && !stop) || (count >= 5 && !stop && !drawDeath) || (count > 5 && stop) || (drawDeath && count>=31)) {  
			count = 0;
		}
		
	}	

	
	
	public void setIndex(int x) {
		index = x;
	}
	
	public void setCount(int x) {
		count = x;
	}
	
	
	public void resize(double fraction, GamePanel panel)  {
		imgDim = (int) (imgDim / fraction);
		sprites.resize(imgDim, panel);
	}
	
}
