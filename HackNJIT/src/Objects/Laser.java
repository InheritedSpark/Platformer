package Objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.ID;
import Main.Main;

public class Laser extends Object{
	
	double x, y, theta;
	int width, height, length;
	long start, current;
	public Laser(Main game, double x, double y, int width, int height, double theta,  ID id){
		super(game, x, y, width, height, id);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		length = height;
		this.theta = theta;
		start = System.currentTimeMillis();
	}

	public void tick() {
		theta += 0.02;
		//beam();
		
	}


	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g.create();
//		
		g2d.rotate(theta, x+width/2, y+height/2);
		g2d.draw(getBounds());
		g2d.setColor(new Color(255,0,155,155));
		g2d.fill(getBounds());
		g2d.dispose();
		
		
	}

	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle((int)x,(int)y, width, length);
	}
	
	boolean change = false;
	public void beam(){
		change = false;
		current = System.currentTimeMillis();
		if(current - start >= 5000){
			start = current;
			length = 1;
		}
	}

}
