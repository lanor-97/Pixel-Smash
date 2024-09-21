package graphics;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;



public class SpriteSheet {
	
		private Image sheet_right = null;
		private Image sheet_left = null;
		private Image sword_sheet_left = null;
		private Image sword_sheet_right = null;
		private Image explosion_sheet = null;
		private Image blood = null;
		private Image guard_left = null;
		private Image guard_right = null;
		private Image charging_effect = null;
		private Image charge_complete = null;
		private String currentPath;
		
		public SpriteSheet(String type) {
			currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/";
			
			if(!type.equals("others") && !type.equals("others2"))
				try {
					sheet_right = ImageIO.read(new File(currentPath + "resources/img/characters/character_" + type + "_right.png"));
					sheet_left = ImageIO.read(new File(currentPath + "resources/img/characters/character_"+ type +"_left.png"));
					explosion_sheet = ImageIO.read(new File(currentPath + "resources/img/characters/deathExplosion.png"));
					sword_sheet_left = ImageIO.read(new File(currentPath + "resources/img/characters/SwordExplosion_left.png"));
					sword_sheet_right = ImageIO.read(new File(currentPath + "resources/img/characters/SwordExplosion_right.png"));
					blood = ImageIO.read(new File(currentPath + "resources/img/characters/blood.png"));
					
				}	
					catch(IOException e)  {
					System.out.println("[ERROR] Unable to find sprite "+ type +" sheets on disk. ");
					//GamePanel.setError("[ERROR] Unable to find sprite "+ type +" sheets on disk.");
					//GamePanel.changeStatus(Status.ERROR);
				}	
			else
				try {
					charge_complete = ImageIO.read(new File(currentPath + "resources/img/characters/ChargeComplete.png"));
					charging_effect = ImageIO.read(new File(currentPath + "resources/img/characters/ChargingEffect.png"));
					guard_left = ImageIO.read(new File(currentPath + "resources/img/characters/guard_left.png"));
					guard_right = ImageIO.read(new File(currentPath + "resources/img/characters/guard_right.png"));
					
				} catch(IOException e)  {
					System.out.println("[ERROR] Unable to find sprite "+ type +" sheets on disk. ");
					//GamePanel.setError("[ERROR] Unable to find sprite "+ type +" sheets on disk.");
					//GamePanel.changeStatus(Status.ERROR);
				}
		}
	
	public Image getBlood() {
		return blood;
	}
	public Image getSheetRight(){
		return sheet_right;
	}
	
	public Image getSheetLeft() {
		return sheet_left;
	}
	
	public Image getExplosion() {
		return explosion_sheet;
	}
	public Image getSwordRight() {
		return sword_sheet_right;
	}
	public Image getSwordLeft() {
		return sword_sheet_left;
	}
	public Image getGuardLeft() {
		return guard_left;
	}
	public Image getGuardRight() {
		return guard_right;
	}
	public Image getChargingEffect() {
		return charging_effect;
	}
	public Image getChargeComplete() {
		return charge_complete;
	}
	
	public void resize(int imgDim, GamePanel panel)  {
		
		int swordImgWidth = (1500*imgDim)/96;
		int swordImgHeight = (200*imgDim)/96;
		
		if(sheet_right != null)  {
			sheet_right = sheet_right.getScaledInstance(6*imgDim, 10*imgDim, Image.SCALE_DEFAULT);
			sheet_left = sheet_left.getScaledInstance(6*imgDim, 10*imgDim, Image.SCALE_DEFAULT);
			explosion_sheet = explosion_sheet.getScaledInstance(32*imgDim, imgDim, Image.SCALE_DEFAULT);
			sword_sheet_left = sword_sheet_left.getScaledInstance(swordImgWidth, swordImgHeight, Image.SCALE_DEFAULT);
			sword_sheet_right = sword_sheet_right.getScaledInstance(swordImgWidth, swordImgHeight, Image.SCALE_DEFAULT);
			blood = blood.getScaledInstance(6*imgDim, imgDim, Image.SCALE_DEFAULT);
		}
		else  {
			charge_complete = charge_complete.getScaledInstance(6*imgDim, imgDim, Image.SCALE_DEFAULT);
			charging_effect = charging_effect.getScaledInstance(3*imgDim, imgDim, Image.SCALE_DEFAULT);
			guard_left = guard_left.getScaledInstance(4*imgDim, imgDim, Image.SCALE_DEFAULT);
			guard_right = guard_right.getScaledInstance(4*imgDim, imgDim, Image.SCALE_DEFAULT);
		}
	}
	
}	
