package online;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import core.AbstractCharacter;
import managers.WorldManager;



public class ClientThread implements Runnable {
	protected WorldManager world = null;
	public String HOST = "127.0.0.1";
	public final static int TIMEOUT = 1000;
	public final static int WRITE_BUFFER = 256 * 1024;
	public final static int READ_BUFFER = 256 * 1024;
	public final static int PORT_TCP = 25565;
	public final static int PORT_UDP = 25566;
	public boolean first_packet = false;

	private Client client;
	private Listener listener;
	
	public ClientThread(WorldManager world, String IP) {
		this.world = world;
		this.HOST = IP;
	}

	@Override
	public void run() {
		listener = new ClientListener();

		client = new Client(WRITE_BUFFER, READ_BUFFER);
		client.getKryo().register(Packet.class);
		client.start();
		
		
		
		
		client.addListener(listener);
		try {
			client.connect(TIMEOUT, HOST, PORT_TCP, PORT_UDP);
		} catch (IOException ex) {
			
			//NON faccio nulla ciao
			System.out.println("Non posso connettermi al client");
		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				client.sendTCP(world.getPacket());
				world.getPacket().setMoveUp(false);
				world.getPacket().setSoftAttack(false);
				
				
			}
		}, 0, 1);
	}

	class ClientListener extends Listener {
		
		@Override
		public void disconnected(Connection connection) {
			System.out.println("Qui trieste si stacca tutto del tipo togliamo il player non so");
		}
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof Packet) {
						Packet precived = new Packet();
						precived = (Packet) object;
						world.getPacket().setHostPlayer(precived.getHostPlayer());
						world.getPacket().setEnemy1(precived.getEnemy1());
						world.getPacket().setEnemy2(precived.getEnemy2());
						
						//Imposto il tempo di gioco al client
						world.setCurrentGameTime(precived.getServerTime());
						
						if(world.getPlayer() != null) {
							world.getPacket().setMapName(precived.getMapName());
							world.getPacket().setTargetX(world.getPlayer().getTargetX());
							world.getPacket().setTargetY(world.getPlayer().getTargetY());
							world.getPacket().setPosX(world.getPlayer().getPosX());
							world.getPacket().setPosY(world.getPlayer().getPosY());
							world.getPacket().setDead(world.getPlayer().isDead());
							world.getPacket().setIsClimbing(world.getPlayer().isClimbing());
							
						}
						//LEGGO PACCHETTO MAGGGICO
						for(AbstractCharacter character : world.getCharacters()) {
							if(character.getType().equals(precived.getClientPlayer())) {
								character.setDamageTaken(precived.getClient_damage_count());
								character.setDeathCount(precived.getClient_death_count());
							}
							if(character.getType().equals(precived.getHostPlayer())){
								
								character.setDamageTaken(precived.getServer_damage_count());
								character.setDeathCount(precived.getServer_death_count());
								
								if(precived.isDead() && !character.isDead()) {
									character.setDeath(true);
								}
								
								if(!precived.getMapName().equals("")) {
									character.setTargetX(precived.getTargetX());
									character.setTargetY(precived.getTargetY());
									if(character.getPosX() != precived.getPosX())
										character.setPosX(precived.getPosX());
									if(character.getPosY() != precived.getPosY())
										character.setPosY(precived.getPosY());
								}
								
								//CLIMBING
								if(precived.getIsClimbing()) {
									character.setClimbing(true);
								} else {
									character.setClimbing(false);
								}
								//SALTO
								if(precived.isMoveUp()) {
									character.moveUp();
								} 
								//SINISTRA
								if(precived.isMoveLeft()) {
									character.moveLeft();
								} else {
									character.stopMoveLeft();
								}
								//DESTRA
								if(precived.isMoveRight()) {
									character.moveRight();
								} else {
									character.stopMoveRight();
								}
														
								//CHARGE ATTACK
								if(precived.isChargingAttack()) {
									character.chargeAttack();
								} else {
									character.stopChargingAttack();
								}
								//SOFT ATTACK
								if(precived.isSoftAttack()) {
									character.softAttack();
								} 
								//DEFEND
								if(precived.isDefend()) {
									character.defend();
								} else {
									character.stopDefend();
								}
							}
//							System.out.println(character.isClimbing() + " sto climbando");
//							System.out.println(character.getJumps() + " numSalti");
//							System.out.println(character.isMovingRight() + " mi sto muovendo a destra");
						}
						if(precived.getSpawnPowerUp()) {
							world.getPowerUpHandler().addPowerUp(precived.getPowerUp_posx(), precived.getTypePowerUp());
						}
						
						world.getPacket().setMoveUp(false);
						world.getPacket().setDead(false);
						//world.getPacket().setSoftAttack(false);

						
						if(world.isLevelFinished()) {
							clearPacket();
							client.close();
							}
					}
				}
		
			}
	
	void clearPacket() {
		world.getPacket().clear();
		}
	}