package online;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import core.AbstractCharacter;
import managers.WorldManager;
import online.Packet;


public class ServerThread implements Runnable  {
	protected WorldManager world = null;
	public final static int WRITE_BUFFER = 256 * 1024;
	public final static int READ_BUFFER = 256 * 1024;
	public final static int PORT_TCP = 25565;
	public final static int PORT_UDP = 25566;

	private AtomicBoolean ready = new AtomicBoolean(false);
	private Server server;
	private Listener listener;
	
	public ServerThread(WorldManager world) {
		this.world = world;
	}
	
	@Override
	public void run() {
		
		listener = new ServerListener();		
		server = new Server(WRITE_BUFFER, READ_BUFFER);
		server.addListener(listener);
		server.getKryo().register(Packet.class);
		server.start();
		try {
			server.bind(PORT_TCP, PORT_UDP);
			ready.set(true);
			world.setHostOnline(true);
			
			System.out.println("[ServerThread] Server online on port: " + PORT_TCP);
		} catch (IOException e) {
			server.stop();
			run();
		}
		

	}

	public boolean isReady() {
		return ready.get();
	}
	
	class ServerListener extends Listener{
		
		@Override
		public void disconnected(Connection connection) {
			for(AbstractCharacter character : world.getCharacters()) {
				if(character.getStats().getType().equals(world.getPacket().getClientPlayer())){
					world.getCharacters().remove(character);
					world.getWinner();
				}
			}
		}
		
	
		
		
		@Override
		public void received(Connection connection, Object object) {
			if(object instanceof Packet) {
				Packet precived = new Packet();
				precived = (Packet) object;
				
				
				//Imposto il personaggio del client
				world.getPacket().setClientPlayer(precived.getClientPlayer());
				//Imposto il danno del server
				world.getPacket().setServer_damage_count((int) world.getPlayer().getDamageTaken());
				world.getPacket().setServer_death_count(world.getPlayer().getDeathCount());
				
				if(world.getPlayer() != null){
					//Aggiorno il tempo di gioco
					world.getPacket().setServerTime(world.getCurrentGameTime());
					//Mando le posizioni del server al client
					world.getPacket().setTargetX(world.getPlayer().getTargetX());
					world.getPacket().setTargetY(world.getPlayer().getTargetY());
					world.getPacket().setPosX(world.getPlayer().getPosX());
					world.getPacket().setPosY(world.getPlayer().getPosY());
					world.getPacket().setDead(world.getPlayer().isDead());
					world.getPacket().setIsClimbing(world.getPlayer().isClimbing());
				}
				for(AbstractCharacter character : world.getCharacters()) {
					if(character.getStats().getType().equals(precived.getClientPlayer())){
						
						world.getPacket().setClient_damage_count((int) character.getDamageTaken());
						world.getPacket().setClient_death_count(character.getDeathCount());
						
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
//						CLIMBING
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
				}
					
				
				//System.out.println("Server recived: " + precived.toString()
				connection.sendTCP(world.getPacket());
				
				world.getPacket().setMoveUp(false);
				world.getPacket().setSoftAttack(false);
				world.getPacket().setSpawnPowerUp(false);
				world.getPacket().setDead(false);
				
				if(world.isLevelFinished())
					stopServer();
				
			}
		}	
	}
	
	
	public void stopServer() {
		if(server != null) {
			server.stop();
			world.setHostOnline(false);
			System.out.println("[ServerThread] Server offline.");
		}
			
	}

		
	public String getIP()  {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "ip non valido";
		}
	}
}
