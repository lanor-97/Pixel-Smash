package menus;

import java.util.Random;
import core.*;
import interfaces.Position;
import managers.SoundManager;
import managers.WorldManager;

public class SingleplayerMenu {
	
	WorldManager world;
	
	//memorizza le posizioni asse x del cursore per la scelta n.2
	protected Position[] modalityPos;
	protected Position[] charactersPos;
	protected Position[] mapsPos;
	
	protected int modalityPosIndex;
	protected int charactersPosIndex;
	protected int mapsPosIndex;
	
	//serve per fare in modo che il cursore non vada sui caratteri già selezionati
	protected boolean[] characters;
	protected int selectedNumber;
	
	Cursor firstSelection;
	
	Cursor player1;
	Cursor player2;
	Cursor com1;
	Cursor com2;
	Cursor com3;
	
	Cursor thirdSelection;
	Cursor fightSelection;
	
	//cursore corrente
	Cursor cursor;
	
	//a quale selezione è, esempio: 1 = scelta tra "brawl" e "boss rush", 2 = scelta dei caratteri etc...
	protected int selection;
	
	public SingleplayerMenu(WorldManager world)  {
		
		this.world = world;
		
		//inizializzo posizioni scelte modalità
		modalityPosIndex = 0;
		modalityPos = new Position[2];
		modalityPos[0] = new Position(570, 60);
		modalityPos[1] = new Position(870, 60);
		
		//inizializzo posizioni scelte carattere
		charactersPosIndex = 0;
		charactersPos = new Position[6];
		for(int i = 0, pos1 = 150; i < 6; i++, pos1 += 225)  {
			charactersPos[i] = new Position(pos1, 250);
			
			if(pos1 > 1050)  {
				charactersPos[i].setPosXY(1050, 380);
			}
		}
		
		//inizializzo posizioni scelte mappa
		mapsPosIndex = 0;
		mapsPos = new Position[4];
		mapsPos[0] = new Position(220, 630);
		mapsPos[1] = new Position(390, 630);
		mapsPos[2] = new Position(560, 630);
		mapsPos[3] = new Position(710, 630);
		
		//inizializzo posizioni iniziali cursori
		firstSelection = new Cursor( modalityPos[0] );
		
		player1 = new Cursor( charactersPos[0] );
		player2 = new Cursor();
		com1 = new Cursor();
		com2 = new Cursor();
		com3 = new Cursor();
		
		thirdSelection = new Cursor( mapsPos[0] );
		fightSelection = new Cursor(new Position(1000, 660));
		
		cursor = player1;
		
		firstSelection.setVisible(true);
		
		selection = 1;
		
		//array di booleani, false -> carattere[i] non selezionato
		characters = new boolean[4];
		for(int i = 0; i < 4; i++)
			characters[i] = false;
		
		selectedNumber = 0;
	}
	
	public void next()  {
		SoundManager.PlaySound("menuSound");
		switch(selection)  {
		case 1:
			modalityPosIndex = 1;
			firstSelection.setPosXY( modalityPos[ modalityPosIndex ] );
			break;
		
		case 2:
			if(charactersPosIndex < 3)  {
				for(int i = charactersPosIndex+1; i < 4; i++)  {
					if(!characters[i])  {
						charactersPosIndex = i;
						break;
					}
					if(i == 3)
						charactersPosIndex = 4;
				}
				
			}
			else if(charactersPosIndex == 3)
				charactersPosIndex++;
			else if(charactersPosIndex == 4)  {
				cursor.setPosY(380);
				charactersPosIndex++;
			}
			
			cursor.setPosX(charactersPos[charactersPosIndex].getPosX());
			cursor.setPosY(charactersPos[charactersPosIndex].getPosY());
			break;
			
		case 3:
			if(mapsPosIndex < 3)
				mapsPosIndex++;
			thirdSelection.setPosX(  mapsPos[ mapsPosIndex ].getPosX() );
			break;
		}
	}
	
	
	public void previous()  {
		
		SoundManager.PlaySound("menuSound");
		switch(selection)  {
		case 1:
			modalityPosIndex = 0;
			firstSelection.setPosXY( modalityPos[ modalityPosIndex ] );
			break;
		
		case 2:
			if(charactersPosIndex < 5)  {
				for(int i = charactersPosIndex-1; i >= 0; i--)  {
					if(!characters[i])  {
						charactersPosIndex = i;
						break;
					}	
				}
			}
			else if(charactersPosIndex == 5)  {
				cursor.setPosY(250);
				charactersPosIndex--;
			}
			
			cursor.setPosX(charactersPos[charactersPosIndex].getPosX());
			cursor.setPosY(charactersPos[charactersPosIndex].getPosY());
			break;
		case 3:
			if(mapsPosIndex > 0)
				mapsPosIndex--;
			thirdSelection.setPosX(  mapsPos[ mapsPosIndex ].getPosX() );
			break;
		}
	}
	
	public boolean chose()  {
		SoundManager.PlaySound("menuSound");
		if(selection == 1)  {
			firstSelectionChose();
		}
		else if(selection == 2)  {
			secondSelectionChose();
		}
		else if(selection == 3)  {
			thirdSelectionChose();
		}
		else if(selection == 4)  {
			startGame();
			clear();
			return true;
		}
		return false;
	}
	
	public void firstSelectionChose()  {
		if(modalityPosIndex == 0)
			world.setBossRushMode(false);
		else
			world.setBossRushMode(true);
		
		selection++;
		player1.setVisible(true);
	}
	
	public void secondSelectionChose()  {
		
		//none
		if(charactersPosIndex == 5)  {
			if(selectedNumber >= 2)  {
				thirdSelection.setVisible(true);
				cursor.setVisible(false);
				selection++;
				
			}
			return;
		}
		
		//random
		if(charactersPosIndex == 4)  {
			
			//non un vero random, penserò poi a modificarlo
			Random rand = new Random();
			int n = rand.nextInt(4);
			
			while(characters[n])
				n = rand.nextInt(4);
			
			charactersPosIndex = n;
			cursor.setPosX(charactersPos[charactersPosIndex].getPosX());
			cursor.setPosY(charactersPos[charactersPosIndex].getPosY());
		}
		
		
		if(selectedNumber == 0)  {
			
			world.setPlayer(new Player("p1", 300, 50, positionToPlayer(charactersPosIndex)));
			if(modalityPosIndex == 1)  {
				
				//primo round: mappa = sand, enemy = random
				world.setMapName("Sand");
				
				selection ++;
				thirdSelection.setVisible(true);
				
				Random rand = new Random();
				int n = rand.nextInt(4);
				
				while(n == charactersPosIndex)
					n = rand.nextInt(4);
				
				world.addEnemyPlayer(new EnemyPlayer("com1", 200, 0, positionToPlayer(n)));
			}
			else  {
				cursor = com1;
				com1.setVisible(true);
				cursor.setPosX(charactersPos[4].getPosX());
				cursor.setPosY(charactersPos[4].getPosY());
			}
		}
		else if(selectedNumber == 1)  {
			cursor = com2;
			com2.setVisible(true);
			cursor.setPosX(charactersPos[4].getPosX());
			cursor.setPosY(charactersPos[4].getPosY());
			world.addEnemyPlayer(new EnemyPlayer("com1", 290, 0, positionToPlayer(charactersPosIndex)));
		}
		else if(selectedNumber == 2)  {
			cursor = com3;
			com3.setVisible(true);
			cursor.setPosX(charactersPos[4].getPosX());
			cursor.setPosY(charactersPos[4].getPosY());
			world.addEnemyPlayer(new EnemyPlayer("com2", 350, 0, positionToPlayer(charactersPosIndex)));
			
		}
		else  {
			selection++;
			thirdSelection.setVisible(true);
			world.addEnemyPlayer(new EnemyPlayer("com3", 410, 0, positionToPlayer(charactersPosIndex)));
		}
		
		selectedNumber++;
		if(charactersPosIndex < 4)
			characters[charactersPosIndex] = true;
		
		charactersPosIndex = 4;
	}
	
	public void thirdSelectionChose()  {
		switch(mapsPosIndex)  {
		case 0:
			world.setMapName("Sand");
			break;
		case 1:
			world.setMapName("Temple");
			break;
		case 2:
			world.setMapName("City");
			break;
		case 3:
			world.setMapName("Custom");
			break;
		}

		for(EnemyPlayer enemy : world.getEnemies())  {
			enemy.setMap(world.getMap());
		}
		selection++;
		fightSelection.setVisible(true);
	}
	
	public void clear()  {
		firstSelection.setPosX(570);
		firstSelection.setPosY(60);
		
		player1.setPosX(140);
		player1.setPosY(250);
		
		thirdSelection.setPosX(mapsPos[0].getPosX());
		
		cursor = player1;
		
		firstSelection.setVisible(true);
		player1.setVisible(false);
		player2.setVisible(false);
		com1.setVisible(false);
		com2.setVisible(false);
		com3.setVisible(false);
		thirdSelection.setVisible(false);
		fightSelection.setVisible(false);		
		
		selection = 1;
		modalityPosIndex = 0;
		charactersPosIndex = 0;
		mapsPosIndex = 0;
		
		for(int i = 0; i < 4; i++)
			characters[i] = false;
		
		selectedNumber = 0;
	}
	
	public String positionToPlayer(int p)  {
		switch(p)  {
		case 0:
			return "guy";
		case 1:
			return "knight";
		case 2:
			return "ninja";
		default:
			return "gerry";
		}
	}
	
	public int cursor1PosX()  {
		return firstSelection.getPosX();
	}
	
	public int cursor1PosY()  {
		return firstSelection.getPosY();
	}
	
	public int cursorP1PosX()  {
		return player1.getPosX();
	}
	
	public int cursorP1PosY()  {
		return player1.getPosY();
	}
	
	public int cursorP2PosX()  {
		return player2.getPosX();
	}
	
	public int cursorP2PosY()  {
		return player2.getPosY();
	}
	
	public int cursorCom1PosX()  {
		return com1.getPosX();
	}
	
	public int cursorCom1PosY()  {
		return com1.getPosY();
	}
	
	public int cursorCom2PosX()  {
		return com2.getPosX();
	}
	
	public int cursorCom2PosY()  {
		return com2.getPosY();
	}
	
	public int cursorCom3PosX()  {
		return com3.getPosX();
	}
	
	public int cursorCom3PosY()  {
		return com3.getPosY();
	}
	
	public int cursor3PosX()  {
		return thirdSelection.getPosX();
	}
	
	public int cursor3PosY()  {
		return thirdSelection.getPosY();
	}
	
	public int cursorFightPosX()  {
		return fightSelection.getPosX();
	}
	
	public int cursorFightPosY()  {
		return fightSelection.getPosY();
	}

	public boolean cursor1() {
		return firstSelection.isVisible();
	}
	
	public boolean cursorP1() {
		return player1.isVisible();
	}

	public boolean cursorP2() {
		return player2.isVisible();
	}

	public boolean cursorCom1() {
		return com1.isVisible();
	}

	public boolean cursorCom2() {
		return com2.isVisible();
	}

	public boolean cursorCom3() {
		return com3.isVisible();
	}

	public boolean cursor3() {
		return thirdSelection.isVisible();
	}

	public boolean cursorFight() {
		return fightSelection.isVisible();
	}
	
	public void startGame()  {
		world.loadMap();
	}
}

