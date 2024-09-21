package game;

import graphics.GamePanel;
import managers.SoundManager;
import managers.WorldManager;
import interfaces.GameState;

//TODO redo timer

public class Timer extends Thread  {
	WorldManager world;
	GamePanel panel;
	int FPS = 60;
	int x = 0;
	
	//easy constructor
	public Timer(WorldManager world, GamePanel gp)  {
		this.world = world;
		this.panel = gp;
	}

	//game loop
	public void run()  {
		/*long startTime;
		long URDTimeMs;
		long waitTime;
		long loopTime = 1000 / FPS;*/
		while(true)  {
			/*startTime = System.nanoTime();
			//System.out.println(world.getPlayer().getPos_x());
			//tutto un sistema per fare in modo che gli FPS settati
			//siano effettivamente le operazioni i cicli per secondo
			URDTimeMs = (System.nanoTime() - startTime) / 1000000;
			waitTime = loopTime - URDTimeMs;
			try  {
			Thread.sleep(waitTime);
			} catch (Exception e)  {
				
			}*/
			
			if(panel.getStatus() == GameState.GAME && panel.options() == false) {
				
				world.update();
			}
			panel.repaint();
			
			try  {
				Thread.sleep(1000/60);
			}
			catch(Exception e)  {
			}
		}
	}
}