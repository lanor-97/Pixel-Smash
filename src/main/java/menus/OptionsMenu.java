package menus;

import interfaces.Position;
import managers.SoundManager;

public class OptionsMenu {
	Position optionsPos[];
	int optionsPosIndex;
	
	String resolutions[];
	int resolutionsIndex;
	
	boolean active;
	boolean soundOn;
	
	public OptionsMenu()  {
		optionsPos = new Position[5];
		optionsPosIndex = 0;
		
		//setting posizioni
		optionsPos[0] = new Position(787, 270);
		optionsPos[1] = new Position(790, 370);
		optionsPos[2] = new Position(565, 472);
		optionsPos[3] = new Position(575, 510);
		optionsPos[4] = new Position(785, 465);	
		
		resolutions = new String[2];
		resolutionsIndex = 0;
		
		resolutions[0] = "1200x800";
		resolutions[1] = "960x640";
		
		active = false;
		soundOn = true;
	}
	
	public void previous()  {
		SoundManager.PlaySound("menuSound");
		if(optionsPosIndex > 0)
			optionsPosIndex--;
	}
	
	public void next()  {
		SoundManager.PlaySound("menuSound");
		if(optionsPosIndex < 4)
			optionsPosIndex++;
	}
	
	/**
	 * @return 1 = resume, 2 = back to menu, 3 = changed resolution
	 */
	public int chose()  {
		if(optionsPosIndex == 0)  {
			close();
			return 1;
		}
		
		if(optionsPosIndex == 1)  {
			close();
			return 2;
		}
		
		if(optionsPosIndex == 2)
			enableSound();
		else if(optionsPosIndex == 3)
			disableSound();
		else  {
			changeResolution();
			return 3;
		}
		
		return 0;
	}
	
	public void close()  {
		active = false;
	}
	
	public void clear()  {
		optionsPosIndex = 0;
	}
	
	public void enableSound()  {
		if(soundOn)
			return;
		
		//attivare suono qui
		soundOn = true;
		SoundManager.UnpauseSounds();
		
	}
	
	public void disableSound()  {
		if(!soundOn)
			return;
		
		//disattivare suono qui
		soundOn = false;
		SoundManager.PauseSounds();
	}
	
	public void changeResolution()  {
		if(resolutionsIndex == resolutions.length - 1)
			resolutionsIndex = 0;
		else
			resolutionsIndex++;
	}
	
	public void activate()  {
		active = true;
		SoundManager.PlaySound("pause");
	}
	
	public int getCursorPosX()  {
		return optionsPos[ optionsPosIndex ].getPosX();
	}
	
	public int getCursorPosY()  {
		return optionsPos[ optionsPosIndex ].getPosY();
	}
	
	public boolean isActive()  {
		return active;
	}
	
	public String getResolution()  {
		return resolutions[ resolutionsIndex ];
	}
}
