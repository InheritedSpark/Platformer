package Graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tile {

	public int width;
	public int height;
	public int y;
	public int x;
	public BufferedImage image;

	public Tile(BufferedImage image, int x,int y, int width,int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = image;
	}
	
	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(image, x, y, width,height,null);
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}
}
