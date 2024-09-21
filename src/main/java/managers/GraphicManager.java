package managers;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import game.*;
import graphics.GamePanel;
import menus.MainMenu;


public class GraphicManager {
	
	WorldManager wm;
	Timer timer;
	GamePanel gp;
	MainMenu mainMenu;
	static JFrame jf;

	public GraphicManager(WorldManager wm) {
		jf = new JFrame("PixelSmash (Alpha Pre-Release)");
		
		jf.setResizable(false);
		jf.getContentPane().setPreferredSize(new Dimension(wm.getBorderWidth(), wm.getBorderHeight()));
	    jf.pack();
		
		this.wm = wm;
		mainMenu = new MainMenu(wm, jf);
		gp = new GamePanel(jf, wm, mainMenu);
		
		
		EventHandler listener = new EventHandler(wm, gp, mainMenu);
		jf.addKeyListener(listener);
		jf.addMouseListener(listener);
		jf.addMouseMotionListener(listener);
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setContentPane(gp);
		
		jf.setFocusable(true);
		jf.setVisible(true);

		jf.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseReleased(e);
                jf.requestFocus();
            }
        });
		
		
	
		addNotify();		
		System.out.println("[GraphicManager] Successfully loaded.");
	}
	
	public void addNotify()  {
		//Facendo questo non ho bisogno di aggiornare ogni volta con draw()
		jf.addNotify();
		if(timer == null)  {
			timer = new Timer(wm, gp);
			timer.start();
		}
	}
	
	public static void resize(int width, int height)  {
		jf.getContentPane().setPreferredSize(new Dimension(width, height));
	    jf.pack();
	}
}
