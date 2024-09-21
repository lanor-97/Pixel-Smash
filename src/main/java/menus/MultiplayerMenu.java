package menus;

import java.util.Random;

import javax.swing.JFrame;

import core.Player;
import managers.WorldManager;
import online.ClientThread;
import online.ServerThread;

public class MultiplayerMenu extends SingleplayerMenu  {
	
	static MenuTextField joinField;
	
	boolean typedIP;
	boolean joined;
	
	String hostIP;
	String joinIP;
	
	ServerThread server;
	ClientThread client;
	
	public MultiplayerMenu(WorldManager wm, JFrame frame)  {
		super(wm);
		
		modalityPos[0].setPosXY(500, 70);
		modalityPos[1].setPosXY(870, 70);
		firstSelection.setPosXY( modalityPos[0] );
		
		joinField = new MenuTextField(frame);
		typedIP = false;
		joined = false;
		
		hostIP = new String();
		joinIP = new String();
		
		server = new ServerThread(world);
		hostIP = server.getIP();
		
		world.setServer(server);
	}
	
	@Override
	public void clear()  {
		super.clear();
		firstSelection.setPosXY( modalityPos[0] );
		typedIP = false;
		joined = false;
	}
	
	public MenuTextField getField()  {
		return joinField;
	}
	
	public void setFieldVisible(boolean b)  {
		joinField.setVisible(b);
	}
	
	@Override
	public void firstSelectionChose()  {
		
		//host
		if(modalityPosIndex == 0)  {
			server.run();
			selection++;
			player1.setVisible(true);
		}
		else  {
			if(typedIP)  {
				selection++;
				player1.setVisible(true);
				joinIP = joinField.getText();
				client = new ClientThread(world, joinIP);
				client.run();
				cursor.setPosX(charactersPos[charactersPosIndex].getPosX());
				
			}
			else  {
				typedIP = true;
				joinField.requestFocus();
			}
		}		
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
			
			if(!typedIP)
				world.setPlayer(new Player("p1", 400, 50, positionToPlayer(charactersPosIndex)));
			else
				world.setPlayer(new Player("p2", 1000, 50, positionToPlayer(charactersPosIndex)));
			
			if(modalityPosIndex == 1)
				world.getPacket().setClientPlayer(positionToPlayer(charactersPosIndex));
			else
				world.getPacket().setHostPlayer(positionToPlayer(charactersPosIndex));
			
			while(!joined) {
				
				System.out.print("");
				
				if(!world.getPacket().getClientPlayer().equals("") && !world.getPacket().getHostPlayer().equals("")) {
					
					String characterChosen = new String();
					
					if(modalityPosIndex == 0)
						characterChosen = world.getPacket().getClientPlayer();
					else
						characterChosen = world.getPacket().getHostPlayer();
					
					joined = true;
					player2.setVisible(true);
					player2.setPosX(charactersPos[ playerToPosition(characterChosen) ].getPosX());
					player2.setPosY(charactersPos[ playerToPosition(characterChosen) ].getPosY());
					if(!typedIP)
						world.addCharacter(new Player("p2", 1000, 50, characterChosen));
					else
						world.addCharacter(new Player("p1", 400, 50, characterChosen));
					
					characters[ playerToPosition(characterChosen) ] = true;
				}		
			}

			if(modalityPosIndex == 0) {
				cursor = thirdSelection;
				cursor.setVisible(true);
				selection = 3;
			}
			else  {
				//fare qualcosa (graficamente) del tipo waiting for host
				
				fightSelection.setVisible(true);
				cursor = fightSelection;
				selection = 4;
			}
		}
	}
	
	@Override
	public void thirdSelectionChose()  {
		
		switch(mapsPosIndex)  {
		case 0:
			world.setMapName("Sand");
			world.getPacket().setMapName("Sand");
			break;
		case 1:
			world.setMapName("Temple");
			world.getPacket().setMapName("Temple");
			break;
		case 2:
			world.setMapName("City");
			world.getPacket().setMapName("City");
			break;
		}
		
		selection++;
		fightSelection.setVisible(true);
	}

	
	@Override
	public void startGame()  {
		super.startGame();
		
		//solo porta
		
	}
	
	public String getHostIP()  {
		return hostIP;
	}
	
	public void resize(double fraction)  {
		joinField.resize(fraction);
	}
	
	public int playerToPosition(String player)  {
		if(player.equals("guy"))
			return 0;
		if(player.equals("knight"))
			return 1;
		if(player.equals("ninja"))
			return 2;
		if(player.equals("gerry"))
			return 3;
		
		return 5;		//player non presente
	}
	
	public boolean chose()  {
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
			if(modalityPosIndex == 1)
				waitForHostMap();
			
			startGame();
			clear();
			return true;
		}
		return false;
	}
	
	public void waitForHostMap()  {
		
		while(true)  {
			
			System.out.close();
			String map = world.getPacket().getMapName();
			
			if(!map.equals(""))  {
				
				cursor = thirdSelection;
				if(map.equals("Sand"))  {
					thirdSelection.setPosX(  mapsPos[0].getPosX() );
					world.setMapName("Sand");
				}
				else if(map.equals("Temple"))  {
					thirdSelection.setPosX(  mapsPos[1].getPosX() );					
					world.setMapName("Temple");
				}
				else if(map.equals("City"))  {
					thirdSelection.setPosX(  mapsPos[2].getPosX() );
					world.setMapName("City");
				}
				else
					continue;
				
				thirdSelection.setVisible(true);
				
				break;
			}
		}
	}

	public ServerThread getServer() {
		return server;
	}

	public void setServer(ServerThread server) {
		this.server = server;
	}
	
	
	
//	public void waitForHost()  {
//		
//		waitForHostMap();
//		
//		while(!world.getPacket().isReady())
//			System.out.close();
//	}
}
