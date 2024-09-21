package graphics;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import core.AbstractStaticObject;
import menus.MapEditorMenu;

public class EditorGUI {
	
	private Image backgrounds[];
	private Image platformBox;
	private Image cursorLeft;
	private Image cursorRight;
	private LinkedList<Image> defaultImages;
	private LinkedList<Image> images;
	
	private String currentPath;
	
	private double fraction;
	
	public EditorGUI(MapEditorMenu editor)  {
		this.defaultImages = new LinkedList<Image>();
		this.images = new LinkedList<Image>();
		this.currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/";
		this.fraction = 1.0;
		
		try {
			this.backgrounds = new Image[4];
			this.backgrounds[0] = ImageIO.read(new File(this.currentPath + "resources/img/maps/SandBackground.png"));
			this.backgrounds[1] = ImageIO.read(new File(this.currentPath + "resources/img/maps/TempleBackground.png"));
			this.backgrounds[2] = ImageIO.read(new File(this.currentPath + "resources/img/maps/CityBackground.png"));
			this.backgrounds[3] = ImageIO.read(new File(this.currentPath + "resources/img/menus/EditorBackground.png"));
			this.platformBox = ImageIO.read(new File(this.currentPath + "resources/img/menus/PlatformBox.png"));
			this.cursorLeft = ImageIO.read(new File(this.currentPath + "resources/img/menus/HorizontalCursor.png"));
			this.cursorRight = ImageIO.read(new File(this.currentPath + "resources/img/menus/HorizontalCursor.png"));
			
			//flip img orizzontale
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-cursorLeft.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			cursorLeft = op.filter((BufferedImage) cursorLeft, null);
			
			for(AbstractStaticObject object : editor.getDefaultPlatforms())
				defaultImages.add(ImageIO.read(new File(this.currentPath + "resources/img/maps/" + object.getObjectTag() + ".png")));
			
			resizeImages();
		} catch (IOException e) {
			System.out.println("[ERROR] Failed to load some editor images");
		}
	}
	
	public LinkedList<Image> getDefaultImages()  {
		return defaultImages;
	}
	
	public LinkedList<Image> getImages()  {
		return images;
	}
	
	public Image getBackground(int x)  {
		return backgrounds[x];
	}
	
	public Image getPlatformBox()  {
		return platformBox;
	}
	
	public Image getCursorLeft()  {
		return cursorLeft;
	}
	
	public Image getCursorRight()  {
		return cursorRight;
	}
	
	public void resizeImages()  {
		for(int i = 0; i < defaultImages.size(); i++)  {
			Image img = defaultImages.get(i);
			int width = img.getWidth(null) / 2;
			int height = img.getHeight(null) / 2;
			defaultImages.set(i, img.getScaledInstance(width, height, Image.SCALE_DEFAULT));
		}
	}
	
	public void load(AbstractStaticObject p)  {
		try  {
			Image img = ImageIO.read(new File(this.currentPath + "resources/img/maps/" + p.getObjectTag() + ".png"));
			int width = (int) (p.getWidth() / fraction);
			int height = (int) (p.getHeight() / fraction);
			img = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
			images.add(img);
		}
		catch(IOException e)  {
			System.out.println("[ERROR] Failed to load some editor images");
		}
	}
	
	public void resize(double fraction, double fractionImg)  {
		
		this.fraction = fractionImg;
		
		for(int i = 0; i < backgrounds.length; i++)  {
			int width = backgrounds[i].getWidth(null);
			int height = backgrounds[i].getHeight(null);
			
			backgrounds[i] = backgrounds[i].getScaledInstance((int) (width / fraction), (int) (height / fraction), Image.SCALE_DEFAULT);
		}
		
		cursorLeft = cursorLeft.getScaledInstance((int) (cursorLeft.getWidth(null) / fraction), (int) (cursorLeft.getHeight(null) / fraction), Image.SCALE_DEFAULT);
		cursorRight = cursorRight.getScaledInstance((int) (cursorRight.getWidth(null) / fraction), (int) (cursorRight.getHeight(null) / fraction), Image.SCALE_DEFAULT);
		platformBox = platformBox.getScaledInstance((int) (platformBox.getWidth(null) / fraction), (int) (platformBox.getHeight(null) / fraction), Image.SCALE_DEFAULT);
		
		for(int i = 0; i < defaultImages.size(); i++)  {
			Image img = defaultImages.get(i);
			Image resizedImg = img.getScaledInstance((int) (img.getWidth(null) / fraction), (int) (img.getHeight(null) / fraction), Image.SCALE_DEFAULT);
			defaultImages.set(i, resizedImg);
		}
		
		for(int i = 0; i < images.size(); i++)  {
			Image img = images.get(i);
			Image resizedImg = img.getScaledInstance((int) (img.getWidth(null) / fraction), (int) (img.getHeight(null) / fraction), Image.SCALE_DEFAULT);
			images.set(i, resizedImg);
		}
	}
	
	public void clear()  {
		images.clear();
	}
	
	public void setFraction(double f)  {
		fraction = f;
	}
}
