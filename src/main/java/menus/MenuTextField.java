package menus;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

import javax.swing.JTextField;

public class MenuTextField extends JTextField  {
	
	static final long serialVersionUID = 12;
	private int posX;
	private int posY;
	private int width;
	private int height;
	
	public MenuTextField(JFrame frame)  {
		super();
		
		Dimension dim = frame.getSize();
		
		posX = 935;
		posY = 85;
		width = 180;
		height = 35;
		
		double fraction = 1200 / (dim.getWidth());
		this.setBounds((int) (posX / fraction), (int) (posY / fraction), (int) (width / fraction), (int) (height / fraction));
		
		addKeyListener(new KeyAdapter()  {
			@Override
            public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE)  {
					frame.requestFocus();
				}
            }
		});
		
		//TODO
//		addMouseListener(new MouseAdapter()  {
//			@Override
//			public void mouseClicked(MouseEvent e)  {
//				super.mouseReleased(e);
//				frame.requestFocus();
//			}
//		});
		
		this.setVisible(false);
	}
	
	public void resize(double fraction)  {
		
		int posX = (int) (this.posX / fraction);
		int posY = (int) (this.posY / fraction);
		int width = (int) (this.width / fraction);
		int height = (int) (this.height / fraction);
		
		this.setLocation(new Point(posX, posY));
		this.setSize(new Dimension(width, height));
	}
}
